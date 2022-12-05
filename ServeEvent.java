class ServeEvent extends Event {
    private static final int SERVE_PRIORITY = 3;
    private static final int STATISTICS_INDEX = 1;

    ServeEvent(Customer customer, double eventTime, String serverName) {
        super(customer, SERVE_PRIORITY, eventTime, serverName);
    }

    @Override
    public ImList<Double> calculate(ImList<Double> statistics, ServerManager serverManager) {
        double customersServed = statistics.get(STATISTICS_INDEX) + 1;
        return statistics.set(STATISTICS_INDEX, customersServed);
    }

    @Override
    public Pair<Event, ServerManager> nextEvent(ServerManager serverManager) {
        AbstractServer tempServer = serverManager.getServer(this.serverName);
        AbstractServer returnServer = tempServer.serveCustomer(
                Math.max(customer.getArrivalTime(),
                        tempServer.getServiceEndTime()) + customer.getServiceTime());
        Event done = new DoneEvent(customer, returnServer.getServiceEndTime(),
                super.getServerName());
        if (returnServer.getQueueSize() > 0) {
            returnServer = returnServer.dequeue();
        }
        serverManager = serverManager.updateServer(returnServer);
        return new Pair<Event, ServerManager>(done, serverManager);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s serves by %s", 
        this.eventTime, this.customer.getCustomerNum(),
                super.getServerName());
    }
}
