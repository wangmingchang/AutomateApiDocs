package com.wmc.AutomateApiDocs.pojo.vo;

import java.util.List;

import com.wmc.AutomateApiDocs.pojo.Student;

/**
 * 测试返回类
 * 
 * @author 王明昌
 * @date 2017年9月9日
 */
public class DemoVo {

	private String id; // 主键
	private String name; // 姓名
	private Double socre; // 分数
	private List<Student> students; // 学生类

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getSocre() {
		return socre;
	}

	public void setSocre(Double socre) {
		this.socre = socre;
	}

	@Override
	public String toString() {
		return "DemoVo [id=" + id + ", name=" + name + ", socre=" + socre + "]";
	}

}
