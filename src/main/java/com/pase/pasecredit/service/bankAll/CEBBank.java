package com.pase.pasecredit.service.bankAll;

import com.alibaba.fastjson.JSONObject;
import com.pase.pasecredit.model.CreditCardBill;
import com.pase.pasecredit.utils.CommonSetCredit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 16:46 2018/5/24
 */
public class CEBBank {
    Logger logger = LoggerFactory.getLogger(CEBBank.class);

    private String bill = "";//本期应还款
    private String minRepay = "";//最低还款
    private String quota = ""; //信用卡额度
    private String billDay = "";//账单日
    private String bankCard = "";//银行卡号
    private String repayDay = "";//还款日
    private String mailName = "";

    /**
     * 解析光大银行账单数据
     *
     * @param acont 邮件文本内容
     */
    public CreditCardBill parsingCCBBills(String userName, String acont) {
        CreditCardBill creditCardBill = new CreditCardBill();
        Document document = Jsoup.parse(acont);
        try {
            if (userName.indexOf("@sina.com") > 0) {
                logger.info("开始解析新浪邮箱>>>>>>>>>>>>>>");
                mailName = "新浪邮箱";
                Element element = document.getElementById("isForwardContent");
                creditCardBill = paseAllCEBbank(element.ownerDocument());
            } else if (userName.indexOf("@163.com") > 0) {
                logger.info("开始解析网易163邮箱>>>>>>>>>>>>>>");
                mailName = "网易163邮箱";
                creditCardBill = paseAllCEBbank(document);
            } else if (userName.indexOf("@126.com") > 0) {
                logger.info("开始解析网易126邮箱>>>>>>>>>>>>>>");
                mailName = "网易126邮箱";
                creditCardBill = paseAllCEBbank(document);
            }

            CommonSetCredit.setCredit(creditCardBill, userName, 0, mailName, "");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("******************解析光大银行账单错误***" + acont + "***************");
            CommonSetCredit.setCredit(creditCardBill, userName, 3, mailName, "解析出现异常");
        }
        return creditCardBill;
    }

    public CreditCardBill paseAllCEBbank(Document document) {
        CreditCardBill creditCardBill = new CreditCardBill();
        String acont = document.text().replaceAll(" ", "");
        //判断中文字符
        String reg = "[\u4e00-\u9fa5]";
        // 定义一些特殊字符的正则表达式 如：&nbsp;&lt;&gt;
        String regEx_special = "\\&[a-zA-Z]{1,10};";
        // 定义HTML标签的正则表达式
        String regEx_html = "<[^>]+>";
        acont = acont.replaceAll(regEx_special, "").replaceAll(regEx_html, "").replaceAll("nbsp;", "");
        String acont1 = acont.substring(acont.indexOf("账单日"), acont.indexOf("本期账户交易明细"));
        String acont2 = acont.substring(acont.indexOf("账号"), acont.indexOf("交易日期"));
        String s = acont1.substring(0, acont1.indexOf("。"));
        Pattern pat = Pattern.compile(reg);
        Matcher mat = pat.matcher(s);
        String repickStr = mat.replaceAll("");
        String ss = repickStr.substring(0, repickStr.lastIndexOf(".") + 3);
        String sss = ss.substring(ss.indexOf("/") - 4, ss.length());
        billDay = sss.substring(0, 10).replaceAll("/", "-");
        repayDay = sss.substring(10, 20).replaceAll("/", "-");
        String allMoney = sss.substring(21, sss.length());
        if (allMoney.indexOf("￥") > 0) {
            String ary[] = allMoney.split("￥");
            if (allMoney != null && !"".equals(allMoney)) {
                quota = ary[0].replaceAll(",", "");
                bill = ary[1].replaceAll(",", "");
                minRepay = ary[2].replaceAll(",", "");
            }
        } else {
            return CommonSetCredit.setCredit(creditCardBill, "", 1, mailName, "抱歉！仅支持人民币");

        }
        bankCard = acont2.substring(acont2.indexOf("*") - 5, acont2.lastIndexOf("*") + 8);

        creditCardBill.setCreditNumber(bankCard);
        creditCardBill.setPayments(bill);
        //creditCardBill.setBillDay(billDay);
        creditCardBill.setPaymentsDate(repayDay);
        creditCardBill.setCreditLine(quota);
        //creditCardBill.setMinRepay(minRepay);

        return creditCardBill;
    }

}
