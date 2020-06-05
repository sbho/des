package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.Scanner;

public class Simulator {
    int seed;
    int noServer;
    int noSelfCheckout;
    int maxQueueSize;
    int noCustomers;
    double lambda;
    double mu;
    double rho;
    double probRest;
    double probGreedy;

    /**
     * Constructor for Simulator class.
     */
    public Simulator(int seed,
                     int noServer,
                     int noSelfCheckout,
                     int maxQueueSize,
                     int noCustomers,
                     double lambda,
                     double mu,
                     double rho,
                     double probRest,
                     double probGreedy) {
        this.seed = seed;
        this.noServer = noServer;
        this.noSelfCheckout = noSelfCheckout;
        this.maxQueueSize = maxQueueSize;
        this.noCustomers = noCustomers;
        this.lambda = lambda;
        this.mu = mu;
        this.rho = rho;
        this.probRest = probRest;
        this.probGreedy = probGreedy;
    }

    /**
     * Runs to DES.
     */
    public void run() {
        PriorityQueue<Event> pq = new PriorityQueue<>(new EventComparator());
        ServerControl serverControl = ServerControl.createSCWithSelfCheck(noServer, noSelfCheckout,
                maxQueueSize, probRest, seed, lambda, mu, rho);

        double t = 0.0;

        for (int i = 1; i <= noCustomers; i++) {
            Customer c = serverControl.rng.genCustomerType() < probGreedy
                    ? new GreedyCustomer(i, t)
                    : new Customer(i, t);
            pq.add(Event.createArriveEvent(t, c));
            t += serverControl.rng.genInterArrivalTime();
        }

        while (pq.peek() != null) {
            Event e = pq.poll();
            if (!e.toString().equals("")) {
                System.out.println(e);
            }
            Event next = e.getNext(serverControl);
            if (next != null) {
                pq.add(next);
            }
        }

        serverControl.printStats();

    }

}
