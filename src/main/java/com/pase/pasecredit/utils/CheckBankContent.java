package com.pase.pasecredit.utils;

import com.pase.pasecredit.model.CreditCardBill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 16:31 2018/5/23
 */
public class CheckBankContent {

    Logger logger4J = LoggerFactory.getLogger(CheckBankContent.class);

    /**
     * 处理账单错误位置 记录错误账单到日志
     *
     * @param
     * @param bankName 账单变化的银行
     * @return
     */
    public CreditCardBill isCreditBillChange(String bankName, String bankCard, String bill, String minRepay, String repayDay, String billDay, String creditLine) {
        CreditCardBill creditCardBill = new CreditCardBill();
        if (!bankCard.contains("*") && "".equals(bankName)) {
            creditCardBill.setIsSuccess(1);
            creditCardBill.setErrorInfo("卡号位置发生变化");
            logger4J.info("******************" + bankName + "银行账单“卡号位置发生变化”******************");
        }
        if (bill.replaceAll("(?:-|\\.)", "").matches("[0-9]+") == false && !"".equals(bill)) {
            creditCardBill.setIsSuccess(1);
            creditCardBill.setErrorInfo("账单位置发生变化");
            logger4J.info("******************" + bankName + "银行账单“已出账单位置发生变化”******************");
        }
        if (minRepay.replaceAll("(?:-|\\.)", "").matches("[0-9]+") == false && !"".equals(minRepay)) {
            creditCardBill.setIsSuccess(1);
            creditCardBill.setErrorInfo("最低还款位置发生变化");
            logger4J.info("******************" + bankName + "银行账单“最低还款位置发生变化”******************");
        }
        if (repayDay.replaceAll("-", "").matches("[0-9]+") == false && !"".equals(repayDay)) {
            creditCardBill.setIsSuccess(1);
            creditCardBill.setErrorInfo("还款日位置发生变化");
            logger4J.info("******************" + bankName + "银行账单“还款日位置发生变化”******************");
        }
        if (billDay.replaceAll("-", "").matches("[0-9]+") == false && !"".equals(billDay)) {
            creditCardBill.setIsSuccess(1);
            creditCardBill.setErrorInfo("账单日位置发生变化");
            logger4J.info("******************" + bankName + "银行账单“账单日位置发生变化”******************");
        }
        if (creditLine.replaceAll("(?:-|\\.)", "").matches("[0-9]+") == false && !"".equals(creditLine)) {
            creditCardBill.setIsSuccess(1);
            creditCardBill.setErrorInfo("信用额度位置发生变化");
            logger4J.info("******************" + bankName + "银行账单“信用额度位置发生变化”******************");
        }
        return creditCardBill;
    }

}
