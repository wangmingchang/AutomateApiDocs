package com.github.wangmingchang.automateapidocs.pojo.apidocs;

import java.util.List;

/**
 * 多行注释返回类
 * 
 * @author 王明昌
 * @since 2017年9月9日
 */

public class ClassMoreRemarkDto {

	private List<MethodExplainDto> methodExplainDtos; //方法的注释说明DTO
	private ClassExplainDto classExplainDto; //类的注释说明DTO

	public List<MethodExplainDto> getMethodExplainDtos() {
		return methodExplainDtos;
	}

	public void setMethodExplainDtos(List<MethodExplainDto> methodExplainDtos) {
		this.methodExplainDtos = methodExplainDtos;
	}

	public ClassExplainDto getClassExplainDto() {
		return classExplainDto;
	}

	public void setClassExplainDto(ClassExplainDto classExplainDto) {
		this.classExplainDto = classExplainDto;
	}

	@Override
	public String toString() {
		return "ClassMoreRemarkDto [methodExplainDtos=" + methodExplainDtos + ", classExplainDto=" + classExplainDto
				+ "]";
	}

}
