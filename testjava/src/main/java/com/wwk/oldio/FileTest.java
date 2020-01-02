package com.wwk.oldio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @Description: TODO
 * @author wangwk
 *
 * @date:2020年1月2日 下午2:15:16
 */
public class FileTest {
	private long begin;
	String srcPath;
	String dstPath;

	@Before
	public void init() {
		srcPath = "F:/delete/9/test.zip";
		dstPath = "F:/delete/9/test3.zip";
		begin = System.currentTimeMillis();
	}

	
	/**
	 * 使用时间 ：724    一次性读取
	 * 使用时间 ：4582刷新一次性读取
	 * 使用时间 ：3672  transferFrom 一次性读取
	 * 使用时间 ：14667   分批次读取
	 * 使用时间 ：11786   分批次读取
	 * @Description: 使用FileChannel的transferTo方法复制文件
	 */
	@Test
	public void testTransferTo(){
		 FileInputStream fis = null;
	        FileOutputStream fos = null;
	        FileChannel fisChannel = null;
	        FileChannel fosChannel = null;
	        try {
	            fis = new FileInputStream(srcPath);
	            fos = new FileOutputStream(dstPath);
	            fisChannel = fis.getChannel();
	            fosChannel = fos.getChannel();
//	            long len = fisChannel.transferTo(0, fisChannel.size(), fosChannel);
	            long len = fosChannel.transferFrom(fisChannel,0, fisChannel.size());
	            
//	            long size = fisChannel.size();
//                long pos = 0;
//                long count = 0;
//                while (pos < size) {
//                    //每次复制最多1024个字节，没有就复制剩余的
//                    count = size - pos > 1024 ? 1024 : size - pos;
//                    //复制内存,偏移量pos + count长度
//                    pos += fisChannel.transferTo(pos, count, fosChannel);
////                    pos += fosChannel.transferFrom(fisChannel, pos, count);
//                }
//                //强制刷新磁盘
                fosChannel.force(true);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (fisChannel != null) {
	                try {
	                    fisChannel.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (fis != null) {
	                try {
	                    fis.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (fosChannel != null) {
	                try {
	                    fosChannel.close();
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
	
	/**
	 * 使用时间 ：2328
	 * @Description: 使用nio复制文件
	 * 文件大小275MB
	 * 缓存大小1024
	 */
	@Test
	public void test() {
		FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel fisChannel = null;
        FileChannel fosChannel = null;
        //nio新建的ByteBuffer默认是写入模式的，需要调用flip翻转为读取模式
        ByteBuffer buffer = ByteBuffer.allocateDirect(1000);
        try {
            fis = new FileInputStream(srcPath);
            fos = new FileOutputStream(dstPath);
            fisChannel = fis.getChannel();
            fosChannel = fos.getChannel();
            //从fisChannel   写入    到  buffer
            while (fisChannel.read(buffer) != -1) {
            	//翻转模式从写入变为读取
                buffer.flip();
                //从buffer  读取到    fosChannel
                fosChannel.write(buffer);
                //清空buffer设置的模式，回到写入模式
                buffer.clear();
            }
//            fisChannel.force(true);
//            fosChannel.force(true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fisChannel != null) {
                try {
                    fisChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fosChannel != null) {
                try {
                    fosChannel.close();
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
