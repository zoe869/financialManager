package ie.tus.financialmanager.controller;

import ie.tus.financialmanager.entity.*;
import ie.tus.financialmanager.service.BillService;
import ie.tus.financialmanager.util.Config;
import ie.tus.financialmanager.util.Result;
import ie.tus.financialmanager.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @RequestMapping("/getPayways")
    public Result getAllPayways(HttpSession session){

        List<Payway> list = billService.getAllPayways(session);
        if (list != null && list.size()>0){
            return ResultUtil.success(list);
        }else {
            return ResultUtil.unSuccess();
        }
    }

    @RequestMapping("/getPaymentTypes")
    public Result getPaymentTypes(HttpSession session){

        Set<RecordTitle> list = billService.getPaymentTypes(session);
        if (list != null && list.size()>0){
            return ResultUtil.success(list);
        }else {
            return ResultUtil.unSuccess();
        }
    }

    @RequestMapping("/addBill")
    public Result addBill(Bill bill, HttpSession session){
        int result=billService.addBill(bill,session);
        if (result>0){
            return ResultUtil.success();
        }else {
            return ResultUtil.unSuccess();
        }

    }

    @RequestMapping("/getBillsByWhere/{type}/{pageNo}/{pageSize}")
    public Result<Bill> getBillsByWhere(Bill bill, @PathVariable("type") Integer typeid,
                                        @PathVariable int pageNo, @PathVariable int pageSize, HttpSession session) {

        pageNo=pageNo-1;

        if(!StringUtils.isEmpty(bill.getStartTime())){
            bill.setStartTime(bill.getStartTime()+" 00:00:00");
        }
        if(!StringUtils.isEmpty(bill.getEndTime())){
            bill.setEndTime(bill.getEndTime()+" 23:59:59");
        }
        if ("-1".equals(bill.getPayway())) {
            bill.setPayway(null);
        }
        if (!StringUtils.isEmpty(bill.getTitle())) {
            bill.setTitle(null);
        }

        bill.setTypeid(typeid);

        bill = getBill(bill,session);//
        Pageable page = PageRequest.of(pageNo, pageSize);
        Page<Bill> billPage=billService.getBillsByWhere(bill,page);
        return ResultUtil.success(billPage);
    }

    private Bill getBill(Bill bill, HttpSession session) {
        UserInfo user = Config.getSessionUser(session);
        bill.setUserInfo(user);
        return bill;
    }

    @RequestMapping("/updateBill")
    public Result updateBill(Bill bill){
        Bill res = billService.updateBill(bill);
        if (res!=null){
            return ResultUtil.success();
        }else {
            return ResultUtil.unSuccess();
        }
    }

    @RequestMapping("/delBill")
    public Result deleteBill(Bill bill){
        billService.deleteBill(bill);
        return ResultUtil.success();
    }

    /**
     *  作用于统计图
     * @param bill
     * @param session
     * @return
     */
    @RequestMapping("/getBillsToChart")
    public Result<Bill> findByWhereNoPage(Bill bill, HttpSession session) {
        bill = getBill(bill, session);
        List<Bill> byWhereNoPage = billService.findByWhereNoPage(bill);
        return byWhereNoPage!=null?ResultUtil.success(byWhereNoPage):ResultUtil.unSuccess();
    }


    @RequestMapping("/addPayway")
    public Result addPayway(Payway way, HttpSession session){
        System.out.println(way);
        Payway result=billService.addPayway(way,session);
        if (result!=null){
            return ResultUtil.success(result);
        }else {
            return ResultUtil.unSuccess();
        }

    }

    @RequestMapping("/updatePayway")
    public Result updatePayway(Payway way, HttpSession session){
        int res = billService.updatePayway(way);
        if (res>0){
            return ResultUtil.success();
        }else {
            return ResultUtil.unSuccess();
        }

    }


    @RequestMapping("/getPaywaysByWhere/{pageNo}/{pageSize}")
    public Result<Payway> getPaywaysByWhere(Payway payway,@PathVariable int pageNo,
                                          @PathVariable int pageSize, HttpSession session) {
        UserInfo user = Config.getSessionUser(session);
        pageNo=pageNo-1;

        payway.setUserInfo(user);
        if ("".equals(payway.getName())) {
            payway.setName(null);
        }
        if ("".equals(payway.getExtra())) {
            payway.setExtra(null);
        }
        Pageable page = PageRequest.of(pageNo, pageSize);
        Page res= billService.findPaywaysByWhereWithPage(payway,page);
        if (res!=null){
            return ResultUtil.success(res);
        }else {
            return ResultUtil.unSuccess();
        }
    }

    //http://localhost:8003/payway/delPayway

    @RequestMapping("/delPayway")
    public Result delPayway(Payway payway){
        billService.delPayway(payway);
        return ResultUtil.success();
    }


    ///addTag
    @RequestMapping("/addTag")
    public Result addTag(RecordTitle recordTitle, HttpSession session){
        System.out.println(recordTitle);
        int result=billService.addTag(recordTitle,session);
        if (result>0){
            return ResultUtil.success();
        }else {
            return ResultUtil.unSuccess();
        }

    }

    @RequestMapping("/updateTag")
    public Result updateTag(RecordTitle recordTitle, HttpSession session){
        int result=billService.updateTag(recordTitle,session);
        if (result>0){
            return ResultUtil.success();
        }else {
            return ResultUtil.unSuccess();
        }

    }

    @RequestMapping("/getTagsByWhere/{pageNo}/{pageSize}")
    public Result getTagsByWhere(RecordTitle recordTitle, @PathVariable int pageNo,
                                            @PathVariable int pageSize, HttpSession session) {
        pageNo=pageNo-1;

        UserInfo user = Config.getSessionUser(session);
        recordTitle.setUserInfo(user);
        if ("".equals(recordTitle.getTitle())) {
            recordTitle.setTitle(null);
        }
        if ("".equals(recordTitle.getType())) {
            recordTitle.setType(null);
        }
        Pageable page = PageRequest.of(pageNo, pageSize);
        Page res= billService.getTagsByWhere(recordTitle,page);
        if (res!=null){
            return ResultUtil.success(res);
        }else {
            return ResultUtil.unSuccess();
        }
    }


    //delTag
    @RequestMapping("/delTag")
    public Result delTag(RecordTitle recordTitle){
        billService.delRecordTitle(recordTitle);
        return ResultUtil.success();
    }
}
