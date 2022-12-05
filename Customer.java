import java.util.function.Supplier;

class Customer {
    private final int customerNum;
    private final double arrivalTime;
    private final Supplier<Double> serviceTime;

    Customer(int customerNum, double arrivalTime, Supplier<Double> serviceTime) {
        this.customerNum = customerNum;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public double getServiceTime() {
        return this.serviceTime.get();
    }

    public double getArrivalTime() {
        return this.arrivalTime;
    }

    public int getCustomerNum() {
        return this.customerNum;
    }

    public Customer resetCustomer(double serviceEndTime) {
        return new Customer(this.customerNum, serviceEndTime, this.serviceTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            if (obj instanceof Customer c) {
                return this.customerNum == c.getCustomerNum();
            } else {
                return false;
            }
        }
    }

}
