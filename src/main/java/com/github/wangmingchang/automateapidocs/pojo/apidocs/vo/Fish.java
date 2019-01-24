package com.github.wangmingchang.automateapidocs.pojo.apidocs.vo;

/**
 * 鱼类
 * 
 * @author 王明昌
 * @since 2017年10月11日
 */
public class Fish {
	/**
	 * 种类
	 */
	private String type;
	private String name; // 名字
	/**分页信息*/
	private PageDto pageDto;

	public PageDto getPageDto() {
		return pageDto;
	}

	public void setPageDto(PageDto pageDto) {
		this.pageDto = pageDto;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Fish [type=" + type + ", name=" + name + "]";
	}

}
