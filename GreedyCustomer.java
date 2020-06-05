package cs2030.simulator;

public class GreedyCustomer extends Customer {
    /**
     * Constructor for GreedyCustomer class.
     *
     * @param id   The id of the customer to be served.
     * @param time The time at which the customer arrives.
     */
    public GreedyCustomer(int id, double time) {
        super(id, time);
    }

    /**
     * Returns the String representation of the greedy customer.
     *
     * @return String representation of the greedy customer
     */
    public String toString() {
        return String.format("%d(greedy)", id);
    }
}
