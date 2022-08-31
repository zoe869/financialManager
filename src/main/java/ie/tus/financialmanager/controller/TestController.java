package ie.tus.financialmanager.controller;

import ie.tus.financialmanager.entity.Payway;
import ie.tus.financialmanager.entity.Privilege;
import ie.tus.financialmanager.entity.UserInfo;
import ie.tus.financialmanager.repositories.PrivilegeRepository;
import ie.tus.financialmanager.repositories.RoleRepository;
import ie.tus.financialmanager.repositories.UserRepository;
import ie.tus.financialmanager.service.BillService;
import ie.tus.financialmanager.util.Config;
import ie.tus.financialmanager.util.Result;
import ie.tus.financialmanager.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TestController {


    @Autowired
    private BillService billService;
    @Autowired
    private PrivilegeRepository privilegeRepository;

    @RequestMapping("/getAllPrivilege/{roleId}")
    @ResponseBody
    public Result getAllPrivilege(@PathVariable("roleId") Integer roleid){

        List<Privilege> privileges = privilegeRepository.findAll();

        if (privileges.size()>0){
            return ResultUtil.success(privileges);
        }else {
            return ResultUtil.unSuccess("no privileges found");
        }
    }

}
