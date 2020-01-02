package com.wwk.lambda.base;

/**
 * @Description: TODO
 * @author wangwk
 *
 * @date:2019年12月30日 下午5:56:12
 */
public class Student {
	public Student(String name, double score) {
		this.name = name;
		this.score = score;
	}

	String name;
	double score;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}
	
	public static String getCollegeName(){
		return "laoma school";
	}
	@Override
	public String toString() {
		return "Student [name=" + name + ", score=" + score + "]";
	}
	
}
