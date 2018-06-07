package com.pase.pasecredit.model;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 9:54 2018/5/15
 */

@EnableAutoConfiguration   //  开启自动配置注解
@Entity /*注释指名这是一个实体Bean*/
@Table(name = "billcycleinfoentity") /*注释指定了Entity所要映射带数据库表*/
public class CreditCardBill {
    public String getErrorContent() {
        return errorContent;
    }

    @Override

    public String toString() {
        return "CreditCardBill{" +
                "id=" + id +
                ", idCard='" + idCard + '\'' +
                ", subject='" + subject + '\'' +
                ", senderUrl='" + senderUrl + '\'' +
                ", receiveAddUrl='" + receiveAddUrl + '\'' +
                ", payments='" + payments + '\'' +
                ", creditLine='" + creditLine + '\'' +
                ", paymentsDate='" + paymentsDate + '\'' +

                ", creditNumber='" + creditNumber + '\'' +
                ", infoSource='" + infoSource + '\'' +
                ", bankName='" + bankName + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", isSuccess=" + isSuccess +
                ", errorInfo='" + errorInfo + '\'' +
                ", ErrorContent='" + errorContent + '\'' +
                ", minRepay='" + minRepay + '\'' +
                ", billDay='" + billDay + '\'' +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "idcard")
    private String idCard;//身份证号

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSenderUrl() {
        return senderUrl;
    }

    public void setSenderUrl(String senderUrl) {
        this.senderUrl = senderUrl;
    }

    public String getReceiveAddUrl() {
        return receiveAddUrl;
    }

    public void setReceiveAddUrl(String receiveAddUrl) {
        this.receiveAddUrl = receiveAddUrl;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }

    public String getCreditLine() {
        return creditLine;
    }

    public void setCreditLine(String creditLine) {
        this.creditLine = creditLine;
    }

    public String getPaymentsDate() {
        return paymentsDate;
    }

    public void setPaymentsDate(String paymentsDate) {
        this.paymentsDate = paymentsDate;
    }

    public String getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(String creditNumber) {
        this.creditNumber = creditNumber;
    }

    public void setInfoSource(String infoSource) {
        this.infoSource = infoSource;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }


    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getInfoSource() {
        return infoSource;
    }

    public String getErrorInfo() {
        return errorInfo;
    }


    public String getMinRepay() {
        return minRepay;
    }

    public void setMinRepay(String minRepay) {
        this.minRepay = minRepay;
    }

    public String getBillDay() {
        return billDay;
    }

    public void setBillDay(String billDay) {
        this.billDay = billDay;
    }

    @Column(name = "subject")
    private String subject;//主题

    @Column(name = "senderurl")
    private String senderUrl = "";//发件人

    @Column(name = "receiveaddurl")
    private String receiveAddUrl = "";//收件人

    public void setErrorContent(String errorContent) {
        this.errorContent = errorContent;
    }

    @Column(name = "payments")
    private String payments;//本期应还款额度

    @Column(name = "creditline")
    private String creditLine;//信用卡额度

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "paymentsdate")

    private String paymentsDate;//到期还款日

    @Column(name = "creditnumber")
    private String creditNumber;//银行卡号

    @Column(name = "infosource")
    private String infoSource;//邮箱名称

    @Column(name = "bankname")
    private String bankName;//银行名称

    @Column(name = "sendtime")
    private String sendTime;//发件时间


    public String getSendTime() {
        return sendTime;
    }

    @Column(name = "issuccess")
    private int isSuccess;//状态

    @Column(name = "errorinfo")
    private String errorInfo;//错误原因

    @Column(name = "errorcontent")
    private String errorContent;//解析内容存放地址

    @Transient
    private String minRepay;//本期最低还款额

    @Transient
    private String billDay;//账单日


}
