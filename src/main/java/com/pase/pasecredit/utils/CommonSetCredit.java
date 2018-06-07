package com.pase.pasecredit.utils;

import com.pase.pasecredit.model.CreditCardBill;

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 17:12 2018/6/4
 */
public class CommonSetCredit {

    public static CreditCardBill setCredit(CreditCardBill creditCardBill,String userName,int isSuccess,String mailName,String errorInfo){
        creditCardBill.setReceiveAddUrl(userName);
        creditCardBill.setIsSuccess(isSuccess);
        creditCardBill.setInfoSource(mailName);
        creditCardBill.setErrorInfo(errorInfo);
        return creditCardBill;
    }
}
