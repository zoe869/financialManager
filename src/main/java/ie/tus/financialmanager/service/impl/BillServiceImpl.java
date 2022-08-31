package ie.tus.financialmanager.service.impl;

import ie.tus.financialmanager.entity.*;
import ie.tus.financialmanager.repositories.*;
import ie.tus.financialmanager.service.BillService;
import ie.tus.financialmanager.util.Config;
import ie.tus.financialmanager.util.JpaUtil;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private PaywayRepository paywayRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecordTitleRepository recordTitleRepository;

    /**
     * test
     *
     * @param session
     * @return
     */


    @Override
    public List<Payway> getAllPayways(HttpSession session) {
        UserInfo user = Config.getSessionUser(session);
        return paywayRepository.findAllByUserInfo_Username(user.getUsername());
    }

    //有用
    @Override
    public Set<RecordTitle> getPaymentTypes(HttpSession session) {
        UserInfo user = Config.getSessionUser(session);
        return recordTitleRepository.findAllByUserInfo(user);
    }

    @Override
    public int updatePayway(Payway way) {
        Payway inDb = paywayRepository.getOne(way.getId());
        if (inDb!=null) {
            JpaUtil.copyNotNullProperties(way, inDb);
        }
        Payway update = paywayRepository.save(inDb);
        return update!=null?1:0;
    }

    @Override
    public void delPayway(Payway payway) {
        paywayRepository.delete(payway);
    }

    @Override
    public int addTag(RecordTitle recordTitle, HttpSession session) {
        UserInfo curuser = Config.getSessionUser(session);
        recordTitle.setUserInfo(curuser);
        RecordTitle res = recordTitleRepository.save(recordTitle);
        return res!=null?1:0;
    }

    @Override
    public Page getTagsByWhere(RecordTitle recordTitle, Pageable page) {
        return recordTitleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (null!=recordTitle.getUserInfo()) {//userinfo
                predicates.add(criteriaBuilder.equal(root.get("userInfo").get("id"), recordTitle.getUserInfo().getId()));
            }
            if (!StringUtils.isEmpty(recordTitle.getTitle())) {//name
                predicates.add(criteriaBuilder.equal(root.get("title"), recordTitle.getTitle()));
            }
            if (!StringUtils.isEmpty(recordTitle.getType())) {//name
                if (!"-1".equals(recordTitle.getType())) predicates.add(criteriaBuilder.equal(root.get("type"), recordTitle.getType()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },page);
    }

    @Override
    public int updateTag(RecordTitle recordTitle, HttpSession session) {
        RecordTitle inDb = recordTitleRepository.getOne(recordTitle.getId());
        if (inDb!=null) {
            JpaUtil.copyNotNullProperties(recordTitle, inDb);
        }
        RecordTitle update = recordTitleRepository.save(inDb);
        return update!=null?1:0;
    }

    @Override
    public void delRecordTitle(RecordTitle recordTitle) {
        recordTitleRepository.delete(recordTitle);
    }


    @Override
    public int addBill(Bill bill, HttpSession session) {
        //获取时间
        Date ss = new Date();
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format0.format(ss.getTime());
        bill.setTime(time);
        //获取姓名
        UserInfo userInfo1=getSessionUser(session);
        bill.setUserInfo(userInfo1);
        bill.setPayway(paywayRepository.getOne(bill.getPayway().getId()));
        Bill res = billRepository.save(bill);

        return res!=null?1:0;
    }

    @Override
    public Page<Bill> getBillsByWhere(Bill bill, Pageable page) {
//        Page<Bill> all = billRepository.searchRecordByCondition(bill,page);
//        return all;
        return billRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<Predicate>();

            if (bill.getTypeid() != 0) {//Type
                predicates.add(criteriaBuilder.equal(root.get("typeid"), bill.getTypeid()));
            }
            if (!StringUtils.isEmpty(bill.getStartTime())) {//startTime
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("time").as(LocalDate.class), LocalDate.parse(bill.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            }
            if (!StringUtils.isEmpty(bill.getEndTime())) {//endTime
                predicates.add(criteriaBuilder.lessThan(root.get("time").as(LocalDate.class), LocalDate.parse(bill.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            }
            if (bill.getUserInfo() != null && !StringUtils.isEmpty(bill.getUserInfo().getRealname())) {//realname
                predicates.add(criteriaBuilder.like(root.get("userInfo").get("realname"), bill.getUserInfo().getRealname()));
            }
            if (!StringUtils.isEmpty(bill.getTitle())) {//title
                predicates.add(criteriaBuilder.equal(root.get("title"), bill.getTitle()));
            }
            if (null != bill.getPayway()) {//paywayid
                predicates.add(criteriaBuilder.equal(root.get("payway").get("id"), bill.getPayway().getId()));
            }
            if (!StringUtils.isEmpty(bill.getRemark())) {//remark
                predicates.add(criteriaBuilder.like(root.get("remark"), bill.getRemark()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, page);

    }


    @Override
    public Bill updateBill(Bill bill){
        Bill find = billRepository.getOne(bill.getId());
        if (find!=null) {
            JpaUtil.copyNotNullProperties(bill, find);
        }
        Bill update = billRepository.save(find);
        return update;
    }

    @Override
    public void deleteBill(Bill bill) {
        billRepository.delete(bill);
    }

    @Override
    public List<Bill> findByWhereNoPage(Bill bill) {
        return this.findBills(bill);
    }

    @Override
    public Payway addPayway(Payway payway, HttpSession session) {
        UserInfo user = Config.getSessionUser(session);
        UserInfo inDb = userRepository.findByUsername(user.getUsername());
        payway.setUserInfo(inDb);
        return paywayRepository.save(payway);
    }

    @Override
    public Page<Payway> findPaywaysByWhereWithPage(Payway payway, Pageable pageable){
        return paywayRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (null!=payway.getUserInfo()) {//userinfo
                predicates.add(criteriaBuilder.equal(root.get("userInfo").get("id"), payway.getUserInfo().getId()));
            }
            if (!StringUtils.isEmpty(payway.getName())) {//name
                predicates.add(criteriaBuilder.equal(root.get("name"), payway.getName()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageable);

    }


    private List<Bill> findBills(Bill bill) {

        return billRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            if (!StringUtils.isEmpty(bill.getType())) {//Type
                predicates.add(criteriaBuilder.equal(root.get("typeid"), bill.getTypeid()));
            }
            if (!StringUtils.isEmpty(bill.getStartTime())) {//startTime
                LocalDateTime startTime = LocalDateTime.parse(bill.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("time").as(LocalDateTime.class), startTime));
            }
//            if (!StringUtils.isEmpty(bill.getEndTime())) {//endTime
//                LocalDateTime endtime = LocalDateTime.parse(bill.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                predicates.add(criteriaBuilder.lessThan(root.get("time").as(LocalDateTime.class), endtime));
//            }
            if (bill.getUserInfo() != null && !StringUtils.isEmpty(bill.getUserInfo().getRealname())) {//realname
                predicates.add(criteriaBuilder.equal(root.get("userInfo").get("realname"), bill.getUserInfo().getRealname()));
            }
            if (!StringUtils.isEmpty(bill.getTitle())) {//title
                predicates.add(criteriaBuilder.equal(root.get("title"), bill.getTitle()));
            }
            if (null!=bill.getPayway()) {//paywayid
                predicates.add(criteriaBuilder.equal(root.get("payway").get("id"), bill.getPayway().getId()));
            }
            if (!StringUtils.isEmpty(bill.getRemark())) {//remark
                predicates.add(criteriaBuilder.like(root.get("remark"), bill.getRemark()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });

    }


    private UserInfo getSessionUser(HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(Config.CURRENT_USERNAME);
        userInfo.setPassword(null);
        return userInfo;
    }

}
