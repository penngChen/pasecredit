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
 * @Date: Created in 16:50 2018/5/24
 */
public class CMBCBank {

    Logger logger = LoggerFactory.getLogger(CMBCBank.class);

    private String bill = "";//本期应还款
    private String minRepay = "";//最低还款
    private String quota = ""; //信用卡额度
    private String billDay = "";//账单日
    private String bankCard = "";//银行卡号
    private String repayDay = "";//还款日
    private String mailName = "";

    /**
     * 解析民生银行账单数据
     *
     * @param acont 邮件文本内容
     */
    public CreditCardBill parsingCMBCBillsHtml(String userName, String acont) {
        CreditCardBill creditCardBill = new CreditCardBill();
        Document document = Jsoup.parse(acont);
        try {
            if (userName.indexOf("@sina.com") > 0) {
                logger.info("开始解析新浪邮箱>>>>>>>>>>>>>>");
                mailName = "新浪邮箱";
                acont = document.text().replaceAll(" ", "");
                int in1 = acont.indexOf("最后还款日");
                int in2 = acont.indexOf("[温馨提示]");
                String s1 = acont.substring(in1, in2).replaceAll("/", "-");
                Pattern pattern = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})");
                Matcher matcher = pattern.matcher(s1);
                if (matcher.find()) {
                    repayDay = matcher.group();
                }
                String s2[] = s1.split("RMB");
                bill = s2[2].replaceAll(",", "");
                creditCardBill.setCreditNumber(bankCard);
                creditCardBill.setPayments(bill);
                creditCardBill.setPaymentsDate(repayDay);
                creditCardBill.setCreditLine(quota);

            } else if (userName.indexOf("@163.com") > 0) {
                logger.info("开始解析网易163邮箱>>>>>>>>>>>>>>");
                mailName = "网易163邮箱";
                creditCardBill = paseAllCMBCBank(document);
            } else if (userName.indexOf("@126.com") > 0) {
                logger.info("开始解析网易126邮箱>>>>>>>>>>>>>>");
                mailName = "网易126邮箱";
                creditCardBill = paseAllCMBCBank(document);
            }

            CommonSetCredit.setCredit(creditCardBill, userName, 0, mailName, "");

        } catch (Exception e) {
            e.printStackTrace();
            CommonSetCredit.setCredit(creditCardBill, userName, 3, mailName, "解析出现异常");
            logger.error("******************解析民生银行账单错误***" + acont + "***************");
        }
        return creditCardBill;
    }

    public CreditCardBill paseAllCMBCBank(Document document) {
        CreditCardBill creditCardBill = new CreditCardBill();
        Element element = document.getElementById("fixBand1");
        if (element != null) {
            //(\d+(\.\d+)?)
            Elements elements = element.getElementsByTag("tbody").eq(0);
            Elements fonts = elements.select("font");
            billDay = fonts.eq(6).text().replaceAll("/", "-");
            repayDay = fonts.eq(10).text().replaceAll("/", "-");
            bill = fonts.eq(23).text().replaceAll(",", "").replaceAll(" ", "");
            minRepay = fonts.eq(24).text().replaceAll(",", "").replaceAll(" ", "");
            Pattern pattern1 = Pattern.compile("-*\\d+(\\.\\d+)?");
            Matcher matcher1 = pattern1.matcher(bill);
            if (matcher1.find()) {
                bill = matcher1.group();
            }
            Matcher matcher2 = pattern1.matcher(minRepay);
            if (matcher2.find()) {
                minRepay = matcher2.group();
            }
            creditCardBill.setCreditNumber(bankCard);
            creditCardBill.setPayments(bill);
            //creditCardBill.setBillDay(billDay);
            creditCardBill.setPaymentsDate(repayDay);
            creditCardBill.setCreditLine(quota);
            //creditCardBill.setMinRepay(minRepay);
        }
        return creditCardBill;
    }
}
