package com.pase.pasecredit.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 9:13 2018/5/31
 */
public class ResultJson {

    public static JSONObject getSuccessResult(JSONObject data) {
        JSONObject json = new JSONObject();
        json.put("msg", "成功");
        json.put("success", 1);//0：失败 ，1：成功
        json.put("data", data);
        return json;
    }

    public static JSONObject getFailResult(JSONObject data) {
        JSONObject json = new JSONObject();
        json.put("msg", "失败");
        json.put("success", 0);//0：失败 ，1：成功
        json.put("data", data);
        return json;
    }
}
