package com.github.wangmingchang.automateapidocs.pojo.apidocs;

import java.util.List;

/**
 * 生成word文档api输出参数
 * 
 * @author 王明昌
 * @since 2017年10月9日
 */
public class WordContentDto {
	private ClassExplainDto classExplainDto; // 类的说明
	private List<MethodInfoDto> methodInfoDtos;// 方法信息

	public ClassExplainDto getClassExplainDto() {
		return classExplainDto;
	}

	public void setClassExplainDto(ClassExplainDto classExplainDto) {
		this.classExplainDto = classExplainDto;
	}

	public List<MethodInfoDto> getMethodInfoDtos() {
		return methodInfoDtos;
	}

	public void setMethodInfoDtos(List<MethodInfoDto> methodInfoDtos) {
		this.methodInfoDtos = methodInfoDtos;
	}

	@Override
	public String toString() {
		return "WordContentDto [classExplainDto=" + classExplainDto + ", methodInfoDtos=" + methodInfoDtos + "]";
	}

}
