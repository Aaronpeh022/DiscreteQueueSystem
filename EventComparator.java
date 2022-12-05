import java.util.Comparator;

class EventComparator implements Comparator<Event> {

    @Override
    public int compare(Event e1, Event e2) {
        if (e1.getEventTime() == e2.getEventTime()) {
            if (e1.getCustomerNum() == e2.getCustomerNum()) {
                return e1.getPriority() - e2.getPriority();
            } else {
                return e1.getCustomerNum() - e2.getCustomerNum();
            }
        } else if (e1.getEventTime() < e2.getEventTime()) {
            return -1;
        } else {
            return 1;
        }
    }
}