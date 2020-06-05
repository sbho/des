package cs2030.simulator;

import java.util.Comparator;

public class EventComparator implements Comparator<Event> {
    /**
     * Custom compare method to compare events.
     *
     * @return a negative integer or a positive integer.
     */
    public int compare(Event first, Event second) {
        Double firstTime = first.time;
        Double secondTime = second.time;
        Integer firstID = first.customer.getID();
        Integer secondID = second.customer.getID();

        if (secondTime.compareTo(firstTime) == 0) {
            return firstID.compareTo(secondID);
        } else {
            return firstTime.compareTo(secondTime);
        }
    }
}