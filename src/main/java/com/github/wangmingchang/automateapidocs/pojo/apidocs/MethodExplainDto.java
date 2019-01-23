package com.github.wangmingchang.automateapidocs.pojo.apidocs;

import java.util.List;

/**
 * 方法的注释说明DTO
 * 
 * @author 王明昌
 * @since 2017年9月9日
 */
public class MethodExplainDto {

	private String methodName; //方法名称
	private String explain; // 方法业务说明
	private List<RequestParamDto> paramDtos; // 参数

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public List<RequestParamDto> getParamDtos() {
		return paramDtos;
	}

	public void setParamDtos(List<RequestParamDto> paramDtos) {
		this.paramDtos = paramDtos;
	}

	@Override
	public String toString() {
		return "MethodExplainDto{" +
				"methodName='" + methodName + '\'' +
				", explain='" + explain + '\'' +
				", paramDtos=" + paramDtos +
				'}';
	}
}
