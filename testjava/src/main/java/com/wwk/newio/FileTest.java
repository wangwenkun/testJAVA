package com.wwk.newio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @Description: TODO
 * @author wangwk
 *
 * @date:2020年1月2日 下午1:47:11
 */
public class FileTest {

	private long begin;
	String srcPath;
	String dstPath;

	@Before
	public void init() {
		srcPath = "F:/delete/9/test.zip";
		dstPath = "F:/delete/9/test2.zip";
		begin = System.currentTimeMillis();
	}

	/**
	 * 使用时间 ：7414
	 * @Description: 使用阻塞io复制文件
	 * 文件大小275MB
	 * 缓存大小1024
	 */
	@Test
	public void test() {
		byte[] buffer = new byte[1024];
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(srcPath);
			fos = new FileOutputStream(dstPath);
			int len;
			while ((len = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@After
	public void destory() {
		long currentTimeMillis = System.currentTimeMillis();
		System.out.println("使用时间 ：" + (currentTimeMillis - begin));
	}

}
