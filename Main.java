import cs2030.simulator.Customer;
import cs2030.simulator.GreedyCustomer;
import cs2030.simulator.ServerControl;
import cs2030.simulator.Event;
import cs2030.simulator.EventComparator;
import cs2030.simulator.Simulator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
    /**
     * Main procedure.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int seed = sc.nextInt();
        int noServer = sc.nextInt();
        int noSelfCheckout = sc.nextInt();
        int maxQueueSize = sc.nextInt();
        int noCustomers = sc.nextInt();
        double lambda = sc.nextDouble();
        double mu = sc.nextDouble();
        double rho = sc.nextDouble();
        double probRest = sc.nextDouble();
        double probGreedy = sc.nextDouble();


        Simulator simulator = new Simulator(seed, noServer, noSelfCheckout, maxQueueSize,
                noCustomers, lambda, mu, rho, probRest, probGreedy);

        simulator.run();

        sc.close();
    }
}
