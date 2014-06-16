import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;

/**
 * @class SimpleSemaphore
 *
 * @brief This class provides a simple counting semaphore
 *        implementation using Java a ReentrantLock and a
 *        ConditionObject.  It must implement both "Fair" and
 *        "NonFair" semaphore semantics, just liked Java Semaphores. 
 */
public class SimpleSemaphore {
    /**
     * Define a ReentrantLock to protect the critical section.
     */
    // TODO - you fill in here
	ReentrantLock mLock; 
	
    /**
     * Define a ConditionObject to wait while the number of
     * permits is 0.
     */
    // TODO - you fill in here
	Condition countCond;
	
    /**
     * Define a count of the number of available permits.
     */
    // TODO - you fill in here.  Make sure that this data member will
    // ensure its values aren't cached by multiple Threads..
	volatile int permits, count;
	
    /**
     * Constructor initialize the data members.  
     */
    public SimpleSemaphore (int permits,
                            boolean fair)
    { 
        // TODO - you fill in here
    	count = permits;
    	this.permits = permits;
    	mLock = new ReentrantLock(fair);
    	countCond = mLock.newCondition();
    }

    /**
     * Acquire one permit from the semaphore in a manner that can
     * be interrupted.
     */
    public void acquire() throws InterruptedException {
        // TODO - you fill in here
    	mLock.lockInterruptibly();
    	try{
    		while(count==0){
    			countCond.await(); //wait on the condition if semaphore unavailable
    		}
    		count--; //reduce the count now that I have it.
    		countCond.signal(); // signal any thread waiting on the condition
    		
    	}finally{
    		mLock.unlock();
    	}
    }

    /**
     * Acquire one permit from the semaphore in a manner that
     * cannot be interrupted.
     */
    public void acquireUninterruptibly() {
        // TODO - you fill in here
    	mLock.lock();
    	try{
    		while(count==0){
    			countCond.awaitUninterruptibly(); //will continue to wait even if interrupted
    		}
    		count--; //reduce the count now that I have it.
    		countCond.signal(); // signal any thread waiting on the condition
    		
    	}finally{
    		mLock.unlock();
    	}
    }

    /**
     * Return one permit to the semaphore.
     */
    void release() {
        // TODO - you fill in here
    	mLock.lock();
    	try{
    		while(count == permits){
    			countCond.awaitUninterruptibly(); // continue to wait until a permit is taken
    			// the above should not happen if the semaphore is used correctly
    			// i.e. a permit is acquired before released
    		}
    		count++;
    		countCond.signal();
    	}finally{
    		mLock.unlock();
    	}
    }
    
    /**
     * Return the number of permits available.
     */
    public int availablePermits(){
    	// TODO - you fill in here
    	// a lock here is not necessary 
    	// but may not seem consistent to a reading thread
    	return count; 
    }
}

