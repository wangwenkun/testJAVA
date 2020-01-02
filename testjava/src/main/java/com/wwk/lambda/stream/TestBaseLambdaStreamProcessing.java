package com.wwk.lambda.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import com.wwk.lambda.base.Student;

/**
 * @Description: TODO
 * @author wangwk 组合利用基本函数，声明式实现集合数据处理功能的编程风格，就是函数式数据处理
 *         调用filter和map都不会执行任何实际的操作，它们只是在构建操作的流水线，调用collect才会触发实际的遍历执行
 * @date:2019年12月31日 上午10:30:39
 */
public class TestBaseLambdaStreamProcessing {
	List<Student> students = Arrays
			.asList(new Student[] { new Student("1", 56D), new Student("2", 56D), new Student("3", 56D) });

	@Test
	public void testFlatMap() {
		List<String> lines = Arrays.asList(new String[] { "hello abc", "函数式 数据 处理" });
		List<String> collect = lines.stream().flatMap(line -> Arrays.stream(line.split("\\s+")))
				.collect(Collectors.toList());
	}

	@Test
	public void testMapToLong() {
		double sum = students.stream().mapToDouble(Student::getScore).sum();

	}

	@Test
	public void testPeek() {
		List<String> collect = students.stream().filter(t -> t.getScore() > 90)
				.peek(t -> System.out.println("Filtered value: " + t)).map(Student::getName)
				.collect(Collectors.toList());
	}

	@Test
	public void testSkipAndLimit() {
		List<Student> collect = students.stream()
				.sorted(Comparator.comparing(Student::getScore).reversed().thenComparing(Student::getName)).skip(2)
				.limit(3).collect(Collectors.toList());
	}

	@Test
	public void testSorted() {
		List<Student> collect = students.stream().filter(t -> t.getScore() > 90)
				.sorted(Comparator.comparing(Student::getScore).reversed().thenComparing(Student::getName))
				.collect(Collectors.toList());
	}

	@Test
	public void testDistinct() {
		List<String> list = Arrays.asList(new String[] { "abc", "def", "hello", "Abc" });
		List<String> retList = list.stream().filter(s -> s.length() <= 3).map(String::toLowerCase).distinct()
				.collect(Collectors.toList());
	}

	/**
	 * @Description: TODO
	 * @author wangwk
	 *
	 * @date:2019年12月31日 上午10:51:09
	 */
	@Test
	public void testFilterAndConvert() {
		// 传统遍历添加集合方式
		List<String> above90Names = new ArrayList<String>();
		for (Student student : students) {
			if (student.getScore() > 90) {
				above90Names.add(student.getName());
			}
		}

		// 函数式数据处理
		List<String> collect = students.stream().filter(t -> t.getScore() > 90).map(Student::getName)
				.collect(Collectors.toList());
	}

	@Test
	public void testConvert() {
		List<Student> students = Arrays
				.asList(new Student[] { new Student("1", 56D), new Student("2", 56D), new Student("3", 56D) });

		// 传统遍历添加集合方式
		List<String> nameList = new ArrayList<String>();
		for (Student student : students) {
			nameList.add(student.getName());
		}

		// 函数式数据处理
		List<String> nameList2 = students.stream().map(Student::getName).collect(Collectors.toList());
	}

	@Test
	public void testFilter() {
		List<Student> students = Arrays
				.asList(new Student[] { new Student("1", 56D), new Student("2", 56D), new Student("3", 56D) });

		// 传统遍历添加集合方式
		List<Student> above90List = new ArrayList<Student>();
		for (Student student : students) {
			if (student.getScore() > 90) {
				above90List.add(student);
			}
		}

		// 使用函数式数据处理接口
		Stream<Student> stream = students.stream();
		stream.filter(t -> t.getScore() > 90);
		List<Student> above90List2 = stream.collect(Collectors.toList());

		students.stream().filter(t -> t.getScore() > 90).collect(Collectors.toList());
	}

}
