package com.wmc.AutomateApiDocs.pojo.dto;

import java.util.List;

/**
 * 多行注释返回类
 * 
 * @author 王明昌
 * @date 2017年9月9日
 */

public class ClassMoreRemarkDto {

	private List<MethodExplainDto> methodExplainDtos; //方法的注释说明DTO
	private ClassExplainDto classExplainDto; //方法的注释说明DTO

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
