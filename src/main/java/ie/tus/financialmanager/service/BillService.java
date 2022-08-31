package ie.tus.financialmanager.service;

import ie.tus.financialmanager.entity.*;
import ie.tus.financialmanager.util.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

public interface BillService {
    List<Payway> getAllPayways(HttpSession session);

    int addBill(Bill bill, HttpSession session);

    Page<Bill> getBillsByWhere(Bill bill, Pageable page);

    Bill updateBill(Bill bill);

    void deleteBill(Bill bill);

    List<Bill> findByWhereNoPage(Bill bill);

    Payway addPayway(Payway payway, HttpSession session);

    Page<Payway> findPaywaysByWhereWithPage(Payway payway, Pageable pageable);

    Set<RecordTitle> getPaymentTypes(HttpSession session);

    int updatePayway(Payway way);

    void delPayway(Payway payway);

    int addTag(RecordTitle recordTitle, HttpSession session);

    Page getTagsByWhere(RecordTitle recordTitle, Pageable page);

    int updateTag(RecordTitle recordTitle, HttpSession session);

    void delRecordTitle(RecordTitle recordTitle);

    //---------test----------
}
