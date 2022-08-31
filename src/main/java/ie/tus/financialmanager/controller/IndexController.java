package ie.tus.financialmanager.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @RequestMapping("/pages/{page}")  // 为了实现动态跳转
    public String toPage(@PathVariable String page){
        return page.replace("_","/");
    }
}
