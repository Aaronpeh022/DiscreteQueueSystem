abstract class AbstractServer {
    protected final String serverName;
    protected final double serviceEndTime;
    protected final int queueSize;

    AbstractServer(String serverName, double serviceEndTime, int queueSize) {
        this.serverName = serverName;
        this.serviceEndTime = serviceEndTime;
        this.queueSize = queueSize;
    }

    protected double getServiceEndTime() {
        return this.serviceEndTime;
    }

    protected String getServerName() {
        return this.serverName;
    }

    protected String getServerWaitName() {
        return this.serverName;
    }

    protected AbstractServer serveCustomer(double serviceTime) {
        return this;
    }

    public boolean isIdle(double arrivalTime) {
        return arrivalTime >= this.getServiceEndTime();
    }

    public int getQueueSize() {
        return this.queueSize;
    }

    abstract AbstractServer queue(Customer customer);

    abstract AbstractServer dequeue();

    abstract AbstractServer serverRest();

    abstract boolean getSelfCheckoutType();
}
