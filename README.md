# DiscreteQueueSystem
This is the final milestone for the Discrete Event Simulator.

As with all modern supermarkets, there are now a number of self-checkout counters being set up. In particular, if there are k human servers, then the self-checkout counters are identified from k + 1 onwards.

Take note of the following:

All self-checkout counters share the same queue.
Unlike human servers, self-checkout counters do not rest.
When we print out the wait event, we say that the customer is waiting for the self-checkout counter k + 1, even though this customer may eventually be served by another self-checkout counter.
Use the program Main.java provided to test your program.

User input starts with values representing the number of servers, the number of self-check counters, the maximum queue length, the number of customers and the probability of a server resting. This is followed by the arrival times of the customers. Lastly, a number of service times (could be more than necessary) are provided.
