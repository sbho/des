package cs2030.simulator;

public class Customer {
    protected final int id;
    protected final double time;

    /**
     * Constructor for Customer class.
     *
     * @param id   The id of the customer to be served.
     * @param time The time at which the customer arrives.
     */
    public Customer(int id, double time) {
        this.id = id;
        this.time = time;
    }

    /**
     * Returns the id of the customer.
     *
     * @return The id of the customer.
     */
    public int getID() {
        return this.id;
    }

    /**
     * Returns the time the customer arrives.
     *
     * @return the time the customer arrives.
     */
    public double getTime() {
        return this.time;
    }

    /**
     * Returns the String representation of the Customer.
     *
     * @return String representation of the Customer
     */
    public String toString() {
        return Integer.toString(id);
    }
}