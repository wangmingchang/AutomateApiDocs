package com.wmc.AutomateApiDocs.pojo.vo;

import java.io.Serializable;

/**
 * 测试返回类
 * 
 * @author 王明昌
 * @date 2017年9月9日
 */
public class DemoVo implements Serializable{

	private static final long serialVersionUID = -550552481238025259L;
	private String id; // 主键
	private String name; // 姓名
	private Double socre; // 分数
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getSocre() {
		return socre;
	}

	public void setSocre(Double socre) {
		this.socre = socre;
	}

	@Override
	public String toString() {
		return "DemoVo [id=" + id + ", name=" + name + ", socre=" + socre + "]";
	}

}
