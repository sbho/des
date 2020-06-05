package cs2030.simulator;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class ServerControl {
    private Server[] servers;
    protected RandomGenerator rng;
    private int noServed = 0;
    private int noLeft = 0;
    protected double probRest;
    private double totalWaitingTime = 0;

    /**
     * Constructor for ServerControl class.
     */
    private ServerControl(Server[] servers, double probRest,
                          RandomGenerator rng) {
        this.servers = servers;
        this.probRest = probRest;
        this.rng = rng;
    }

    /**
     * Creates an instance of ServerControl with self-checkout servers.
     * @return  an instance of ServerControl with self-checkout servers.
     */
    public static ServerControl createSCWithSelfCheck(int noServer,
                                                      int noSelfCheckout,
                                                      int maxQueueSize,
                                                      double probRest,
                                                      int seed,
                                                      double lambda,
                                                      double mu,
                                                      double rho) {
        Server[] servers = new Server[noServer + noSelfCheckout];
        for (int i = 0; i < noServer; i++) {
            servers[i] = new Server(i + 1, maxQueueSize);
        }
        Queue<Customer> selfCheckOutQueue = new LinkedList<>();
        for (int i = 0; i < noSelfCheckout; i++) {
            servers[noServer + i] = new SelfCheckoutServer(
                    noServer + i + 1,
                    maxQueueSize,
                    selfCheckOutQueue
            );
        }
        RandomGenerator rng =
                new RandomGenerator(seed, lambda, mu, rho);
        return new ServerControl(servers, probRest, rng);
    }

    /**
     * Checks if there is a server to serve the given customer.
     *
     * @return 1 if there is a free server, 0 if there is a server with free queue, and -1
     *     if all queues are full.
     */
    public int canServe() {
        for (Server s : servers) {
            //There is a free server.
            if (s.isFree()) {
                return 1;
            }
        }
        for (Server s : servers) {
            //There is a server to wait for.
            if (!s.queueLimitReached()) {
                return 0;
            }
        }
        return -1;
    }

    /**
     * Returns the first server that is not serving anyone, if exists.
     *
     * @return the first server that is not serving anyone, if exists.
     *     null if there is no free server.
     */
    public Server getFirstFreeServer() {
        for (Server s : servers) {
            if (s.isFree()) {
                return s;
            }
        }
        for (Server s : servers) {
            if (!s.queueLimitReached()) {
                return s;
            }
        }
        return null;
    }

    /**
     * Returns the server with the shortest queue.
     *
     * @return the server with the shortest queue.
     */
    public Server getShortestQueueServer() {
        for (Server s : servers) {
            if (s.isFree()) {
                return s;
            }
        }

        PriorityQueue<Server> serversWithFreeSpace = new PriorityQueue<>((s1, s2)
            -> s1.waitingQueue.size() - s2.waitingQueue.size());

        for (Server s : servers) {
            if (!s.queueLimitReached()) {
                serversWithFreeSpace.add(s);
            }
        }

        return serversWithFreeSpace.poll();
    }

    public void updateStatsServed() {
        noServed++;
    }

    public void updateStatsLeft() {
        noLeft++;
    }

    public void updateStatsTotalWaitingTime(double waitingTime) {
        totalWaitingTime += waitingTime;
    }

    public void printStats() {
        System.out.println(String.format("[%.3f %d %d]",
                totalWaitingTime / noServed, noServed, noLeft));
    }

}
