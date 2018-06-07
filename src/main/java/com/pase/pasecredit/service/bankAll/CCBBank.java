package com.pase.pasecredit.service.bankAll;

import com.pase.pasecredit.model.CreditCardBill;
import com.pase.pasecredit.utils.CheckBankContent;
import com.pase.pasecredit.utils.CommonSetCredit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 16:55 2018/5/24
 */
public class CCBBank {

    Logger logger = LoggerFactory.getLogger(CCBBank.class);

    private CheckBankContent ck = new CheckBankContent();


    private String bill = "";//本期应还款
    private String minRepay = "";//最低还款
    private String quota = ""; //信用卡额度
    private String bankCard = "";//银行卡号
    private String repayDay = "";//还款日
    private String billDay = "";//账单日
    private String mailName = "";

    /**
     * 解析建设银行账单数据
     *
     * @param acont 邮件文本内容
     */
    public CreditCardBill parsingCCBBillsHtml(String userName, String acont) {
        CreditCardBill creditCardBill = new CreditCardBill();
        Document document = Jsoup.parse(acont);
        try {
            if (userName.indexOf("@sina.com") > 0) {
                logger.info("开始解析新浪邮箱>>>>>>>>>>>>>>");
                mailName = "新浪邮箱";
                Element element = document.getElementById("isForwardContent");
                Elements elements = element.select("table");
                creditCardBill = paseAllCCBBank(elements);
            } else if (userName.indexOf("@163.com") > 0) {
                logger.info("开始解析网易163邮箱>>>>>>>>>>>>>>");
                mailName = "网易163邮箱";
                Elements elements = document.select("table");
                creditCardBill = paseAllCCBBank(elements);
            } else if (userName.indexOf("@126.com") > 0) {
                logger.info("开始解析网易126邮箱>>>>>>>>>>>>>>");
                mailName = "网易126邮箱";
                Elements elements = document.select("table");
                creditCardBill = paseAllCCBBank(elements);
            }

//            CreditCardBill credit = ck.isCreditBillChange("建设银行", bankCard, bill, minRepay, repayDay, billDay, quota);
            CommonSetCredit.setCredit(creditCardBill, userName, 0, mailName, "");

        } catch (Exception e) {
            e.printStackTrace();
            CommonSetCredit.setCredit(creditCardBill, userName, 3, mailName, "解析出现异常");
            logger.error("******************解析建设银行账单错误***" + acont + "***************");
        }
        return creditCardBill;
    }

    public CreditCardBill paseAllCCBBank(Elements elements) {
        CreditCardBill creditCardBill = new CreditCardBill();
        if (!elements.select("tbody").isEmpty()) {
            Elements resultLinks = elements.select("tbody > tr").eq(13);
            Elements resultLinks1 = elements.select("table").eq(15);
            Elements all = resultLinks.select("[width=100%]");
            Elements fonts = all.select("font");
            Elements fonts1 = fonts.select("[size=2]").select("[face=arial]");
            bill = fonts.select("[size=3]").eq(1).text().replaceAll(",", "");
            billDay = fonts1.eq(0).text();
            repayDay = fonts.eq(25).text().replaceAll("/", "-");
            String quota1 = fonts1.eq(1).text().replaceAll(",", "").replaceAll(" ", "");
            quota = quota1.substring(3, quota1.length());
            minRepay = fonts1.eq(4).text().replaceAll(",", "");
            bankCard = resultLinks1.select("font").select("[size=2]").select("[face=arial]").eq(0).text();

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
