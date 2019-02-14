
package com.github.wangmingchang.automateapidocs.pojo.apidocs.vo;

/**
 * 业务响应
 * @author lianghaifeng
 * @version Id: BizResponseDTO.java, v 0.1 2019.1.7 007 14:38 lianghaifeng Exp $$
 */
public class BizResponseVo<T extends BaseResponseVo> {
    /**
     * 返回状态码
     * TRUE/FALSE 此字段是通信标识，非交易标识，交易是否成功需要查看bizCode来判断
     */
    private String code;
    /**
     * 返回信息，如非空，为错误原因，参数格式校验错误
     */
    private String msg;
    /**
     * 响应时间
     * 时间格式：时间搓精确到毫秒
     */
    private String sysTime;
    /**
     * 返回业务参数
     */
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BizResponseVo{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", sysTime='" + sysTime + '\'' +
                ", data=" + data +
                '}';
    }
}
