package com.wmc.automateapidocs.pojo.vo;

import java.util.Date;
import java.util.List;

/**
 * 猫类
 * 
 * @author 王明昌
 * @date 2017年10月11日
 */
public class CatVo {
	private String catName; // 猫名
	private Date adoptionDate; // 领养日期
	private String address; // 领养地址
	private DogVo dogVo; // 狗仔
	private List<Fish> fish; // 鱼集合

	public List<Fish> getFish() {
		return fish;
	}

	public void setFish(List<Fish> fish) {
		this.fish = fish;
	}

	public DogVo getDogVo() {
		return dogVo;
	}

	public void setDogVo(DogVo dogVo) {
		this.dogVo = dogVo;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public Date getAdoptionDate() {
		return adoptionDate;
	}

	public void setAdoptionDate(Date adoptionDate) {
		this.adoptionDate = adoptionDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "CatVo [catName=" + catName + ", adoptionDate=" + adoptionDate + ", address=" + address + ", dogVo="
				+ dogVo + ", fish=" + fish + "]";
	}

}
