package com.pase.pasecredit.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pase.pasecredit.model.CreditCardBill;
import com.pase.pasecredit.service.bankAll.*;
import com.pase.pasecredit.service.database.CreditCardBillService;
import com.pase.pasecredit.utils.CommonSetCredit;
import com.pase.pasecredit.utils.ResultJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 11:00 2018/5/29
 */
@Service
public class ProductPaseCreditServiceImpl implements ProductPaseCreditService {
    Logger logger = LoggerFactory.getLogger(ProductPaseCreditServiceImpl.class);
    CMBBank cmbBank = new CMBBank();
    BCMBank bcmBank = new BCMBank();
    CEBBank cebBank = new CEBBank();
    CCBBank ccbBank = new CCBBank();
    CMBCBank cmbcBank = new CMBCBank();
    CGBBank cgbBank = new CGBBank();
    CiticBank citicBank = new CiticBank();
    JXBank jxBank = new JXBank();

    @Autowired
    private CreditCardBillService creditCardBillService;

    @Override
    public JSONObject checkPaseAllMail(String acout, String param, String pathName) {
        JSONObject data = new JSONObject();
        JSONObject testdata = new JSONObject();
        try {
            String bankName = "";//银行名称
            List<CreditCardBill> list = new <CreditCardBill>ArrayList();
            JSONObject jsonObject = JSONObject.parseObject(param);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            String splitStr = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
            String acouts[] = acout.split(splitStr);
            logger.info("邮箱共有：" + acouts.length + "封");
            if (jsonArray.size() > 0) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    String acoutAll = acouts[i];
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String idAll = jsonObject1.getString("id");
                    String userNameAll = jsonObject1.getString("receiveMail");
                    String subjectAll = jsonObject1.getString("subject");
                    String sendPeopleAll = jsonObject1.getString("sendMail");
                    String idCardAll = jsonObject1.getString("idCard");
                    String sendTimeAll = jsonObject1.getString("sendTime");
                    System.err.println("id为：" + idAll);
                    System.err.println("发件人为：" + sendPeopleAll);
                    System.err.println("主题为：" + subjectAll);

                    if (sendPeopleAll.equals("ccsvc@message.cmbchina.com") || subjectAll.contains("招商银行信用卡电子账单")) {
                        bankName = "招商银行";
                    } else if (sendPeopleAll.equals("pccc@bocomcc.com") || subjectAll.contains("交通银行信用卡电子账单")) {
                        bankName = "交通银行";
                    } else if (sendPeopleAll.equals("cebbank@cardcenter.cebbank.com") || subjectAll.contains("光大银行信用卡电子对账单")) {
                        bankName = "光大银行";
                    } else if (sendPeopleAll.equals("service@vip.ccb.com") || subjectAll.contains("中国建设银行信用卡电子账单")) {
                        bankName = "建设银行";
                    } else if (sendPeopleAll.equals("master@creditcard.cmbc.com.cn") || subjectAll.contains("民生信用卡") && subjectAll.contains("电子对账单")) {
                        bankName = "民生银行";
                    } else if (sendPeopleAll.equals("creditcard@cgbchina.com.cn") || subjectAll.contains("广发卡") && subjectAll.contains("账单")) {
                        bankName = "广发银行";
                    } else if (sendPeopleAll.equals("citiccard@citiccard.com") || subjectAll.contains("中信银行信用卡电子账单")) {
                        bankName = "中信银行";
                    } else if (sendPeopleAll.equals("creditcard@cc.jx-bank.com") || subjectAll.contains("江西银行信用卡电子账单")) {
                        bankName = "江西银行";
                    } else {
                        logger.info("尚不支持该邮件解析，请稍后再试！");
                        bankName = "";
                    }

                    if ("招商银行".equals(bankName)) {
                        logger.info("开始解析招商银行信用卡账单");
                        CreditCardBill cmbcreditCardBill = cmbBank.parsingCMBBills(userNameAll, acoutAll);
                        setAll(cmbcreditCardBill, bankName, sendPeopleAll, subjectAll, pathName, sendTimeAll, idCardAll);
                        list.add(cmbcreditCardBill);
                        updateCredit(cmbcreditCardBill);

                    } else if ("交通银行".equals(bankName)) {
                        logger.info("开始解析交通银行信用卡账单");
                        CreditCardBill bcmCreditCardBill = bcmBank.parsingBCMBillsHtml(userNameAll, acoutAll);
                        setAll(bcmCreditCardBill, bankName, sendPeopleAll, subjectAll, pathName, sendTimeAll, idCardAll);
                        list.add(bcmCreditCardBill);
                        updateCredit(bcmCreditCardBill);

                    } else if ("光大银行".equals(bankName)) {
                        logger.info("开始解析光大银行信用卡账单");
                        CreditCardBill cebCreditCardBill = cebBank.parsingCCBBills(userNameAll, acoutAll);
                        setAll(cebCreditCardBill, bankName, sendPeopleAll, subjectAll, pathName, sendTimeAll, idCardAll);
                        list.add(cebCreditCardBill);
                        updateCredit(cebCreditCardBill);

                    } else if ("建设银行".equals(bankName)) {
                        logger.info("开始解析建设银行信用卡账单");
                        CreditCardBill ccbCreditCardBill = ccbBank.parsingCCBBillsHtml(userNameAll, acoutAll);
                        setAll(ccbCreditCardBill, bankName, sendPeopleAll, subjectAll, pathName, sendTimeAll, idCardAll);
                        list.add(ccbCreditCardBill);
                        updateCredit(ccbCreditCardBill);

                    } else if ("民生银行".equals(bankName)) {
                        logger.info("开始解析民生银行信用卡账单");
                        CreditCardBill cmbcCreditCardBill = cmbcBank.parsingCMBCBillsHtml(userNameAll, acoutAll);
                        setAll(cmbcCreditCardBill, bankName, sendPeopleAll, subjectAll, pathName, sendTimeAll, idCardAll);
                        list.add(cmbcCreditCardBill);
                        updateCredit(cmbcCreditCardBill);

                    } else if ("广发银行".equals(bankName)) {
                        logger.info("开始解析广发银行信用卡账单");
                        CreditCardBill cgbCreditCardBill = cgbBank.parsingCGBBillsHtml(userNameAll, acoutAll);
                        setAll(cgbCreditCardBill, bankName, sendPeopleAll, subjectAll, pathName, sendTimeAll, idCardAll);
                        list.add(cgbCreditCardBill);
                        updateCredit(cgbCreditCardBill);

                    } else if ("中信银行".equals(bankName)) {
                        logger.info("开始解析中信银行信用卡账单");
                        CreditCardBill citicCreditCardBill = citicBank.parsingCITICBillsHtml(userNameAll, acoutAll);
                        setAll(citicCreditCardBill, bankName, sendPeopleAll, subjectAll, pathName, sendTimeAll, idCardAll);
                        list.add(citicCreditCardBill);
                        updateCredit(citicCreditCardBill);

                    } else if ("江西银行".equals(bankName)) {
                        logger.info("开始解析江西银行信用卡电子账单");
                        CreditCardBill jxCreditCardBill = jxBank.parsingJxBillsHtml(userNameAll, acoutAll);
                        setAll(jxCreditCardBill, bankName, sendPeopleAll, subjectAll, pathName, sendTimeAll, idCardAll);
                        list.add(jxCreditCardBill);
                        updateCredit(jxCreditCardBill);

                    } else if ("".equals(bankName)) {
                        CreditCardBill creditCardBill = new CreditCardBill();
                        creditCardBill.setIdCard(idCardAll);
                        creditCardBill.setReceiveAddUrl(userNameAll);
                        creditCardBill.setIsSuccess(2);
                        creditCardBill.setErrorInfo("尚不支持该邮件解析");
                        setAll(creditCardBill, bankName, sendPeopleAll, subjectAll, pathName, sendTimeAll, idCardAll);
                        list.add(creditCardBill);
                        creditCardBillService.save(creditCardBill);
                    }
                }
            }
            testdata.put("list", list);
            logger.info("解析结果为：" + testdata);
            data.put("list", new ArrayList());
            return ResultJson.getSuccessResult(data);
        } catch (Exception e) {
            e.printStackTrace();
            data.put("list", new ArrayList());
            return ResultJson.getFailResult(data);
        }
    }

    public void updateCredit(CreditCardBill creditCardBill) {
        CreditCardBill creditCardBill1 = creditCardBillService.findByIdCardAndBankNameAndPaymentsDate(creditCardBill.getIdCard(), creditCardBill.getBankName(), creditCardBill.getPaymentsDate());
        if (creditCardBill1 == null || !creditCardBill.getBankName().equals(creditCardBill1.getBankName()) && !creditCardBill.getIdCard().equals(creditCardBill1.getIdCard()) && !creditCardBill.getPaymentsDate().equals(creditCardBill1.getPaymentsDate())) {
            creditCardBillService.save(creditCardBill);
        } else {
            creditCardBillService.updateInfoSource(creditCardBill.getInfoSource(), creditCardBill.getErrorContent(), creditCardBill.getIdCard(), creditCardBill.getBankName(), creditCardBill.getPaymentsDate());
        }
    }

    public void setAll(CreditCardBill creditCardBill, String bankName, String senPeople, String subject, String pathName, String sendTime, String idCard) {
        creditCardBill.setBankName(bankName);
        creditCardBill.setSenderUrl(senPeople);
        creditCardBill.setSubject(subject);
        creditCardBill.setErrorContent(pathName);
        creditCardBill.setSendTime(sendTime);
        creditCardBill.setIdCard(idCard);
    }

//    public   static   List<CreditCardBill>  removeDuplicate(List<CreditCardBill> list)  {
//        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
//            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
//                if  (list.get(j).equals(list.get(i)))  {
//                    list.remove(j);
//                }
//            }
//        }
//        return list;
//    }

    /*
     * unicode编码转中文
     */
    public static String decodeUnicode(String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }
}
