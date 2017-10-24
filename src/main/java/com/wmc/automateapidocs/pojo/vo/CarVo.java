package com.wmc.automateapidocs.pojo.vo;

import java.io.Serializable;

/**
 * 车信息
 * 
 * @author 王明昌
 * @date 2017年9月12日
 */
public class CarVo implements Serializable {

	private static final long serialVersionUID = 8788743094649743561L;
	private String carName; // 车名
	private Double weig; // 重量
	private String color; // 颜色
	private String displacement; // 排量
	private Double price; // 售价


	public CarVo() {
		super();
	}

	public CarVo(String carName, Double weig, String color, String displacement, Double price) {
		super();
		this.carName = carName;
		this.weig = weig;
		this.color = color;
		this.displacement = displacement;
		this.price = price;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public Double getWeig() {
		return weig;
	}

	public void setWeig(Double weig) {
		this.weig = weig;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDisplacement() {
		return displacement;
	}

	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "CarVo [carName=" + carName + ", weig=" + weig + ", color=" + color + ", displacement=" + displacement
				+ ", price=" + price + "]";
	}

}
