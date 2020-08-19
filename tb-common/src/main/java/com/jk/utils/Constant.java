package com.jk.utils;

public class Constant {

    public static final String SELECT_USER_LIST = "selectUserList";

    public static final String SAVE_ORDER_WARNING_KEY = "SaveOrderWaringKey";
    /**
     * 短信验证码
     */
    public static final String SMS_URL="https://api.netease.im/sms/sendcode.action";
    /**
     * 验证码钥匙
     */
    public static final String APP_KEY = "5212292bdb72e1fe9cd9f3184dedc5f1";
    /**
     * 验证码密钥
     */
    public static final String APP_SECRET = "11133c696a68";
    /**
     * 验证码模板编号(如不指定则使用配置的默认模版)
     */
    public static final String TEMPLATEID = "14889350";
    /**
     * 验证码 冒号后面是手机号  缓存到redis中
     */
    public static final String SMS_CODE = "sms:code:";
    /**
     * 验证码 冒号后面是手机号  缓存到redis中
     */
    public static final String SMS_LOCK = "sms:lock:";

}
