package com.github.wangmingchang.automateapidocs.pojo.apidocs;

/**
 * 请求参数DTO
 * 
 * @author 王明昌
 * @since 2017年9月9日
 */
public class RequestParamDto {
	private String name; // 参数名称
	private String type; // 参数类型
	private boolean required; // 是否必须
	private String description; // 参数说明

	public RequestParamDto(){
		this.type = "string";
		this.required = true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "RequestParamDto [name=" + name + ", type=" + type + ", required=" + required + ", description=" + description
				+ "]";
	}

}
