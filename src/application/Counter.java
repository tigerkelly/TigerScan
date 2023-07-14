package application;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
	private final AtomicInteger counter = new AtomicInteger(0);
    
    int getValue() {
        return counter.get();
    }
    
    void increment() {
        counter.incrementAndGet();
    }
    
    void set(int value) {
    	counter.set(value);
    }
}
