package com.pase.pasecredit.service.bankAll;

import com.alibaba.fastjson.JSONObject;
import com.pase.pasecredit.model.CreditCardBill;
import com.pase.pasecredit.service.database.CreditCardBillService;
import com.pase.pasecredit.utils.CheckBankContent;
import com.pase.pasecredit.utils.CommonSetCredit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 16:48 2018/5/24
 */
public class BCMBank {

    Logger logger = LoggerFactory.getLogger(BCMBank.class);
    CheckBankContent ck = new CheckBankContent();

    private String bill = "";//本期应还款
    private String minRepay = "";//最低还款
    private String quota = ""; //信用卡额度
    private String billDay = "";//账单日
    private String bankCard = "";//银行卡号
    private String repayDay = "";//还款日
    private String mailName = "";

    /**
     * 解析交通银行账单数据
     *
     * @param acont 邮件文本内容
     */
    public CreditCardBill parsingBCMBillsHtml(String userName, String acont) {
        Document document = Jsoup.parse(acont);
        CreditCardBill creditCardBill = new CreditCardBill();
        try {
            if (userName.indexOf("@sina.com") > 0) {
                logger.info("开始解析新浪邮箱>>>>>>>>>>>>>>");
                mailName = "新浪邮箱";
                Element element = document.getElementById("isForwardContent");
                Elements elements = element.getElementsByTag("table");
                creditCardBill = paseAllXLBCMBank(elements);
            } else if (userName.indexOf("@163.com") > 0) {
                logger.info("开始解析网易163邮箱>>>>>>>>>>>>>>");
                mailName = "网易163邮箱";
                Elements elements = document.getElementsByTag("table");
                creditCardBill = paseAllWYBCMBank(elements);
            } else if (userName.indexOf("@126.com") > 0) {
                logger.info("开始解析网易126邮箱>>>>>>>>>>>>>>");
                mailName = "网易126邮箱";
                Elements elements = document.getElementsByTag("table");
                creditCardBill = paseAllWYBCMBank(elements);
            }

            CommonSetCredit.setCredit(creditCardBill, userName, 0, mailName, "");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("******************解析交通银行账单错误***" + acont + "***************");
            CommonSetCredit.setCredit(creditCardBill, userName, 3, mailName, "解析出现异常");
        }
        return creditCardBill;
    }

    public CreditCardBill paseAllWYBCMBank(Elements elements) {
        CreditCardBill creditCardBill = new CreditCardBill();
        String s1 = elements.select("p").eq(2).text();
        String s2 = elements.select("p").eq(3).text();
        int in1 = s1.indexOf("卡号：");
        bankCard = s1.substring(in1 + ("卡号：").length(), s1.length());
        billDay = s2.substring(s2.indexOf("-") + 1, s2.length()).replaceAll("/", "-");
        String s3 = elements.eq(5).select("span").text().replaceAll(" ", "");
        int in2 = s3.indexOf("本期应还款额");
        int in3 = s3.indexOf("最低还款金额");
        int in4 = s3.indexOf("信用额度");
        int in5 = s3.indexOf("取现额度");
        repayDay = s3.substring("还款日".length(), in2).replaceAll("/", "-");
        String billAll = s3.substring(in2 + "本期应还款额".length(), in3).replaceAll(",", "");
        String cbill = billAll.substring(0, billAll.indexOf("$")).replaceAll("￥", "");
        String abill = billAll.substring(billAll.indexOf("$")).replaceAll("$", "");
        String minRepayAll = s3.substring(in3 + "最低还款金额".length(), in4).replaceAll(",", "");
        String cminRepay = minRepayAll.substring(0, minRepayAll.indexOf("$")).replaceAll("￥", "");
        String aminRepay = minRepayAll.substring(minRepayAll.indexOf("$")).replaceAll("$", "");
        String quatoAll = s3.substring(in4 + "信用额度".length(), in5).replaceAll(",", "");
        String cquato = quatoAll.substring(0, quatoAll.indexOf("$")).replaceAll("￥", "");
        String aquato = quatoAll.substring(quatoAll.indexOf("$")).replaceAll("$", "");
        if (cbill.equals("0.00")) {
            bill = abill;
            minRepay = aminRepay;
            quota = aquato;
        } else {
            bill = cbill;
            minRepay = cminRepay;
            quota = cquato;
        }
        creditCardBill.setCreditNumber(bankCard);
        creditCardBill.setPayments(bill);
        //creditCardBill.setBillDay(billDay);
        creditCardBill.setPaymentsDate(repayDay);
        creditCardBill.setCreditLine(quota);
        //creditCardBill.setMinRepay(minRepay);
        return creditCardBill;
    }

    public CreditCardBill paseAllXLBCMBank(Elements elements) {
        CreditCardBill creditCardBill = new CreditCardBill();
        String s1 = elements.select("p").select("a[target=_blank]").get(2).text();
        String s2 = elements.select("p").select("a[target=_blank]").get(3).text();
        int in1 = s1.indexOf("卡号：");
        bankCard = s1.substring(in1 + ("卡号：").length(), s1.length());
        billDay = s2.substring(s2.indexOf("-") + 1, s2.length()).replaceAll("/", "-");
        String s3 = elements.eq(5).select("span").text().replaceAll(" ", "");
        int in2 = s3.indexOf("本期应还款额");
        int in3 = s3.indexOf("最低还款金额");
        int in4 = s3.indexOf("信用额度");
        int in5 = s3.indexOf("取现额度");
        String repayDay = s3.substring("还款日".length(), in2).replaceAll("/", "-");
        String billAll = s3.substring(in2 + "本期应还款额".length(), in3).replaceAll(",", "");
        String cbill = billAll.substring(0, billAll.indexOf("$")).replaceAll("￥", "");
        String abill = billAll.substring(billAll.indexOf("$")).replaceAll("$", "");
        String minRepayAll = s3.substring(in3 + "最低还款金额".length(), in4).replaceAll(",", "");
        String cminRepay = minRepayAll.substring(0, minRepayAll.indexOf("$")).replaceAll("￥", "");
        String aminRepay = minRepayAll.substring(minRepayAll.indexOf("$")).replaceAll("$", "");
        String quatoAll = s3.substring(in4 + "信用额度".length(), in5).replaceAll(",", "");
        String cquato = quatoAll.substring(0, quatoAll.indexOf("$")).replaceAll("￥", "");
        String aquato = quatoAll.substring(quatoAll.indexOf("$")).replaceAll("$", "");
        if (cbill.equals("0.00")) {
            bill = abill;
            minRepay = aminRepay;
            quota = aquato;
        } else {
            bill = cbill;
            minRepay = cminRepay;
            quota = cquato;
        }
        creditCardBill.setCreditNumber(bankCard);
        creditCardBill.setPayments(bill);
        //creditCardBill.setBillDay(billDay);
        creditCardBill.setPaymentsDate(repayDay);
        creditCardBill.setCreditLine(quota);
        //creditCardBill.setMinRepay(minRepay);
        return creditCardBill;
    }
}
