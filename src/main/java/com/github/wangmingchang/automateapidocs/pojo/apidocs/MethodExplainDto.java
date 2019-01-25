package com.github.wangmingchang.automateapidocs.pojo.apidocs;

import java.util.List;

/**
 * 方法的注释说明DTO
 * 
 * @author 王明昌
 * @since 2017年9月9日
 */
public class MethodExplainDto {

	private String methodPath; //方法路径
	private String explain; // 方法业务说明
	private List<RequestParamDto> paramDtos; // 参数

	public String getMethodPath() {
		return methodPath;
	}

	public void setMethodPath(String methodPath) {
		this.methodPath = methodPath;
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
				"methodPath='" + methodPath + '\'' +
				", explain='" + explain + '\'' +
				", paramDtos=" + paramDtos +
				'}';
	}
}
