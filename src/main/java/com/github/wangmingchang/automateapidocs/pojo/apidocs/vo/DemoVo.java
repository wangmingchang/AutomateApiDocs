package com.github.wangmingchang.automateapidocs.pojo.apidocs.vo;

import java.util.List;

/**
 * 测试返回类
 * 
 * @author 王明昌
 * @since 2017年9月9日
 */
public class DemoVo {

	private String id; // 主键
	private String name; // 姓名
	private Double socre; // 分数
	private CarVo carVo; // 车
	private List<DemoVo> demoVos; // 路径集合

	public List<DemoVo> getDemoVos() {
		return demoVos;
	}

	public void setDemoVos(List<DemoVo> demoVos) {
		this.demoVos = demoVos;
	}

	public CarVo getCarVo() {
		return carVo;
	}

	public void setCarVo(CarVo carVo) {
		this.carVo = carVo;
	}

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
