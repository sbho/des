package cs2030.simulator;

import java.util.Queue;

public class SelfCheckoutServer extends Server {
    /**
     * Constructor for SelfCheckoutServer class.
     *
     * @param id           The id of the server.
     * @param maxQueueSize The maximum queue length the server accepts.
     * @param waitingQueue The queue of the server.
     */
    public SelfCheckoutServer(int id, int maxQueueSize, Queue<Customer> waitingQueue) {
        super(id, maxQueueSize);
        this.waitingQueue = waitingQueue;
    }

    /**
     * Returns the String representation of the server.
     *
     * @return the String representation of the server.
     */
    public String toString() {
        return String.format("self-check %d", id);
    }
}
