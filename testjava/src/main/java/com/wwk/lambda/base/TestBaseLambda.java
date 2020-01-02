package com.wwk.lambda.base;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.security.auth.Subject;

import org.junit.Test;

/**
 * @Description: TODO
 * @author wangwk lambda与匿名内部类的区别
 *         1：java会为匿名内部类生成一个类文件，但lambda表达式不会生成类文件，lambda是函数式接口
 *
 *         函数式接口定义： 也是接口，但此接口只能有一个抽象方法，例如：Comparator接口
 *
 *         预定义的函数式接口包：java.util.funtion
 * @date:2019年12月30日 下午5:10:50
 */
public class TestBaseLambda {

	/**
	 * java.util.function.Function<T, R>
	 * 
	 * @Description: TODO
	 * @author wangwk
	 *
	 * @date:2019年12月30日 下午5:58:04
	 */
	@Test
	public void testFunction() {
		List<Student> students = Arrays
				.asList(new Student[] { new Student("1", 56D), new Student("2", 56D), new Student("3", 56D) });

		// 获取所有的学生名称集合
		List<String> names = convert(students, t -> t.getName());
		
		List<String> names2 = convert(students, new Function<Student, String>() {
			@Override
			public String apply(Student t) {
				return t.getName();
			}
		});
		List<String> names3 = convert(students, Student :: getName);

		//获取一个新的学生集合，学生名称转大写
		List<Student> students2 = convert(students, t -> new Student(t.getName().toUpperCase(), t.getScore()));
		
		List<Student> students3 = convert(students, new Function<Student, Student>() {
			@Override
			public Student apply(Student t) {
				return new Student(t.getName().toUpperCase(), t.getScore());
			}
		});
		
//		convert(students, Student::new Student(Student::getName, Student::getScore));
		
		convertConsumer(students, t -> t.setName(t.getName().toUpperCase()));
		
		convertConsumer(students, new Consumer<Student>() {

			@Override
			public void accept(Student t) {
				t.setName(t.getName().toUpperCase());
			}
		});
		
	}
	
	
	public static <T> void convertConsumer(List<T> list, Consumer<T> consumer){
		for (T t : list) {
			consumer.accept(t);
		}
	}
	

	/**
	 * 新建对象进行修改
	 * @Description: 根据原有集合转为新的集合
	 * @author wangwk
	 * @param list
	 * @param function
	 * @return
	 *
	 * @date:2019年12月30日 下午6:48:24
	 */
	public static <T, R> List<R> convert(List<T> list, Function<T, R> function) {
		List<R> resultList = new ArrayList<>();
		for (T t : list) {
			resultList.add(function.apply(t));
		}
		return resultList;
	}

	/**
	 * java.util.function.Predicate<T>
	 * 
	 * @Description: TODO
	 * @author wangwk
	 *
	 * @date:2019年12月30日 下午6:01:00
	 */
	@Test
	public void testPredicate() {
		List<Student> students = Arrays
				.asList(new Student[] { new Student("1", 56D), new Student("2", 56D), new Student("3", 56D) });
		// 过滤分数大于90分的
		filter(students, t -> t.getScore() > 90);
		
		filter(students, new Predicate<Student>() {
			@Override
			public boolean test(Student t) {
				if(t.getScore() > 90){
					return true;
				}
				return false;
			}
			
		});
	}

	/**
	 * 
	 * @Description: 过滤集合中的符合表达式的结果集
	 * @author wangwk
	 * @param list
	 * @param pred
	 * @return 结果集
	 *
	 * @date:2019年12月30日 下午6:37:51
	 */
	public static <E> List<E> filter(List<E> list, Predicate<E> pred) {
		List<E> resultList = new ArrayList<>();
		for (E e : list) {
			if (pred.test(e)) {
				resultList.add(e);
			}
		}
		return resultList;
	}

	@Test
	public void testLambdaInterface() {
		File file = new File("F:/javaProject5/testJAVA/testjava");
		File[] listFiles = file.listFiles((File dir, String name) -> {
			if (name.endsWith(".yml")) {
				return true;
			}
			return false;
		});

		File[] listFiles2 = file.listFiles((dir, name) -> ".java".endsWith(name));

		Arrays.sort(listFiles, (File file1, File file2) -> {
			return file1.getName().compareTo(file2.getName());
		});
		Arrays.sort(listFiles2, (file1, file2) -> file1.getName().compareTo(file2.getName()));
		
		Arrays.sort(listFiles2, Comparator.comparing(File::getName));

		/// Lambda只能访问final 修饰的 局部变量，修饰符可不写，但是变量事实上不能被重新赋值
		String msg = "hello world";
		// msg = "good morning";
		Executors.newFixedThreadPool(100).submit(() -> System.out.println(msg));

	}

	@Test
	public void testInterface() {
		File file = new File("F:/javaProject5/testJAVA/testjava");
		File[] listFiles = file.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".yml")) {
					return true;
				}
				return false;
			}
		});

		Arrays.sort(listFiles, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});

		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(100);
		newFixedThreadPool.submit(new Runnable() {
			@Override
			public void run() {
				System.out.println("hello world");
			}
		});
	}
}
