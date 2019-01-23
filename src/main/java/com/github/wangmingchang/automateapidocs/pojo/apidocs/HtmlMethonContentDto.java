package com.github.wangmingchang.automateapidocs.pojo.apidocs;

import java.util.List;
import java.util.Map;

/**
 * 生成html页面的api数据DTO
 * 
 * @author 王明昌
 * @since 2017年10月17日
 */
public class HtmlMethonContentDto {
	private ClassExplainDto classExplainDto; // 类的注释说明DTO
	private List<Map<String, String>> methodDescriptions; // 方法业务说明
	private List<MethodInfoDto> methodInfoDtos; // 方法信息

	public HtmlMethonContentDto() {
		super();
	}

	public HtmlMethonContentDto(ClassExplainDto classExplainDto, List<Map<String, String>> methodDescriptions,
			List<MethodInfoDto> methodInfoDtos) {
		super();
		this.classExplainDto = classExplainDto;
		this.methodDescriptions = methodDescriptions;
		this.methodInfoDtos = methodInfoDtos;
	}

	public ClassExplainDto getClassExplainDto() {
		return classExplainDto;
	}

	public void setClassExplainDto(ClassExplainDto classExplainDto) {
		this.classExplainDto = classExplainDto;
	}

	public List<Map<String, String>> getMethodDescriptions() {
		return methodDescriptions;
	}

	public void setMethodDescriptions(List<Map<String, String>> methodDescriptions) {
		this.methodDescriptions = methodDescriptions;
	}

	public List<MethodInfoDto> getMethodInfoDtos() {
		return methodInfoDtos;
	}

	public void setMethodInfoDtos(List<MethodInfoDto> methodInfoDtos) {
		this.methodInfoDtos = methodInfoDtos;
	}

	@Override
	public String toString() {
		return "HtmlMethonContentDto [classExplainDto=" + classExplainDto + ", methodDescriptions=" + methodDescriptions
				+ ", methodInfoDtos=" + methodInfoDtos + "]";
	}

}
