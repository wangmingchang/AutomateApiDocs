package com.wmc.AutomateApiDocs.pojo.apidocs;

import com.wmc.AutomateApiDocs.pojo.vo.BaseResponseVo;

/**
 * 分页DTO
 * @author 王明昌
 * @date 2017年9月9日
 */
public class PageDto extends BaseResponseVo{
	private long pageNo; //页码
	private long pageSize; //页数
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
