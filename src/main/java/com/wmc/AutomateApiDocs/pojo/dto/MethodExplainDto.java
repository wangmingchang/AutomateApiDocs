package com.wmc.AutomateApiDocs.pojo.dto;

import java.util.List;

/**
 * 方法的注释说明DTO
 * 
 * @author 王明昌
 * @date 2017年9月9日
 */
public class MethodExplainDto {

	private String explain; // 方法业务说明
	private List<RequestParamDto> paramDtos; // 参数

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
		return "MethodExplainDto [explain=" + explain + ", paramDtos=" + paramDtos + "]";
	}

}
