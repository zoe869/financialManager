package ie.tus.financialmanager.controller;

import ie.tus.financialmanager.entity.Bill;
import ie.tus.financialmanager.entity.Budget;
import ie.tus.financialmanager.entity.RecordTitle;
import ie.tus.financialmanager.entity.UserInfo;
import ie.tus.financialmanager.service.BudgitService;
import ie.tus.financialmanager.util.Config;
import ie.tus.financialmanager.util.Result;
import ie.tus.financialmanager.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/budgets")
public class BudgitController {

    @Autowired
    private BudgitService budgitService;


    @RequestMapping("/getBudgets/{pageNo}/{pageSize}")
    public Result getTagsByWhere(Budget budget, @PathVariable int pageNo,
                                 @PathVariable int pageSize, HttpSession session) {
        pageNo=pageNo-1;

        UserInfo user = Config.getSessionUser(session);
        budget.setUserInfo(user);
        if (null==budget.getRecordTitle()) {
            budget.setRecordTitle(null);
        }
        if (!StringUtils.isEmpty(budget.getType())) {
            budget.setType(null);
        }
        Pageable page = PageRequest.of(pageNo, pageSize);
        Page res= budgitService.getBudgetsByWhere(budget,page);
        if (res!=null){
            return ResultUtil.success(res);
        }else {
            return ResultUtil.unSuccess();
        }
    }

    @RequestMapping("/getBudgets")
    public Result findByWhereNoPage(Budget budget, HttpSession session) {
        budget.setUserInfo(Config.getSessionUser(session));
        List<Budget> byWhereNoPage = budgitService.findByWhereNoPage(budget);
        return byWhereNoPage!=null? ResultUtil.success(byWhereNoPage):ResultUtil.unSuccess();
    }

    @RequestMapping("/alerts")
    public Result getAlert(Budget budget, HttpSession session) {
        List<Budget> alerts=budgitService.getAlert(budget,session);
        if (alerts != null) {
            return ResultUtil.success(alerts);
        }else{
            return ResultUtil.unSuccess();
        }
    }
    @RequestMapping("/addBudget")
    public Result addBudget(Budget budget, HttpSession session) {
        int res=budgitService.save(budget,session);
        if (res>0){
            return ResultUtil.success();
        }else {
            return ResultUtil.unSuccess();
        }
    }

    @RequestMapping("/delBudget")
    public Result delBudget(Budget budget) {
        budgitService.delBudget(budget);
        return ResultUtil.success();
    }

    @RequestMapping("/updateBudget")
    public Result updateBudget(Budget budget, HttpSession session) {
        int res=budgitService.updateBudget(budget,session);
        if (res>0){
            return ResultUtil.success();
        }else {
            return ResultUtil.unSuccess();
        }
    }






}
