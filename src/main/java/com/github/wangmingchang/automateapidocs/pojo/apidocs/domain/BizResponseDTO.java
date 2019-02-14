/**
 * Software License Declaration.
 * <p>
 * wandaph.com, Co,. Ltd.
 * Copyright ? 2017 All Rights Reserved.
 * <p>
 * Copyright Notice
 * This documents is provided to wandaph contracting agent or authorized programmer only.
 * This source code is written and edited by wandaph Co,.Ltd Inc specially for financial
 * business contracting agent or authorized cooperative company, in order to help them to
 * install, programme or central control in certain project by themselves independently.
 * <p>
 * Disclaimer
 * If this source code is needed by the one neither contracting agent nor authorized programmer
 * during the use of the code, should contact to wandaph Co,. Ltd Inc, and get the confirmation
 * and agreement of three departments managers  - Research Department, Marketing Department and
 * Production Department.Otherwise wandaph will charge the fee according to the programme itself.
 * <p>
 * Any one,including contracting agent and authorized programmer,cannot share this code to
 * the third party without the agreement of wandaph. If Any problem cannot be solved in the
 * procedure of programming should be feedback to wandaph Co,. Ltd Inc in time, Thank you!
 */
package com.github.wangmingchang.automateapidocs.pojo.apidocs.domain;

import java.io.Serializable;

/**
 * 业务响应
 * @author lianghaifeng
 * @version Id: BizResponseDTO.java, v 0.1 2019.1.7 007 14:38 lianghaifeng Exp $$
 */
public class BizResponseDTO<T extends Serializable> {
    /**
     * 请求成功
     */
    public static final String CODE_SUCCESS = "200000";
    /**
     * 参数检验错误
     */
    public static final String CODE_PARAM_ERROR = "200001";
    /**
     * 手机号已经在万e贷注册
     */
    public static final String CODE_USER_EXIST_WED = "921289";
    /**
     * 手机号已经在快易花注册
     */
    public static final String CODE_USER_EXIST_KYH = "921290";
    /**
     * 手机号已经在万达贷注册
     */
    public static final String CODE_USER_EXIST_WDD = "921292";
    /**
     * 外部用户ID已经存在
     */
    public static final String CODE_USER_ID_EXIST = "921293";
    /**
     * 用户不存在
     */
    public static final String CODE_USER_UN_EXIST = "921283";
    /**
     * 身份证已经被绑定
     */
    public static final String CODE_USER_IDNO_BINDED = "921291";
    /**
     * 身份证已经在万e贷注册
     */
    public static final String CODE_USER_IDNO_EXIST_WED = "921307";
    /**
     * 身份证已经在快易花注册
     */
    public static final String CODE_USER_IDNO_EXIST_KYH = "921308";
    /**
     * 身份证已经在万达贷注册
     */
    public static final String CODE_USER_IDNO_EXIST_WDD = "921309";
    /**
     * 信息被篡改
     */
    public static final String CODE_USER_INFO_TEMPER = "921203";

    /**
     * 返回状态码
     *
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
}
