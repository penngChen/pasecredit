package com.pase.pasecredit.service.database;

import com.pase.pasecredit.model.CreditCardBill;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 16:19 2018/5/28
 */
public interface CreditCardBillRepository extends JpaRepository<CreditCardBill, Integer> {
    public CreditCardBill save(CreditCardBill creditCardBill);

    public CreditCardBill findByIdCardAndBankNameAndPaymentsDate(String idCard, String bankName, String paymentsDate);

    public List findByIdCard(String idCard, Sort sort);

    @Modifying
    @Query("update CreditCardBill  c set c.infoSource=?1,c.errorContent=?2 where c.idCard=?3 and c.bankName=?4 and c.paymentsDate=?5")
    public void updateInfoSource(String infoSource, String errorContent, String idCard, String bankName, String paymentsDay);
}
