package com.wmc.AutomateApiDocs.pojo.apidocs;

import java.util.List;

/**
 * 方法信息DTO
 * 
 * @author OP-T-PC-0036
 *
 */
public class MethodInfoDto {

	private String methodDescription;// 方法说明
	private String type; // 请求方法
	private String url; // 请求路径
	private List<RequestParamDto> requestParamDtos; // 请求的参数
	List<ResponseDataDto> responseDataDtos; // 响应字段信息
	List<ResponseDataDto> baseResponseDataDtos; // 基础返回类信息

	public String getMethodDescription() {
		return methodDescription;
	}

	public void setMethodDescription(String methodDescription) {
		this.methodDescription = methodDescription;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<RequestParamDto> getRequestParamDtos() {
		return requestParamDtos;
	}

	public void setRequestParamDtos(List<RequestParamDto> requestParamDtos) {
		this.requestParamDtos = requestParamDtos;
	}

	public List<ResponseDataDto> getResponseDataDtos() {
		return responseDataDtos;
	}

	public void setResponseDataDtos(List<ResponseDataDto> responseDataDtos) {
		this.responseDataDtos = responseDataDtos;
	}

	public List<ResponseDataDto> getBaseResponseDataDtos() {
		return baseResponseDataDtos;
	}

	public void setBaseResponseDataDtos(List<ResponseDataDto> baseResponseDataDtos) {
		this.baseResponseDataDtos = baseResponseDataDtos;
	}

	@Override
	public String toString() {
		return "MethodInfoDto [methodDescription=" + methodDescription + ", type=" + type + ", url=" + url
				+ ", requestParamDtos=" + requestParamDtos + ", responseDataDtos=" + responseDataDtos
				+ ", baseResponseDataDtos=" + baseResponseDataDtos + "]";
	}

}
