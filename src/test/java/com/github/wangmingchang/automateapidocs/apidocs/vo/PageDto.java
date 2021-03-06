package com.github.wangmingchang.automateapidocs.apidocs.vo;

/**
 * 分页DTO
 * 
 * @author 王明昌
 * @since 2017年9月9日
 */
public class PageDto extends BaseResponseVo {
	private long pageNo; // 页码
	private long pageSize; // 页数

	public PageDto(long pageNo, long pageSize) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public PageDto() {
		super();
	}

	public long getPageNo() {
		return pageNo;
	}

	public void setPageNo(long pageNo) {
		this.pageNo = pageNo;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}
}
