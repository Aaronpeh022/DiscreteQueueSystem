class DoneEvent extends Event {
    private static final int PRIORITY = 4;
    
    DoneEvent(Customer customer, double eventTime, String serverName) {
        super(customer, PRIORITY, eventTime, serverName);
    }

    //placeholder
    @Override
    public Pair<Event, ServerManager> nextEvent(ServerManager serverManager) {
        AbstractServer tempServer = serverManager.getServer(this.serverName);
        tempServer = tempServer.serverRest();
        ServerManager returnManager = serverManager.updateServer(tempServer);
        if (tempServer.getSelfCheckoutType() & serverManager.getSelfCheckoutQueueSize() > 0) {
            returnManager = returnManager.selfCheckoutDequeue();
        }
        return new Pair<Event, ServerManager>(this, returnManager);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s done serving by %s",
                this.eventTime, this.customer.getCustomerNum(),
        super.serverName);
    }
}
