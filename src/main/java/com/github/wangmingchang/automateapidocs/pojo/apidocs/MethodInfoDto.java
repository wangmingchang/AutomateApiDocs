package com.github.wangmingchang.automateapidocs.pojo.apidocs;

import java.util.List;

/**
 * 方法信息DTO
 * 
 * @since OP-T-PC-0036
 *
 */
public class MethodInfoDto {

	private String methodDescription;// 方法说明
	private String type; // 请求方法
	private String url; // 请求路径
	private List<RequestParamDto> requestParamDtos; // 请求的参数
	List<ResponseClassDto> responseClassDtos; // 返回类的信息
	List<ResponseDataDto> baseResponseDataDtos; // 基础返回类信息
	private String methodKey; //方法的唯一标识，UUID
	private String requestBeanJsonKey; //请求参数样例的key
	private String responseBeanJsonKey; //响应结果样例的key

	public String getRequestBeanJsonKey() {
		return requestBeanJsonKey;
	}

	public void setRequestBeanJsonKey(String requestBeanJsonKey) {
		this.requestBeanJsonKey = requestBeanJsonKey;
	}

	public String getResponseBeanJsonKey() {
		return responseBeanJsonKey;
	}

	public void setResponseBeanJsonKey(String responseBeanJsonKey) {
		this.responseBeanJsonKey = responseBeanJsonKey;
	}

	public String getMethodKey() {
		return methodKey;
	}

	public void setMethodKey(String methodKey) {
		this.methodKey = methodKey;
	}

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

	public List<ResponseDataDto> getBaseResponseDataDtos() {
		return baseResponseDataDtos;
	}

	public void setBaseResponseDataDtos(List<ResponseDataDto> baseResponseDataDtos) {
		this.baseResponseDataDtos = baseResponseDataDtos;
	}

	public List<ResponseClassDto> getResponseClassDtos() {
		return responseClassDtos;
	}

	public void setResponseClassDtos(List<ResponseClassDto> responseClassDtos) {
		this.responseClassDtos = responseClassDtos;
	}

	@Override
	public String toString() {
		return "MethodInfoDto{" +
				"methodDescription='" + methodDescription + '\'' +
				", type='" + type + '\'' +
				", url='" + url + '\'' +
				", requestParamDtos=" + requestParamDtos +
				", responseClassDtos=" + responseClassDtos +
				", baseResponseDataDtos=" + baseResponseDataDtos +
				", methodKey='" + methodKey + '\'' +
				", requestBeanJsonKey='" + requestBeanJsonKey + '\'' +
				", responseBeanJsonKey='" + responseBeanJsonKey + '\'' +
				'}';
	}
}
