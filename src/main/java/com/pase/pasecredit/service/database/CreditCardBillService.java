package com.pase.pasecredit.service.database;

import com.pase.pasecredit.model.CreditCardBill;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 16:14 2018/5/28
 */
public interface CreditCardBillService {
    public CreditCardBill save(CreditCardBill creditCardBill);

    public CreditCardBill findByIdCardAndBankNameAndPaymentsDate(String idCard, String bankName, String paymentsDate);

    public List findByIdCard(String idCard, Sort sort);

    public CreditCardBill findById(Integer id);

    public void updateInfoSource(String infoSource, String errorContent, String idCard, String bankName, String paymentsDay);

}
