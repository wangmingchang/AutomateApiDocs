package com.github.wangmingchang.automateapidocs.pojo.apidocs.vo;
/**
 * 老师类
 * @author 王明昌
 * @since 2017年9月9日
 */
public class Teachar {
	private String teacharName;	//姓名
	private String subject; //学科
	public String getTeacharName() {
		return teacharName;
	}
	public void setTeacharName(String teacharName) {
		this.teacharName = teacharName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		return "Teachar [teacharName=" + teacharName + ", subject=" + subject + "]";
	}
}
