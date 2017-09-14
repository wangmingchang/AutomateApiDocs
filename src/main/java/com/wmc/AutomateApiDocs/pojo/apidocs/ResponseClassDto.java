package com.wmc.AutomateApiDocs.pojo.apidocs;

import java.util.List;
/**
 * 返回数据类信息
 * @author 王明昌
 * @date 2017年9月12日
 */
public class ResponseClassDto {

	private String className; // 类名
	private List<ResponseDataDto> responseDataDtos; // 返回数据信息

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

	@Override
	public String toString() {
		return "ResponseClassDto [className=" + className + ", responseDataDtos=" + responseDataDtos + "]";
	}

}
