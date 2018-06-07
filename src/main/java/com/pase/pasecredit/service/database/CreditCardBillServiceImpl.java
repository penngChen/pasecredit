package com.pase.pasecredit.service.database;

import com.pase.pasecredit.model.CreditCardBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 16:17 2018/5/28
 */

@Service
@Transactional
public class CreditCardBillServiceImpl implements CreditCardBillService {
    @Autowired
    private CreditCardBillRepository creditCardBillRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    public CreditCardBill findById(Integer id) {
        return em.find(CreditCardBill.class, id);
    }

    @Override
    public void updateInfoSource(String infoSource, String errorContent, String idCard, String bankName, String paymentsDay) {
        creditCardBillRepository.updateInfoSource(infoSource, errorContent, idCard, bankName, paymentsDay);
    }

//    @Override
//    public CreditCardBill save(CreditCardBill creditCardBill) {
//        if (creditCardBill.getId() == null) {
//            em.persist(creditCardBill);
//            return creditCardBill;
//        } else {
//            return this.em.merge(creditCardBill);
//        }
//    }

    @Override
    public CreditCardBill save(CreditCardBill creditCardBill) {
        return creditCardBillRepository.save(creditCardBill);
    }

    @Override
    public CreditCardBill findByIdCardAndBankNameAndPaymentsDate(String idCard, String bankName, String paymentsDate) {
        return creditCardBillRepository.findByIdCardAndBankNameAndPaymentsDate(idCard, bankName, paymentsDate);
    }

    @Override
    public List findByIdCard(String idCard, Sort sort) {
        return creditCardBillRepository.findByIdCard(idCard, sort);
    }
}
