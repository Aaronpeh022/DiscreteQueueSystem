abstract class Event {
    protected final Customer customer;
    protected final int priority;
    protected final double eventTime;
    protected final String serverName;
    protected final boolean printable;

    Event(Customer customer, int priority, double eventTime, String serverName) {
        this.customer = customer;
        this.priority = priority;
        this.eventTime = eventTime;
        this.serverName = serverName;
        this.printable = true;
    }

    Event(Customer customer, int priority, double eventTime, String serverName, boolean printable) {
        this.customer = customer;
        this.priority = priority;
        this.eventTime = eventTime;
        this.serverName = serverName;
        this.printable = printable;
    }

    public boolean getPrintable() {
        return this.printable;
    }

    public double getEventTime() {
        return this.eventTime;
    }

    public int getPriority() {
        return this.priority;
    }

    public String getServerName() {
        return this.serverName;
    }

    public ImList<Double> calculate(ImList<Double> statistics, ServerManager serverManager) {
        return statistics;
    }

    abstract Pair<Event, ServerManager> nextEvent(ServerManager serverManager);

    public int getCustomerNum() {
        return this.customer.getCustomerNum();
    }

}
