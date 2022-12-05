class ArriveEvent extends Event {
    private static final int PRIORITY = 1;
    private final int qmax;

    ArriveEvent(Customer customer, double eventTime, int qmax) {
        //super(customer, server, ARRIVE_PRIORITY, customer.getArrivalTime());
        super(customer, PRIORITY, customer.getArrivalTime(), "");
        this.qmax = qmax;
    }

    @Override
    public Pair<Event, ServerManager> nextEvent(ServerManager serverManager) {
        AbstractServer server = serverManager.getServer(this.customer.getArrivalTime(), this.qmax);
        String serverName = server.getServerName();
        // Serve by human server
        if (server.isIdle(this.customer.getArrivalTime())) {
            Event serve = new ServeEvent(this.customer,
                    Math.max(this.customer.getArrivalTime(), server.getServiceEndTime()),
                    serverName);
            return new Pair<Event, ServerManager>(serve, serverManager);
        }
        // Queue for human server
        if (server.getQueueSize() < this.qmax) {
            Event wait = new WaitEvent(this.customer, this.customer.getArrivalTime(),
                    serverName, server.getServerWaitName());
            serverManager = serverManager.updateServer(server.queue(this.customer));
            return new Pair<Event, ServerManager>(wait, serverManager);
        }
        // Queue for selfCheckout
        if (serverManager.getNumOfSelfCheckout() != 0) {
            if (serverManager.getSelfCheckoutQueueSize() < this.qmax) {
                Event wait = new WaitEvent(this.customer, this.customer.getArrivalTime(),
                        serverName, server.getServerWaitName());
                serverManager = serverManager.selfCheckoutQueue(this.customer);
                return new Pair<Event, ServerManager>(wait, serverManager);
            }
        }
        // Leave if no space in queue and no avail server
        Event leave = new LeaveEvent(customer, this.customer.getArrivalTime(), "");
        return new Pair<Event, ServerManager>(leave, serverManager);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s arrives", 
        this.eventTime, this.customer.getCustomerNum());
    }
}
