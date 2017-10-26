package com.wmc.automateapidocs.pojo.apidocs;

/**
 * 类的注释说明DTO
 * 
 * @author 王明昌
 * @since 2017年9月9日
 */
public class ClassExplainDto {

	private String explain; // 类的业务说明
	private String author; // 作者
	private String createDate; // 创建日期

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "ClassExplainDto [explain=" + explain + ", author=" + author + ", createDate=" + createDate + "]";
	}

}
