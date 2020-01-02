package com.wwk.oldio;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class FileChannelDemo {

    private static void createFile(String path, long numKB) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(path);
            bw = new BufferedWriter(fw);
            for (long i = 0; i < numKB * 16 * 4; i++) {
                for (int j = 0; j < 16; j++) {
                    String hex = Integer.toHexString(j);
                    bw.write(hex);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void copyByIO(String srcPath, String dstPath) {
        byte[] buffer = new byte[bufferSize];
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

    private static void copyByNIO(String srcPath, String dstPath) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel fisChannel = null;
        FileChannel fosChannel = null;
        ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
        try {
            fis = new FileInputStream(srcPath);
            fos = new FileOutputStream(dstPath);
            fisChannel = fis.getChannel();
            fosChannel = fos.getChannel();
            while (fisChannel.read(buffer) != -1) {
                buffer.flip();
                fosChannel.write(buffer);
                buffer.clear();
            }
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

    private static void copyByNIOTransfer(String srcPath, String dstPath) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel fisChannel = null;
        FileChannel fosChannel = null;
        try {
            fis = new FileInputStream(srcPath);
            fos = new FileOutputStream(dstPath);
            fisChannel = fis.getChannel();
            fosChannel = fos.getChannel();
            long len = fisChannel.transferTo(0, fisChannel.size(), fosChannel);
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

    private static void copyByFiles(String srcPath, String dstPath) {
        Path path = Paths.get(srcPath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(dstPath);
            /*
             * private static final int BUFFER_SIZE = 8192;
             * private static long copy(InputStream source, OutputStream sink) throws IOException
             * {
             * long nread = 0L;
             * byte[] buf = new byte[BUFFER_SIZE];
             * int n;
             * while ((n = source.read(buf)) > 0) {
             * sink.write(buf, 0, n);
             * nread += n;
             * }
             * return nread;
             * }
             */
            long len = Files.copy(path, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void multiThreadWriteIO(String path) {
        FileOutputStream fos = null;
        int num = 1000;
        final CountDownLatch countDownLatch = new CountDownLatch(num);
        try {
            fos = new FileOutputStream(path);
            final FileOutputStream thisFos = fos;
            Thread[] threads = new Thread[num];
            for (int i = 0; i < num; i++) {
                final int index = i;
                threads[i] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] buffer = (index + ". " + (new Date()).toString() + " hello world!xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx welcome to Newland\r\n").getBytes();
                        try {
                            thisFos.write(buffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        countDownLatch.countDown();
                    }
                });
            }
            for (int i = 0; i < num; i++) {
                threads[i].start();
            }
            countDownLatch.await();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void multiThreadWriteNIO(String path) {
        FileOutputStream fos = null;
        FileChannel fosChannel = null;
        int num = 1000;
        final CountDownLatch countDownLatch = new CountDownLatch(num);
        try {
            fos = new FileOutputStream(path);
            fosChannel = fos.getChannel();
            final FileChannel thisFosChannel = fosChannel;
            for (int i = 0; i < num; i++) {
                final int index = i;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ByteBuffer buffer = ByteBuffer.wrap((index + ". " + (new Date()).toString() + " hello world!xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx welcome to Newland\r\n").getBytes());
                        try {
                            thisFosChannel.write(buffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        countDownLatch.countDown();
                    }
                }).start();
            }
            countDownLatch.await();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
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

    private static void multiThreadOpenWriteIO(final String path) {
        int num = 1000;
        final CountDownLatch countDownLatch = new CountDownLatch(num);
        final Object obj = new Object();
        for (int i = 0; i < num; i++) {
            final int index = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RandomAccessFile raf = null;
                    synchronized (obj) {
                        try {
                            raf = new RandomAccessFile(path, "rw");
                            raf.seek(raf.length());
                            byte[] buffer = (index + ". " + (new Date()).toString() + " hello world!xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx welcome to Newland\r\n").getBytes();
                            raf.write(buffer);
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (raf != null) {
                                try {
                                    raf.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    countDownLatch.countDown();
                }
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void multiThreadOpenWriteNIO(final String path, long sleepTimeMillis) {
        int num = 1000;
        final CountDownLatch countDownLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            final int index = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RandomAccessFile raf = null;
                    FileChannel rafChannel = null;
                    FileLock fl = null;
                    try {
                        raf = new RandomAccessFile(path, "rw");
                        rafChannel = raf.getChannel();
                        while (true) {
                            try {
                                fl = rafChannel.tryLock();
                                if (fl != null) {
                                    break;
                                }
                            } catch (OverlappingFileLockException e) {
                                try {
                                    Thread.sleep(sleepTimeMillis);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                        rafChannel.position(rafChannel.size());
                        ByteBuffer buffer = ByteBuffer.wrap((index + ". " + (new Date()).toString() + " hello world!xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx welcome to Newland\r\n").getBytes());
                        rafChannel.write(buffer);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fl != null) {
                            try {
                                fl.release();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (rafChannel != null) {
                            try {
                                rafChannel.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (raf != null) {
                            try {
                                raf.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    countDownLatch.countDown();
                }
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
  //（变量、函数定义，略……）
  	private static void multiThreadOpenWriteNIOEx(final String path) {
  	        int num = 1000;
  	        final CountDownLatch countDownLatch = new CountDownLatch(num);
  	        final Object object = new Object();
  	        for (int i = 0; i < num; i++) {
  	            final int index = i;
  	            new Thread(new Runnable() {
  	                @Override
  	                public void run() {
  	                    RandomAccessFile raf = null;
  	                    FileChannel rafChannel = null;
  	                    FileLock fl = null;
  	                    try {
  	                        raf = new RandomAccessFile(path, "rw");
  	                        rafChannel = raf.getChannel();
  	                        if( index != 0) {
  	                            synchronized (object) {
  	                                try {
  	                                    object.wait();
  	                                } catch (InterruptedException e) {
  	                                    e.printStackTrace();
  	                                }
  	                            }
  	                        }
  	                        while(true) {
  	                            try {
  	                                fl = rafChannel.tryLock();
  	                                if(fl != null) {
  	                                    break;
  	                                }
  	                            } catch (OverlappingFileLockException e) {
  	                                e.printStackTrace();
  	                                return;
  	                            }
  	                        }
  	                        rafChannel.position(rafChannel.size());
  	                        ByteBuffer buffer = ByteBuffer.wrap((index + ". " + (new Date()).toString() + " hello world!xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx welcome to Newland\r\n").getBytes());
  	                        rafChannel.write(buffer);
  	                    } catch (FileNotFoundException e1) {
  	                        e1.printStackTrace();
  	                    } catch (IOException e) {
  	                        e.printStackTrace();
  	                    } finally {
  	                        if (fl != null) {
  	                            try {
  	                                fl.release();
  	                            } catch (IOException e) {
  	                                e.printStackTrace();
  	                            }
  	                        }
  	                        if (rafChannel != null) {
  	                            try {
  	                                rafChannel.close();
  	                            } catch (IOException e) {
  	                                e.printStackTrace();
  	                            }
  	                        }
  	                        synchronized (object) {
  	                            object.notify();
  	                        }
  	                        if (raf != null) {
  	                            try {
  	                                raf.close();
  	                            } catch (IOException e) {
  	                                e.printStackTrace();
  	                            }
  	                        }
  	                    }
  	
  	                    countDownLatch.countDown();
  	                }
  	            }).start();
  	        }
  	        try {
  	            countDownLatch.await();
  	        } catch (InterruptedException e) {
  	            e.printStackTrace();
  	        }
  	    }

    private static final int[] NUM_KB_ARR = { 1, 10, 100, 1024, 10 * 1024, 100 * 1024 };
    private static final int[] BUFF_SIZE_ARR = { 256, 1024, 8192 };
    private static int bufferSize = 8192;
    private static long beginTime;

    private static void calcCostTime(String msg) {
        if (msg == null) {
            beginTime = System.nanoTime();
        } else {
            System.out.println(msg + "\tcost\t" + String.format("%011d", System.nanoTime() - beginTime) + "us");
        }
    }

    public static void main(String[] args) {
        String testPath = "test.txt";
        String ioOutputPath = "io.txt";
        String nioOutputPath = "nio.txt";
        String nioTransferOutputPath = "nioTransfer.txt";
        String nioFilesPath = "nioFiles.txt";
        String ioMultiThreadWritePath = "multiThreadIoWritePath.txt";
        String nioMultiThreadNioWritePath = "multiThreadNioWritePath.txt";
        String iomultiThreadIoOpenWritePath = "multiThreadIoOpenWritePath.txt";
        String nioMultiThreadNioOpenWritePath = "multiThreadNioOpenWritePath.txt";
        String nioMultiThreadNioOpenWriteExPath = "nioMultiThreadNioOpenWriteExPath.txt";

        Runtime runtime = Runtime.getRuntime();
        int availableProcessors = runtime.availableProcessors();
        System.out.println("availableProcessors = " + availableProcessors);
        System.out.println("vm's freeMemory = " + runtime.freeMemory() + ", vm's totalMemory = " + runtime.totalMemory());
        System.out.println();

        for (int i = 0; i < BUFF_SIZE_ARR.length; i++) {
            bufferSize = BUFF_SIZE_ARR[i];
            System.out.println("BUFF_SIZE = " + bufferSize);
            // 复制文件的情况
            for (int j = 0; j < NUM_KB_ARR.length; j++) {
                System.out.println(j + ". FILE_SIZE = " + NUM_KB_ARR[j] + " KB");
                String numKBStr = String.format("%09d", NUM_KB_ARR[j]);
                calcCostTime(null);
                createFile(testPath, NUM_KB_ARR[j]);
                calcCostTime("createFile\t\t" + numKBStr);
                calcCostTime(null);
                copyByIO(testPath, bufferSize+"_"+ioOutputPath);
                calcCostTime("copyByIO\t\t" + numKBStr);
                calcCostTime(null);
                copyByNIO(testPath, bufferSize+"_"+nioOutputPath);
                calcCostTime("copyByNIO\t\t" + numKBStr);
                calcCostTime(null);
                copyByNIOTransfer(testPath, bufferSize+"_"+nioTransferOutputPath);
                calcCostTime("copyByNIOTransfer\t" + numKBStr);
                calcCostTime(null);
                copyByFiles(testPath, bufferSize+"_"+nioFilesPath);
                calcCostTime("copyByFiles\t\t" + numKBStr);
            }
            System.out.println();
        }

        // 多线程共用同一个流追加数据的情况
        calcCostTime(null);
        multiThreadWriteIO(ioMultiThreadWritePath);
        calcCostTime("multiThreadWriteIO\t\t");
        calcCostTime(null);
        multiThreadWriteNIO(nioMultiThreadNioWritePath);
        calcCostTime("multiThreadWriteNIO\t\t");

        // 多线程使用不同的流追加数据的情况
        calcCostTime(null);
        multiThreadOpenWriteIO(iomultiThreadIoOpenWritePath);
        calcCostTime("multiThreadOpenWriteIO\t\t");
        calcCostTime(null);
        multiThreadOpenWriteNIO(nioMultiThreadNioOpenWritePath, 15);
        calcCostTime("multiThreadOpenWriteNIO\t\t");
        System.out.println();
	    calcCostTime(null);
	    multiThreadOpenWriteNIOEx(nioMultiThreadNioOpenWriteExPath);
	    calcCostTime("multiThreadOpenWriteNIOEx\t");
	    System.out.println();
        File file = new File(".");
        for (String path : file.list()) { 
            if (path.endsWith(".txt")) {
                try {
                    System.out.println("prepare delete "+path);
                    Files.delete(Paths.get(path));
                    System.out.println("delete succ");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("delete fail");
                }
            }
        }
    }
}
