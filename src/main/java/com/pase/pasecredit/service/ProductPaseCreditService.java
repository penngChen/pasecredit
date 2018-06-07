package com.pase.pasecredit.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 11:00 2018/5/29
 */
public interface ProductPaseCreditService {

    /**
     * @param pathName   邮件保存路径
     * @param param   参数集合
     * @return
     */
    public JSONObject checkPaseAllMail( String acout,String param, String pathName);
}
