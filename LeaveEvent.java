class LeaveEvent extends Event {

    private static final int PRIORITY = 5;
    private static final int STATISTICS_INDEX = 2;
    
    LeaveEvent(Customer customer, double eventTime, String serverName) {
        super(customer, PRIORITY, eventTime, serverName);
    }

    @Override
    public ImList<Double> calculate(ImList<Double> statistics, ServerManager serverManager) {
        double customersServed = statistics.get(STATISTICS_INDEX) + 1;
        return statistics.set(STATISTICS_INDEX, customersServed);
    }

    //placeholder
    @Override
    public Pair<Event, ServerManager> nextEvent(ServerManager serverManager) {
        return new Pair<Event, ServerManager>(this, serverManager);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s leaves",
        this.eventTime, this.customer.getCustomerNum());
    }
}
