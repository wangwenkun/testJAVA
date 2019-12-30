/**
 * @Description: TODO
 * @author wangwk
 *
 * @date:2019年12月9日 下午5:49:07
 */
package com.wwk.threadtest.problem;

/**
 * @Description: TODO
 * @author wangwk
 *	线程竞态条件示例
 *	预期 1000000
 *	实际 994356 988145
 *	解决方式
 *		使用synchronized关键字
 *		使用显示锁
 *		使用原子变量
 * @date:2019年12月9日 下午5:49:07
 */
public class CounterThread extends Thread {
	private static int counter = 0;

	@Override
	public void run() {
		for (int i = 0; i < 1000; i++) {
			counter++;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		int num = 1000;
		Thread[] threads = new Thread[num];
		for (int i = 0; i < num; i++) {
			threads[i] = new CounterThread();
			threads[i].start();
		}
		for (int i = 0; i < num; i++) {
			threads[i].join();
		}
		System.out.println(counter);
	}
}
