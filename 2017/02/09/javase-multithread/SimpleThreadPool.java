package com.sunday.multithread_demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.PrivateKeyResolver;

public class SimpleThreadPool {
	
	private final static int nThreads = 5;
	private final static int corePoolSize = 5;
	
	//首次执行延迟
	private final static long initialDelay = 2;
	
	//连续执行之间的周期
	private final static long period = 5;
	
	//一次执行终止和下一次执行开始之间的延迟
	private final static long delay = 5;
	
	//处理工作
	private static void doWrok(ExecutorService executor){
		 for (int i = 0; i < 10; i++) {
	            Runnable worker = new WorkerThread(i);
	            executor.execute(worker);
	          }
	        executor.shutdown();
	        //关闭后所有任务都已完成，则返回 true
	        while (!executor.isTerminated()) {
	            try {
	                Thread.sleep(1000);
	                System.out.println("wait...");
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	        System.out.println("Finished all threads");
	}
	
	//固定线程数的线程池
	private static void fixed(){
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        doWrok(executor);
	}
	
	
	//根据需要创建新线程的可缓存线程池
	private static void cached(){
		//根据需要创建新线程的线程池
    	ExecutorService executor = Executors.newCachedThreadPool();
    	doWrok(executor);
	}
	
	
	//单个 worker 线程的 Executor
	private static void single(){
		ExecutorService executor = Executors.newSingleThreadExecutor();
		doWrok(executor);
	}
	
	private static void scheduled(){		
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(corePoolSize);
		
		//创建并执行在给定延迟后启用的一次性操作
		//scheduler.schedule(new WorkerThread(), initialDelay, TimeUnit.SECONDS);
		//scheduler.shutdown();
		
		//固定速率
		scheduler.scheduleAtFixedRate(new WorkerThread(), initialDelay, period, TimeUnit.SECONDS);
		
		//固定延迟
		scheduler.scheduleWithFixedDelay(new WorkerThread(), initialDelay, delay, TimeUnit.SECONDS);
	}
	
	
    public static void main(String[] args) {
//    	fixed();
    	
//    	cached();
    	
    	
    	single();
    	
//    	scheduled();
    }
}

class WorkerThread implements Runnable {
	  
    private int command;
    
    public WorkerThread(){
        this.command = 0;
    }
    public WorkerThread(int s){
        this.command = s;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName()+" Start. Command = "+(command++));
        processCommand();
        System.out.println(Thread.currentThread().getName()+" End.");
    }

    private void processCommand() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return this.command + "";
    }
}