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

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 16:57 2018/5/24
 */
public class CGBBank {
    Logger logger = LoggerFactory.getLogger(CGBBank.class);


    private String bill = "";//本期应还款
    private String minRepay = "";//最低还款
    private String quota = ""; //信用卡额度
    private String billDay = "";//账单日
    private String bankCard = "";//银行卡号
    private String repayDay = "";//还款日
    private String mailName = "";

    /**
     * 解析广发银行账单数据
     *
     * @param acont 邮件文本内容
     */

    public CreditCardBill parsingCGBBillsHtml(String userName, String acont) {
        CreditCardBill creditCardBill = new CreditCardBill();
        Document document = Jsoup.parse(acont);
        try {
            if (userName.indexOf("@sina.com") > 0) {
                logger.info("开始解析新浪邮箱>>>>>>>>>>>>>>");
                mailName = "新浪邮箱";
                Element element = document.getElementById("isForwardContent");
                Document document1 = element.ownerDocument();
                creditCardBill = paseAllCGBBank(document1);

            } else if (userName.indexOf("@163.com") > 0) {
                mailName = "网易163邮箱";
                logger.info("开始解析网易163邮箱>>>>>>>>>>>>>>");
                creditCardBill = paseAllCGBBank(document);
            } else if (userName.indexOf("@126.com") > 0) {
                mailName = "网易126邮箱";
                logger.info("开始解析网易126邮箱>>>>>>>>>>>>>>");
                creditCardBill = paseAllCGBBank(document);
            }

            CommonSetCredit.setCredit(creditCardBill, userName, 0, mailName, "");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("******************解析广发银行账单错误***" + acont + "***************");
            CommonSetCredit.setCredit(creditCardBill, userName, 3, mailName, "解析出现异常");

        }
        return creditCardBill;
    }

    public CreditCardBill paseAllCGBBank(Document document) {
        CreditCardBill creditCardBill = new CreditCardBill();
        Element element = document.getElementById("fixBand1");
        if (element != null) {
            Elements elements1 = element.select("div#fixBand32");
            String s1 = elements1.select("font").text().replaceAll(" ", "");
            billDay = s1.substring(s1.indexOf("至") + 1, s1.length()).replaceAll("/", "-");
            Elements elements2 = element.select("div#fixBand4").eq(1);
            Elements fonts = elements2.select("font");
            bankCard = fonts.eq(0).text();
            bill = fonts.eq(1).text().replaceAll(",", "");
            minRepay = fonts.eq(2).text().replaceAll(",", "");
            repayDay = fonts.eq(3).text().replaceAll("/", "-");
            quota = fonts.eq(5).text().replaceAll(",", "");

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
