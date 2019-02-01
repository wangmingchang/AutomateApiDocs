package com.github.wangmingchang.automateapidocs.pojo.apidocs;

import java.util.List;

/**
 * properties配置参数Dto
 *
 * @escription
 * @auther: wangmingchang
 * @date: 2019/1/28 10:15
 */
public class PropertiesParamDto {

    private String className; //controller类的包名
    private String methodName; //方法名称
    private String url; //方法路径
    private String type; //请求类型
    private String methodExplain; //方法说明
    private String requestBeanCn; //请求对象的包名
    private String baseResponseBeanCn; //响应基类包名
    private String baseResponseBeanGenericityCn; //响应泛型包名
    private String responseBeanCn; //响应类包名
    private List<String> responseBeansCns; //响应类包名(多个)
    private List<String> requestFalses; //请求字段不是必传的（默认必传）
    private List<String> requestIsShowFalse; //请求字段不显示（默认显示）
    private List<String> responseIsShowFalse; //响应字段不显示（默认显示）

    public PropertiesParamDto(){
        this.className = "NA";
        this.methodName = "NA";
        this.url = "NA";
        this.type = "NA";
        this.methodExplain = "NA";
        this.requestBeanCn = "NA";
        this.baseResponseBeanCn = "NA";
        this.baseResponseBeanGenericityCn = "NA";
        this.responseBeanCn = "NA";
        this.responseBeansCns = null;
        this.requestFalses = null;
        this.requestIsShowFalse = null;
        this.responseIsShowFalse = null;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethodExplain() {
        return methodExplain;
    }

    public void setMethodExplain(String methodExplain) {
        this.methodExplain = methodExplain;
    }

    public String getRequestBeanCn() {
        return requestBeanCn;
    }

    public void setRequestBeanCn(String requestBeanCn) {
        this.requestBeanCn = requestBeanCn;
    }

    public String getBaseResponseBeanCn() {
        return baseResponseBeanCn;
    }

    public void setBaseResponseBeanCn(String baseResponseBeanCn) {
        this.baseResponseBeanCn = baseResponseBeanCn;
    }

    public String getBaseResponseBeanGenericityCn() {
        return baseResponseBeanGenericityCn;
    }

    public void setBaseResponseBeanGenericityCn(String baseResponseBeanGenericityCn) {
        this.baseResponseBeanGenericityCn = baseResponseBeanGenericityCn;
    }

    public String getResponseBeanCn() {
        return responseBeanCn;
    }

    public void setResponseBeanCn(String responseBeanCn) {
        this.responseBeanCn = responseBeanCn;
    }

    public List<String> getResponseBeansCns() {
        return responseBeansCns;
    }

    public void setResponseBeansCns(List<String> responseBeansCns) {
        this.responseBeansCns = responseBeansCns;
    }

    public List<String> getRequestFalses() {
        return requestFalses;
    }

    public void setRequestFalses(List<String> requestFalses) {
        this.requestFalses = requestFalses;
    }

    public List<String> getRequestIsShowFalse() {
        return requestIsShowFalse;
    }

    public void setRequestIsShowFalse(List<String> requestIsShowFalse) {
        this.requestIsShowFalse = requestIsShowFalse;
    }

    public List<String> getResponseIsShowFalse() {
        return responseIsShowFalse;
    }

    public void setResponseIsShowFalse(List<String> responseIsShowFalse) {
        this.responseIsShowFalse = responseIsShowFalse;
    }

    @Override
    public String toString() {
        return "PropertiesParamDto{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", methodExplain='" + methodExplain + '\'' +
                ", requestBeanCn='" + requestBeanCn + '\'' +
                ", baseResponseBeanCn='" + baseResponseBeanCn + '\'' +
                ", baseResponseBeanGenericityCn='" + baseResponseBeanGenericityCn + '\'' +
                ", responseBeanCn='" + responseBeanCn + '\'' +
                ", responseBeansCns=" + responseBeansCns +
                ", requestFalses=" + requestFalses +
                ", requestIsShowFalse=" + requestIsShowFalse +
                ", responseIsShowFalse=" + responseIsShowFalse +
                '}';
    }
}
