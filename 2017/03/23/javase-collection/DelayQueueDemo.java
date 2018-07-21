package com.sunday.jdk6_learn;

import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

class DelayedTask implements Runnable, Delayed {
	//延迟时间单位
	private final static TimeUnit delayUnit = TimeUnit.MILLISECONDS;
	
    private final int delayTime;
    private final long triggerTime;
    public DelayedTask(int delayInMillis) {
        delayTime = delayInMillis;
        triggerTime = System.nanoTime() + TimeUnit.NANOSECONDS.convert(delayTime, delayUnit);
    }
    
    @Override
    public int compareTo(Delayed o) {
        DelayedTask that = (DelayedTask)o;
        if (triggerTime < that.triggerTime) return -1;
        if (triggerTime > that.triggerTime) return 1;
        return 0;
    }

    /**
     * 剩余的延迟时间
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(triggerTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public void run() {
        System.out.println(this);
    }

	@Override
	public String toString() {
		return "DelayedTask [delayTime=" + delayTime + "]";
	}
}

public class DelayQueueDemo {
    public static void main(String[] args) {
    	//延迟任务队列
        final DelayQueue<DelayedTask> queue = new DelayQueue<DelayedTask>();
        
        int maxDelayTime = 5000;//milliseconds
        Random random = new Random();
        for(int i = 0; i < 10;i++){
        	 DelayedTask delayedTask = new DelayedTask(random.nextInt(maxDelayTime));
             queue.put(delayedTask);
        }
        
        Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (queue.size() > 0) {
					try {
						DelayedTask take = queue.take();
						take.run();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
        
        thread.start();
        
        try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}