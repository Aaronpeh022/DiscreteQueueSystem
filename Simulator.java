import java.util.List;
import java.util.function.Supplier;

class Simulator {
    private final int numOfServers;
    private final int qMax;
    private final ImList<Pair<Double, Supplier<Double>>> inputTimes;
    private final Supplier<Double> restTime;
    private final int numOfSelfChecks;

    Simulator(int numOfServers, int numOfSelfChecks, int qMax,
              ImList<Pair<Double, Supplier<Double>>> inputTimes, Supplier<Double> restTime) {
        this.numOfServers = numOfServers;
        this.qMax = qMax;
        this.inputTimes = inputTimes;
        this.restTime = restTime;
        this.numOfSelfChecks = numOfSelfChecks;
    }

    public String simulate() {
        int customerNum = 0;
        String returnString = "";
        Supplier<Double> placeholderService = () -> 0.0;
        Customer customer = new Customer(customerNum, 0, placeholderService);
        ImList<Server> serverList = new ImList<Server>();
        ImList<SelfCheckout> selfCheckoutList = new ImList<SelfCheckout>();
        PQ<Event> eventsQueue = new PQ<>(new EventComparator());
        ImList<Double> statistics = new ImList<>(List.of(0.0, 0.0, 0.0));
        for (int i = 0; i < numOfServers; i++) {
            String serverName = String.format("%s", (i + 1));
            // Fill serverList with the number of servers declared
            serverList = serverList.add(new Server(serverName, 0, this.restTime));
        }
        for (int j = numOfServers; j < numOfServers + numOfSelfChecks; j++) {
            String serverName = String.format("self-check %s", (j + 1));
            String defaultName = String.format("self-check %s",
                    (this.numOfServers + 1));
            // Fill selfCheckoutList with number of counters
            selfCheckoutList = selfCheckoutList.add(new SelfCheckout(serverName, defaultName));
        }
        ServerManager serverManager = new ServerManager(serverList, selfCheckoutList);
        for (Pair<Double, Supplier<Double>> pair : inputTimes) {
            double arrivalTime = pair.first();
            Supplier<Double> serviceTime = pair.second();
            customer = new Customer(customer.getCustomerNum() + 1, arrivalTime, serviceTime);
            // Starts to call service end time
            ArriveEvent arriveEvent = new ArriveEvent(customer,
                    customer.getArrivalTime(), this.qMax);
            eventsQueue = eventsQueue.add(arriveEvent);
        }
        while (!eventsQueue.isEmpty()) {
            Pair<Event, PQ<Event>> eventPairs = eventsQueue.poll();
            Event newEvent = eventPairs.first();
            eventsQueue = eventPairs.second();
            if (newEvent.getPrintable()) {
                returnString += newEvent + "\n";
            }
            Pair<Event, ServerManager> followingEventPair = newEvent.nextEvent(serverManager);
            Event followingEvent = followingEventPair.first();
            serverManager = followingEventPair.second();
            statistics = newEvent.calculate(statistics, serverManager);
            if (followingEvent != newEvent) {
                eventsQueue = eventsQueue.add(followingEvent);
            }
        }
        double avgTimeWait = statistics.get(0) / statistics.get(1);
        returnString += String.format("[%.3f %.0f %.0f]", avgTimeWait,
                statistics.get(1), statistics.get(2));
        return returnString;
    }
}