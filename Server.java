package cs2030.simulator;

import java.util.LinkedList;
import java.util.Queue;

public class Server {
    protected int id;
    protected boolean isBusy = false;
    protected int maxQueueSize;
    protected Queue<Customer> waitingQueue = new LinkedList<>();

    /**
     * Constructor for Server class.
     */
    public Server(int id, int maxQueueSize) {
        this.id = id;
        this.maxQueueSize = maxQueueSize;
    }

    /**
     * Sets the server's state to serving some customer.
     */
    public void serve() {
        isBusy = true;
    }

    /**
     * Sets the server's state to not serving anyone.
     */
    public void clear() {
        isBusy = false;
    }

    /**
     * Checks if server is free.
     *
     * @return true if the server is not serving any customer, false otherwise
     */
    public boolean isFree() {
        return !this.isBusy;
    }

    /**
     * Checks whether the server's queue has reached its limit.
     *
     * @return true if the server's queue has reached its limit, false otherwise
     */
    public boolean queueLimitReached() {
        return waitingQueue.size() == maxQueueSize;
    }

    /**
     * Returns the String representation of the server.
     *
     * @return the String representation of the server.
     */
    public String toString() {
        return String.format("server %d", id);
    }

}