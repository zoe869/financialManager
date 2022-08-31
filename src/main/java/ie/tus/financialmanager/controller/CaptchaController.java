package ie.tus.financialmanager.controller;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CaptchaController {

    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 算术类型
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100,40);
        captcha.setLen(3);  // 几位数运算，默认是两位
        // captcha.getArithmeticString();  // 获取运算的格式：3+2=？
        // captcha.text();   // 获取运算的结果
        CaptchaUtil.out(captcha,request,response);
    }
}
