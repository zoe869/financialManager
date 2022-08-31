package ie.tus.financialmanager.service.impl;

import ie.tus.financialmanager.entity.Bill;
import ie.tus.financialmanager.entity.Budget;
import ie.tus.financialmanager.entity.RecordTitle;
import ie.tus.financialmanager.repositories.BudgetRepository;
import ie.tus.financialmanager.repositories.RecordTitleRepository;
import ie.tus.financialmanager.service.BillService;
import ie.tus.financialmanager.service.BudgitService;
import ie.tus.financialmanager.util.Config;
import ie.tus.financialmanager.util.JpaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BudgitServiceImpl implements BudgitService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BillService billService;

    @Autowired
    private RecordTitleRepository recordTitleRepository;

    @Override
    public List<Budget> findByWhereNoPage(Budget budget) {
        return budgetRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (null!=budget.getUserInfo()) {//username
                predicates.add(criteriaBuilder.equal(root.get("userInfo"), budget.getUserInfo()));
            }
            if (!StringUtils.isEmpty(budget.getType())) {//username
                predicates.add(criteriaBuilder.equal(root.get("type"), budget.getType()));
            }
            if (budget.getRecordTitle()!=null) {//username
                predicates.add(criteriaBuilder.equal(root.get("recordTitle").get("id"), budget.getRecordTitle()));
            }
            if (!StringUtils.isEmpty(budget.getStatus())) {//username
                predicates.add(criteriaBuilder.equal(root.get("status"), budget.getStatus()));
            }
            if (!StringUtils.isEmpty(budget.getStartDate())) {//startTime
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("time").as(LocalDate.class), LocalDate.parse(budget.getStartDate(), DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))));
            }
            if (!StringUtils.isEmpty(budget.getEndDate())) {//endTime
                predicates.add(criteriaBuilder.lessThan(root.get("time").as(LocalDate.class), LocalDate.parse(budget.getEndDate(),DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
    }

    @Override
    public int save(Budget budget, HttpSession session) {
        Date sysdate = new Date();
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format0.format(sysdate.getTime());
        budget.setTime(time);
        budget.setUserInfo(Config.getSessionUser(session));
        budget.setStatus(0);//初始化
        budget.setRecordTitle(recordTitleRepository.findByTitle(budget.getTitle()));
        return budgetRepository.save(budget)!=null?1:0;
    }

    @Override
    public void delBudget(Budget budget) {
        budgetRepository.delete(budget);
    }

    @Override
    public int updateBudget(Budget budget, HttpSession session) {
        Budget inDb = budgetRepository.getOne(budget.getId());
        JpaUtil.copyNotNullProperties(budget,inDb);
        return budgetRepository.save(inDb)!=null?1:0;
    }

    @Override
    public List<Budget> getAlert(Budget budget, HttpSession session) {

        List res=new ArrayList<String>();
        generateAlertMsg(budget,session);
        List<Budget> activeBudget = budgetRepository.findAllByStatus(1);
        return activeBudget;
    }

    @Override
    public Page getBudgetsByWhere(Budget budget, Pageable page) {
        return budgetRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<Predicate>();

            if (!StringUtils.isEmpty(budget.getType())) {//Type
                predicates.add(criteriaBuilder.equal(root.get("type"), budget.getType()));
            }
            if (!StringUtils.isEmpty(budget.getEndDate())) {//endTime
                predicates.add(criteriaBuilder.lessThan(root.get("time").as(LocalDate.class), LocalDate.parse(budget.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            }
            if (!StringUtils.isEmpty(budget.getStartDate())) {//starttime
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("time").as(LocalDate.class), LocalDate.parse(budget.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            }
            if (budget.getUserInfo() != null && !StringUtils.isEmpty(budget.getUserInfo().getRealname())) {//realname
                predicates.add(criteriaBuilder.like(root.get("userInfo").get("realname"), budget.getUserInfo().getRealname()));
            }
            if (null != budget.getRecordTitle()) {//title
                predicates.add(criteriaBuilder.equal(root.get("recordTitle"), budget.getRecordTitle()));
            }
            if (!StringUtils.isEmpty(budget.getStatus())) {//Type
                predicates.add(criteriaBuilder.equal(root.get("status"), budget.getStatus()));
            }

            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },page);
    }

    private void generateAlertMsg(Budget budget, HttpSession session) {
        //设置budget的记录时间
        //获取当前用户符合条件的所有预算记录
        budget.setUserInfo(Config.getSessionUser(session));
        //1用户
        List<Budget> budgets = this.findByWhereNoPage(budget);//查找全部符合条件的alert
        if (budgets == null) return;
        //1 遍历budget
        Budget save=null;
        for (Budget b :budgets) {
            //对应每一条budget b
//            读取title
//            读取用户
//            读取budget
//            获取当前时间
            SimpleDateFormat format =  new  SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            Bill bill=new Bill();
//            bill.setTitle(b.getRecordTitle().getTitle());
            bill.setUserInfo(b.getUserInfo());
            Calendar c = Calendar.getInstance();
//            获取开始时间
            c.add(Calendar.MONTH,-1);
            String startTime = format.format(c.getTime());
            bill.setStartTime(startTime);

            //查询所有符合条件的bills
            List<Bill> bills = billService.findByWhereNoPage(bill);//查找全部符合条件的bills
            double sum=0;
            double pay=0;
            double titlePay=0;//RecordTitle 是支出类型
            for (Bill btemp:bills) {
                if (btemp.getTypeid()==2) {
                    sum+=btemp.getMoney();//总收入
                }else {
                    pay+=btemp.getMoney();
                    if ((b.getRecordTitle().getTitle()).equals(btemp.getTitle())) {
                        //获取title类型支出总和
                        titlePay+=btemp.getMoney();
                    }
                    //总支出
                    pay+=btemp.getMoney();//总支出
                }
            }
            String message="";
            //按照金额控制
            if (b.getType() == 1) {//百分比
                if (b.getBudget()/100< titlePay/sum)
//                    message=b.getRecordTitle().getTitle()+"类型支出预算超支，相对同期收入消费占比为"+b.getBudget()+"%";
                    message="Budget overspending on "+b.getRecordTitle().getTitle()+"+ type expenditures, relative to "+b.getBudget()+"% of income consumption over the same period";
            }
            if (b.getType() == 2) {//按照金额
                if (b.getBudget()< titlePay)
//                    message=b.getRecordTitle().getTitle()+"类型支出金额超过预算上限："+b.getBudget()+"元";
                    message="The amount spent on l"+b.getRecordTitle().getTitle()+" types exceeds the budget ceiling by "+b.getBudget()+" euros";
            }//
            Budget inDb = budgetRepository.getOne(b.getId());
            //修改budget至提示状态
            inDb.setStatus(1);
            //储存message
            inDb.setMessage(message);
            save = budgetRepository.save(inDb);

        }
    }
}
