package com.wwk.lambda.stream;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import org.junit.Test;

/**
 * @Description: TODO
 * @author wangwk
 *
 * @date:2019年12月31日 下午12:02:37
 */
public class TestFuture {
	private static Random rnd = new Random();

	static int delayRandom(int min, int max) {
		int milli = max > min ? rnd.nextInt(max - min) : 0;
		try {
			Thread.sleep(min + milli);
		} catch (InterruptedException e) {
		}
		return milli;
	}

	static Callable<Integer> callable = new Callable<Integer>() {

		@Override
		public Integer call() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	static Callable<Integer> externalTask = () -> {
		int time = delayRandom(20, 2000);
		return time;
	};
	
	private static ExecutorService executor = Executors.newFixedThreadPool(10);
	
	public static  Future<Integer> callExecutorService(){
		return executor.submit(externalTask);
	}
	
	
	static Supplier<Integer> externalTask2 = () ->{
		int time = delayRandom(20, 2000);
		return time;
	};
	
	public static  Future<Integer> callExecutorService2(){
		return CompletableFuture.supplyAsync(externalTask2, executor);
	}
	
	@Test
	public void testRun(){
		Future<Integer> asynchRet = callExecutorService();
		try {
			asynchRet.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
