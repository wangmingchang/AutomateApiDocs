package com.wmc.AutomateApiDocs.pojo.apidocs;

import java.util.List;

/**
 * 返回数据类信息
 * 
 * @author 王明昌
 * @date 2017年9月12日
 */
public class ResponseClassDto {

	private String className; // 类名
	private List<ResponseDataDto> responseDataDtos; // 返回数据信息
	private int grade; // 类的级别
	private String fieldType; // 字段类型

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<ResponseDataDto> getResponseDataDtos() {
		return responseDataDtos;
	}

	public void setResponseDataDtos(List<ResponseDataDto> responseDataDtos) {
		this.responseDataDtos = responseDataDtos;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	@Override
	public String toString() {
		return "ResponseClassDto [className=" + className + ", responseDataDtos=" + responseDataDtos + ", grade="
				+ grade + ", fieldType=" + fieldType + "]";
	}

}
