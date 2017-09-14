package com.wmc.AutomateApiDocs.pojo.vo;

import java.util.Date;

public class CatVo {
	private String catName; // 猫名
	private Date adoptionDate; // 领养日期
	private String address; // 领养地址

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
		return "CatVo [catName=" + catName + ", adoptionDate=" + adoptionDate + ", address=" + address + "]";
	}

}
