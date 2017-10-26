package com.wmc.automateapidocs.pojo;

/**
 * 学生类
 * 
 * @author 王明昌
 * @since 2017年9月9日
 */
public class Student extends Teachar {
	/** 年龄1 */
	private int age; // 年龄
	private String name; // 姓名
	private double socre; // 分数

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSocre() {
		return socre;
	}

	public void setSocre(double socre) {
		this.socre = socre;
	}

	@Override
	public String toString() {
		return "Student [age=" + age + ", name=" + name + ", socre=" + socre + "]";
	}

}
