package cs2030.simulator;

public abstract class Event {
    protected double time;
    protected Customer customer;
    protected Server server;
    protected String action;

    /**
     * Constructor for Event class.
     *
     * @param time     The time the event occurs
     * @param customer The customer involved
     * @param server   The server involved
     */
    public Event(double time, Customer customer, Server server) {
        this.time = time;
        this.customer = customer;
        this.server = server;
    }

    /**
     * Generates an arrive event.
     *
     * @param time     The time the customer arrives
     * @param customer The customer involved
     * @return an arrive event
     */
    public static Event createArriveEvent(double time, Customer customer) {
        return new Event(time, customer, null) {
            @Override
            public Event getNext(ServerControl s) {
                if (s.canServe() == -1) {
                    return Event.createLeaveEvent(time, customer);
                } else if (s.canServe() == 0) {
                    Server x = (customer instanceof GreedyCustomer)
                            ? s.getShortestQueueServer()
                            : s.getFirstFreeServer();
                    return Event.createWaitEvent(time, customer, x);
                } else {
                    Server x = (customer instanceof GreedyCustomer)
                            ? s.getShortestQueueServer()
                            : s.getFirstFreeServer();
                    x.serve();
                    return Event.createServeEvent(time, customer, x);
                }
            }

            @Override
            public String toString() {
                return String.format("%.3f %s arrives", time, customer.toString());
            }
        };
    }

    /**
     * Generates a done event.
     *
     * @param time     The time the customer arrives
     * @param customer The customer involved
     * @return a done event.
     */
    public static Event createDoneEvent(double time, Customer customer, Server server) {
        return new Event(time, customer, server) {
            @Override
            public Event getNext(ServerControl s) {
                server.clear();

                if (!(server instanceof SelfCheckoutServer) && s.rng.genRandomRest() < s.probRest) {
                    return Event.createServerRest(time, server);
                }

                if (!server.waitingQueue.isEmpty()) {
                    Customer nextCustomer = server.waitingQueue.poll();
                    s.updateStatsTotalWaitingTime(time - nextCustomer.getTime());
                    return Event.createServeEvent(time, nextCustomer, server);
                }
                return null;
            }

            @Override
            public String toString() {
                return String.format("%.3f %s done serving by %s",
                        time, customer.toString(), server.toString());
            }
        };

    }

    /**
     * Generates a leave event.
     *
     * @param time     The time the customer arrives
     * @param customer The customer involved
     * @return a leave event.
     */
    public static Event createLeaveEvent(double time, Customer customer) {
        return new Event(time, customer, null) {
            @Override
            public Event getNext(ServerControl s) {
                s.updateStatsLeft();
                return null;
            }

            @Override
            public String toString() {
                return String.format("%.3f %s leaves", time, customer.toString());
            }

        };
    }

    /**
     * Generates a serve event.
     *
     * @param time     The time the customer arrives
     * @param customer The customer involved
     * @return a serve event.
     */
    public static Event createServeEvent(double time, Customer customer, Server server) {
        return new Event(time, customer, server) {
            @Override
            public Event getNext(ServerControl s) {
                server.serve();
                s.updateStatsServed();
                double serviceTime = s.rng.genServiceTime();
                return Event.createDoneEvent(time + serviceTime, customer, server);
            }

            @Override
            public String toString() {
                return String.format("%.3f %s served by %s",
                        time,
                        customer.toString(),
                        server.toString());
            }
        };
    }

    /**
     * Generates a server rest event.
     *
     * @param time The time the server rests.
     * @return a server rest event.
     */
    public static Event createServerRest(double time, Server server) {
        return new Event(time, new Customer(0, 0), server) {

            @Override
            public Event getNext(ServerControl s) {
                server.serve();
                double restingTime = s.rng.genRestPeriod();
                return Event.createServerBack(time + restingTime, server);
            }

            @Override
            public String toString() {
                return "";
            }
        };
    }

    /**
     * Generates a server back event.
     *
     * @param time The time server is back from rest.
     * @return a server back event.
     */
    public static Event createServerBack(double time, Server server) {
        return new Event(time, new Customer(0, 0), server) {

            @Override
            public Event getNext(ServerControl s) {
                if (!server.waitingQueue.isEmpty()) {
                    Customer nextCustomer = server.waitingQueue.poll();
                    s.updateStatsTotalWaitingTime(time - nextCustomer.getTime());
                    return Event.createServeEvent(time, nextCustomer, server);
                }
                server.clear();
                return null;
            }

            @Override
            public String toString() {
                return "";
            }
        };
    }

    /**
     * Generates a wait event.
     *
     * @param time     The time server is back from rest.
     * @param customer The customer involved.
     * @param server   The server involved.
     * @return a wait event
     */
    public static Event createWaitEvent(double time, Customer customer, Server server) {
        return new Event(time, customer, server) {
            @Override
            public Event getNext(ServerControl s) {
                server.waitingQueue.add(customer);
                return null;
            }

            @Override
            public String toString() {
                return String.format("%.3f %s waits to be served by %s",
                        time,
                        customer.toString(),
                        server.toString());
            }
        };
    }

    /**
     * Returns the next Event of DES and set the state of DES.
     *
     * @return the next Event.
     */
    public abstract Event getNext(ServerControl s);

}
