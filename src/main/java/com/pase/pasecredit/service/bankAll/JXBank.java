package com.pase.pasecredit.service.bankAll;

import com.pase.pasecredit.model.CreditCardBill;
import com.pase.pasecredit.utils.CommonSetCredit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.util.Elements;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 9:21 2018/5/31
 */
public class JXBank {
    Logger logger = LoggerFactory.getLogger(JXBank.class);
    private String bill = "";//本期应还款
    private String minRepay = "";//最低还款
    private String quota = ""; //信用卡额度
    private String billDay = "";//账单日
    private String bankCard = "";//银行卡号
    private String repayDay = "";//还款日
    private String mailName = "";

    /**
     * 解析江西银行账单数据
     *
     * @param acont 邮件文本内容
     */
    public CreditCardBill parsingJxBillsHtml(String userName, String acont) {
        CreditCardBill creditCardBill = new CreditCardBill();
        Document document = Jsoup.parse(acont);
        try {
            if (userName.indexOf("@sina.com") > 0) {
                logger.info("开始解析新浪邮箱>>>>>>>>>>>>>>");
                mailName = "新浪邮箱";
                Element element = document.getElementById("isForwardContent");
                Document document1 = element.ownerDocument();
                creditCardBill = paseJxBankAll(document1);

            } else if (userName.indexOf("@163.com") > 0) {
                logger.info("开始解析网易163邮箱>>>>>>>>>>>>>>");
                mailName = "网易163邮箱";
                creditCardBill = paseJxBankAll(document);

            } else if (userName.indexOf("@126.com") > 0) {
                logger.info("开始解析网易126邮箱>>>>>>>>>>>>>>");
                mailName = "网易126邮箱";
                creditCardBill = paseJxBankAll(document);

            }

            CommonSetCredit.setCredit(creditCardBill, userName, 0, mailName, "");
        } catch (Exception e) {
            CommonSetCredit.setCredit(creditCardBill, userName, 3, mailName, "解析出现异常");

            logger.error("******************解析江西银行账单错误***" + acont + "***************");
            e.printStackTrace();
        }

        return creditCardBill;
    }

    public CreditCardBill paseJxBankAll(Document document) throws Exception {
        CreditCardBill creditCardBill = new CreditCardBill();
        billDay = document.select("table").get(1).select("td[width=55%]").get(0).text()
                .replaceAll("　", "");
        Date date = new SimpleDateFormat("yyyy年MM月dd").parse(billDay);
        billDay = new SimpleDateFormat("yyyy-MM-dd").format(date);
        quota = document.select("table").get(1).select("td[width=55%]").get(1).text().replaceAll(",", "");
        Pattern pattern1 = Pattern.compile("-*\\d+(\\.\\d+)?");
        Matcher matcher1 = pattern1.matcher(quota);
        if (matcher1.find()) {
            quota = matcher1.group();
        }
        org.jsoup.select.Elements all = document.select("table").select("td[width=222]").select("b");
        repayDay = all.get(0).text().replaceAll("　", "");
        Date date1 = new SimpleDateFormat("yyyy年MM月dd").parse(repayDay);
        repayDay = new SimpleDateFormat("yyyy-MM-dd").format(date1);
        bill = all.get(1).text().replaceAll(",", "");
        minRepay = all.get(3).text().replaceAll(",", "");
        Matcher matcher2 = pattern1.matcher(bill);
        if (matcher2.find()) {
            bill = matcher2.group();
        }
        Matcher matcher3 = pattern1.matcher(minRepay);
        if (matcher3.find()) {
            minRepay = matcher3.group();
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
