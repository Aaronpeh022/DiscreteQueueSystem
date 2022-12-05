import java.util.function.Supplier;

class Server extends AbstractServer {
    private final Supplier<Double> restTime;
    
    Server(String serverName, int queueSize, Supplier<Double> restTime) {
        super(serverName, 0, queueSize);
        this.restTime = restTime;
    }

    Server(String serverName, double serviceEndTime,
           int queueSize, Supplier<Double> restTime) {
        super(serverName, serviceEndTime, queueSize);
        this.restTime = restTime;
    }

    // Requires changes just plus 1 the queueSize
    @Override
    public Server queue(Customer customer) {
        return new Server(this.serverName,
                this.serviceEndTime, this.queueSize + 1, this.restTime);
    }

    // Requires changes just minus 1 the queueSize
    @Override
    public Server dequeue() {
        return new Server(this.serverName,
                this.serviceEndTime, this.queueSize - 1, this.restTime);
    }

    public double getRestTime() {
        return this.restTime.get();
    }

    // Start time could be arrivalTime/serviceEndTime
    @Override
    public Server serveCustomer(double startTime) {
        return new Server(this.serverName, startTime, this.queueSize, this.restTime);
    }

    // Dunnid change
    @Override
    public Server serverRest() {
        return new Server(this.serverName, this.serviceEndTime +
                this.getRestTime(), this.getQueueSize(), this.restTime);
    }
    
    public int getQueueSize() {
        return this.queueSize;
    }

    @Override
    public boolean getSelfCheckoutType() {
        return false;
    }

}
