class WaitEvent extends Event {
    private static final int PRIORITY = 2;
    private static final int STATISTICS_INDEX = 0;
    private final String waitAtServerName;

    WaitEvent(Customer customer, double eventTime, String serverName, String waitAtServerName) {
        super(customer, PRIORITY, eventTime, serverName);
        this.waitAtServerName = waitAtServerName;
    }

    WaitEvent(Customer customer, double eventTime, String serverName,
              String waitAtServerName, boolean printable) {
        super(customer, PRIORITY, eventTime, serverName, printable);
        this.waitAtServerName = waitAtServerName;
    }

    @Override
    public Pair<Event, ServerManager> nextEvent(ServerManager serverManager) {
        AbstractServer tempServer = serverManager.getServer(this.serverName);
        // If there is a earlier time for selfcheckout, swap for that time.
        if (tempServer.getSelfCheckoutType()) {
            for (SelfCheckout sc : serverManager.getSelfCheckoutList()) {
                if (sc.getServiceEndTime() < tempServer.getServiceEndTime()) {
                    tempServer = sc;
                }
            }
        }
        if (this.customer.getArrivalTime() < tempServer.getServiceEndTime()) {
            Event waitEvent = new WaitEvent(
                    this.customer.resetCustomer(tempServer.getServiceEndTime()),
                    tempServer.getServiceEndTime(), this.serverName, this.waitAtServerName, false);
            return new Pair<Event, ServerManager>(waitEvent, serverManager);
        } else {
            Event serveEvent = new ServeEvent(this.customer,
                    tempServer.getServiceEndTime(), tempServer.getServerName());
            return new Pair<Event, ServerManager>(serveEvent, serverManager);
        }
    }

    @Override
    public ImList<Double> calculate(ImList<Double> statistics, ServerManager serverManager) {
        // cant use this.nextEvent cos it uses the old version of serverList
        // instead of the updated one.
        Pair<Event, ServerManager> tempPair = this.nextEvent(serverManager);
        // Heavily reliant on nextEvent
        Event nextEvent = tempPair.first();
        double timeSpentWaiting = nextEvent.getEventTime() - this.eventTime;
        double totalTimeWait = statistics.get(STATISTICS_INDEX) + timeSpentWaiting;
        return statistics.set(STATISTICS_INDEX, totalTimeWait);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s waits at %s", this.eventTime,
                this.customer.getCustomerNum(), this.waitAtServerName);
    }
}