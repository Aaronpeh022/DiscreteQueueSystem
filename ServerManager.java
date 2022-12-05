class ServerManager {
    private final ImList<Server> serverList;
    private final ImList<SelfCheckout> selfCheckoutList;
    private final int selfCheckoutQueueSize;

    ServerManager(ImList<Server> serverList, ImList<SelfCheckout> selfCheckoutList) {
        this(serverList, selfCheckoutList, 0);
    }

    ServerManager(ImList<Server> serverList, ImList<SelfCheckout> selfCheckoutList,
                  int selfCheckoutQueueSize) {
        this.serverList = serverList;
        this.selfCheckoutList = selfCheckoutList;
        this.selfCheckoutQueueSize = selfCheckoutQueueSize;
    }

    public AbstractServer getServer(double arrivalTime, int qMax) {
        // return human server if free
        for (Server s : this.serverList) {
            if (s.isIdle(arrivalTime)) {
                return s;
            }
        }
        // return selfcheckout if free
        for (SelfCheckout sc : this.selfCheckoutList) {
            if (sc.isIdle(arrivalTime)) {
                return sc;
            }
        }
        // waits at human server if has queue
        for (Server s : serverList) {
            if (s.getQueueSize() < qMax) {
                return s;
            }
        }
        // waits at self checkout counter
        // System.out.println(String.format("This is qmax: %s selfCheck size: %s",
        //         qMax, selfCheckoutList.size()));
        // System.out.println(selfCheckoutList.size() != 0 & this.selfCheckoutQueueSize < qMax);
        if (selfCheckoutList.size() != 0 && this.selfCheckoutQueueSize < qMax) {
            SelfCheckout returnSelfCheckout = selfCheckoutList.get(0);
            for (SelfCheckout sc : selfCheckoutList) {
                if (sc.getServiceEndTime() < returnSelfCheckout.getServiceEndTime()) {
                    returnSelfCheckout = sc;
                }
            }
            return returnSelfCheckout;
        }
        // Asking customer to leave
        return new Server("Shouldnt be this", Integer.MAX_VALUE,
                Integer.MAX_VALUE, () -> Double.MIN_VALUE);
    }

    public AbstractServer getServer(String serverName) {
        for (Server server : serverList) {
            if (server.getServerName().equals(serverName)) {
                return server;
            }
        }
        for (SelfCheckout selfCheckout: selfCheckoutList) {
            if (selfCheckout.getServerName().equals(serverName)) {
                return selfCheckout;
            }
        }
        // Shouldnt reach here
        return serverList.get(0);
    }

    public ServerManager updateServer(AbstractServer abstractServer) {
        for (int i = 0; i < this.serverList.size(); i++) {
            Server currentServer = serverList.get(i);
            if (currentServer.getServerName().equals(abstractServer.getServerName())) {
                ImList<Server> serverImList = serverList.set(i, (Server) abstractServer);
                return new ServerManager(serverImList, this.selfCheckoutList,
                        this.selfCheckoutQueueSize);
            }
        }
        for (int j = 0; j < this.selfCheckoutList.size(); j++) {
            SelfCheckout currentSelfCheckout = selfCheckoutList.get(j);
            if (currentSelfCheckout.getServerName().equals(abstractServer.getServerName())) {
                ImList<SelfCheckout> selfCheckoutImList = selfCheckoutList.set(j,
                        (SelfCheckout) abstractServer);
                return new ServerManager(this.serverList, selfCheckoutImList,
                        this.selfCheckoutQueueSize);
            }
        }
        // shouldnt reach here
        return this;
    }

    public ServerManager selfCheckoutQueue(Customer customer) {
        return new ServerManager(this.serverList, this.selfCheckoutList,
                this.selfCheckoutQueueSize + 1);
    }

    public ServerManager selfCheckoutDequeue() {
        return new ServerManager(this.serverList, this.selfCheckoutList,
                this.selfCheckoutQueueSize - 1);
    }

    public int getSelfCheckoutQueueSize() {
        return this.selfCheckoutQueueSize;
    }

    public int getNumOfSelfCheckout() {
        return this.selfCheckoutList.size();
    }

    public ImList<SelfCheckout> getSelfCheckoutList() {
        return this.selfCheckoutList;
    }
}
