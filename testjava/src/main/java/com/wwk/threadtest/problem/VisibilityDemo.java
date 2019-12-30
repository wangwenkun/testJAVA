/**
 * @Description: TODO
 * @author wangwk
 *
 * @date:2019年12月9日 下午5:54:37
 */
package com.wwk.threadtest.problem;

/**
 * @Description: TODO
 * @author wangwk
 *	内存可见性示例
 *	期望	两个线程都退出
 *	结果 主线程退出后，子线程没有退出
 *	解决方式
 *		使用volatile关键字
 *		使用synchronized关键字
 *		使用显示锁
 * @date:2019年12月9日 下午5:54:37
 */
public class VisibilityDemo {
	private static boolean shutdown = false;

	static class HelloThread extends Thread {
		@Override
		public void run() {
			while (!shutdown) {
				// do nothing
			}
			System.out.println("exit hello");
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new HelloThread().start();
		Thread.sleep(1000);
		shutdown = true;
		System.out.println("exit main");
	}

}
