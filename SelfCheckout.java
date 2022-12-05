class SelfCheckout extends AbstractServer {
    private final String defaultName;

    SelfCheckout(String serverName, String defaultName) {
        super(serverName, 0, Integer.MAX_VALUE);
        this.defaultName = defaultName;
    }

    SelfCheckout(String serverName, String defaultName, double serviceEndTime, int queueSize) {
        super(serverName, serviceEndTime, queueSize);
        this.defaultName = defaultName;
    }

    @Override
    public SelfCheckout serveCustomer(double startTime) {
        return new SelfCheckout(this.serverName, this.defaultName, startTime, this.queueSize);
    }

    @Override
    public SelfCheckout queue(Customer customer) {
        return new SelfCheckout(this.serverName, this.defaultName, this.serviceEndTime,
                this.queueSize + 1);
    }

    @Override
    public SelfCheckout dequeue() {
        return new SelfCheckout(this.serverName, this.defaultName, this.serviceEndTime,
                this.queueSize - 1);
    }

    @Override
    public SelfCheckout serverRest() {
        return this;
    }

    @Override
    public String getServerWaitName() {
        return this.defaultName;
    }

    @Override
    public boolean getSelfCheckoutType() {
        return true;
    }
}
