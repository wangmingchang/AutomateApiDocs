package com.wmc.AutomateApiDocs.pojo.dto;

/**
 * 响应数据DTO
 * 
 * @author 王明昌
 * @date 2017年9月10日
 */
public class ResponseDataDto {
	private String name; // 字段名
	private String type; // 字段类型
	private String description; // 字段注释
	private String childNode; // 子节点
	private String parentNode; // 父节点

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getChildNode() {
		return childNode;
	}

	public void setChildNode(String childNode) {
		this.childNode = childNode;
	}

	public String getParentNode() {
		return parentNode;
	}

	public void setParentNode(String parentNode) {
		this.parentNode = parentNode;
	}

	@Override
	public String toString() {
		return "ResponseDataDto [name=" + name + ", type=" + type + ", description=" + description + ", childNode="
				+ childNode + ", parentNode=" + parentNode + "]";
	}

}
