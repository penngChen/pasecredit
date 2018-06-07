package com.pase.pasecredit.service.bankAll;

import com.pase.pasecredit.model.CreditCardBill;
import com.pase.pasecredit.utils.CommonSetCredit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.util.Elements;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 9:23 2018/5/31
 */
public class CiticBank {
    Logger logger = LoggerFactory.getLogger(CiticBank.class);
    private String bill = "";//本期应还款
    private String minRepay = "";//最低还款
    private String quota = ""; //信用卡额度
    private String billDay = "";//账单日
    private String bankCard = "";//银行卡号
    private String repayDay = "";//还款日
    private String mailName = "";


    /**
     * 解析中信银行账单数据
     *
     * @param acout 邮件文本内容
     */
    public CreditCardBill parsingCITICBillsHtml(String userName, String acout) {
        CreditCardBill creditCardBill = new CreditCardBill();
        Document document = Jsoup.parse(acout);
        try {
            if (userName.indexOf("@sina.com") > 0) {
                logger.info("开始解析新浪邮箱>>>>>>>>>>>>>>");
                mailName = "新浪邮箱";
                acout = document.text().replaceAll(" ", "");
                int in1 = acout.indexOf("到期还款日：");
                int in2 = acout.indexOf("取现额度");
                int in3 = acout.indexOf("◆卡号:");
                int in4 = acout.indexOf("交易日");
                int in5 = acout.lastIndexOf("到期还款日：");
                repayDay = acout.substring(in1, in2).replaceAll("[-|\\/|年|月|\\|\\s]", "-");
                //定义日期的正则
                Pattern pattern = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})");
                Matcher matcher = pattern.matcher(repayDay);
                if (matcher.find()) {
                    repayDay = matcher.group();
                }
                bankCard = acout.substring(in3 + ("◆卡号:").length(), in4).replaceAll("-", "");
                String s1 = acout.substring(in1, in5);
                if (s1.indexOf("RMB") > 0) {
                    String s2[] = s1.split("RMB");
                    bill = s2[1].replaceAll(",", "");
                    quota = s2[3].replaceAll(",", "");

                } else {
                    return CommonSetCredit.setCredit(creditCardBill, userName, 1, mailName, "抱歉！仅支持人民币");
                }
                creditCardBill.setCreditNumber(bankCard);
                creditCardBill.setPayments(bill);
                creditCardBill.setPaymentsDate(repayDay);
                creditCardBill.setCreditLine(quota);

            } else if (userName.indexOf("@163.com") > 0) {
                logger.info("开始解析网易163邮箱邮箱>>>>>>>>>>>>>>");
                mailName = "网易163邮箱";
                creditCardBill = paseCiticBankAll(document);
            } else if (userName.indexOf("@126.com") > 0) {
                logger.info("开始解析网易126邮箱邮箱>>>>>>>>>>>>>>");
                mailName = "网易126邮箱";
                creditCardBill = paseCiticBankAll(document);
            }

            CommonSetCredit.setCredit(creditCardBill, userName, 0, mailName, "");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("******************解析中信银行账单错误***" + acout + "***************");
            CommonSetCredit.setCredit(creditCardBill, userName, 3, mailName, "解析出现异常");

        }
        return creditCardBill;
    }

    public CreditCardBill paseCiticBankAll(Document document) {
        CreditCardBill creditCardBill = new CreditCardBill();
        org.jsoup.select.Elements elements = document.select("span[id=fixBand1]");
        Pattern pattern = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})");
        org.jsoup.select.Elements elements1 = elements.select("span[id=fixBand50]").select("table").get(1).select("div");
        String s1 = elements1.text();
        s1 = s1.substring(s1.indexOf("-") + 1, s1.length());
        s1 = s1.replaceAll("[-|\\/|年|月|\\|\\s]", "-");
        Matcher matcher1 = pattern.matcher(s1);
        if (matcher1.find()) {
            billDay = matcher1.group(0);
        }
        org.jsoup.select.Elements elements2 = elements.select("span[id=fixBand36]").select("table").get(2).select("div");
        repayDay = elements2.text().replaceAll("[-|\\/|年|月|\\|\\s]", "-");
        Matcher matcher = pattern.matcher(repayDay);
        if (matcher.find()) {
            repayDay = matcher.group(0);
        }
        bill = elements.select("span[id=loopBand1]").select("span[id=fixBand16]")
                .select("font").get(1).text().replaceAll(",", "");
        minRepay = elements.select("span[id=loopBand2]").select("span[id=fixBand11]")
                .select("font").get(1).text().replaceAll(",", "");

        quota = elements.select("span[id=fixBand22]").select("span[id=fixBand12]")
                .select("font").get(1).text().replaceAll(",", "");
        bankCard = elements.select("span[id=loopBand3]").select("font").get(0).text();
        bankCard = bankCard.substring(bankCard.indexOf(":") + 1, bankCard.length()).replaceAll("-", "");

        creditCardBill.setCreditNumber(bankCard);
        creditCardBill.setPayments(bill);
        //creditCardBill.setBillDay(billDay);
        creditCardBill.setPaymentsDate(repayDay);
        creditCardBill.setCreditLine(quota);
        //creditCardBill.setMinRepay(minRepay);
        return creditCardBill;
    }
}
