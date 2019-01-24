package com.github.wangmingchang.automateapidocs.pojo.apidocs;

import java.util.Map;

/**
 * 多行注释返回类
 * 
 * @author 王明昌
 * @since 2017年9月9日
 */

public class ClassMoreRemarkDto {

	private Map<String,MethodExplainDto> methodExplainDtoMap; //类的方法多行注释Map(key:methodMapKey-0；value:和备注信息)
	private ClassExplainDto classExplainDto; //类的注释说明DTO

	public Map<String, MethodExplainDto> getMethodExplainDtoMap() {
		return methodExplainDtoMap;
	}

	public void setMethodExplainDtoMap(Map<String, MethodExplainDto> methodExplainDtoMap) {
		this.methodExplainDtoMap = methodExplainDtoMap;
	}

	public ClassExplainDto getClassExplainDto() {
		return classExplainDto;
	}

	public void setClassExplainDto(ClassExplainDto classExplainDto) {
		this.classExplainDto = classExplainDto;
	}

	@Override
	public String toString() {
		return "ClassMoreRemarkDto{" +
				"methodExplainDtoMap=" + methodExplainDtoMap +
				", classExplainDto=" + classExplainDto +
				'}';
	}
}
