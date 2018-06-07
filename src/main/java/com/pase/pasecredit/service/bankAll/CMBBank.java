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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 16:42 2018/5/24
 */
public class CMBBank {
    Logger logger = LoggerFactory.getLogger(CMBBank.class);


    private String bill = "";//本期应还款
    private String minRepay = "";//最低还款
    private String quota = ""; //信用卡额度
    private String billDay = "";//账单日
    private String bankCard = "";//银行卡号
    private String repayDay = "";//还款日
    private String mailName = "";

    /**
     * 解析招商银行账单数据
     *
     * @param acont 邮件文本内容
     */
    public CreditCardBill parsingCMBBills(String userName, String acont) {
        CreditCardBill creditCardBill = new CreditCardBill();
        Document document = Jsoup.parse(acont);
        try {
            if (userName.indexOf("@sina.com") > 0) {
                logger.info("开始解析新浪邮箱>>>>>>>>>>>>>>");
                mailName = "新浪邮箱";
                Element element = document.getElementById("isForwardContent");
                Document document1 = element.ownerDocument();
                creditCardBill = paseAllCMBBank(document1);

            } else if (userName.indexOf("@163.com") > 0) {
                logger.info("开始解析网易163邮箱>>>>>>>>>>>>>>");
                mailName = "网易邮箱163";
                creditCardBill = paseAllCMBBank(document);
            } else if (userName.indexOf("@126.com") > 0) {
                logger.info("开始解析网易126邮箱>>>>>>>>>>>>>>");
                mailName = "网易126邮箱";
                creditCardBill = paseAllCMBBank(document);
            }

            CommonSetCredit.setCredit(creditCardBill, userName, 0, mailName, "");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("******************解析招商银行账单错误***" + acont + "***************");
            CommonSetCredit.setCredit(creditCardBill, userName, 3, mailName, "解析出现异常");
        }
        return creditCardBill;
    }

    public CreditCardBill paseAllCMBBank(Document document) {
        CreditCardBill creditCardBill = new CreditCardBill();
        Calendar calendar = Calendar.getInstance();
        if (document.getElementById("fixBand13") != null) {
            Element element = document.getElementById("fixBand13");
            Element repayDayElement = element.getElementById("fixBand18");
            Element billElement = element.getElementById("fixBand52");
            Element minRepayElement = element.getElementById("fixBand53");
            repayDay = calendar.get(Calendar.YEAR) + "-" + repayDayElement.select("font").eq(1).text().replaceAll("/", "-");
            String billAll = billElement.select("font").text().replaceAll(" ", "").replaceAll(",", "");
            String replyAll = minRepayElement.select("font").text().replaceAll(" ", "").replaceAll(",", "");
            String bill1 = billAll.substring(0, billAll.indexOf("＄")).replaceAll("￥", "");
            String bill2 = billAll.substring(billAll.indexOf("＄"), billAll.length()).replaceAll("＄", "");
            String minRepay1 = replyAll.substring(0, replyAll.indexOf("＄")).replaceAll("￥", "");
            String minRepay2 = replyAll.substring(replyAll.indexOf("＄"), replyAll.length()).replaceAll("＄", "");
            if (bill1.equals("0.00")) {
                bill = bill2;
                minRepay = minRepay2;
            } else if (bill2.equals("0.00")) {
                bill = bill1;
                minRepay = minRepay1;
            }
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
