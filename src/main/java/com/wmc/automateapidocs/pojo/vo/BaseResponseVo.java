package com.wmc.automateapidocs.pojo.vo;
/**
 * 基础返回类
 * @author 王明昌
 * @date 2017年9月9日
 */
public class BaseResponseVo {
	private String status; // 状态（1：成功；0：失败）
	private String msg; // 状态说明
	private Object result; // 返回数据

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "BaseResponseVo [status=" + status + ", msg=" + msg + ", result=" + result + "]";
	}

}
