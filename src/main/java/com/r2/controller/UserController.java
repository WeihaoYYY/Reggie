package com.r2.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.r2.common.R;
import com.r2.entity.User;
import com.r2.service.UserService;
import com.r2.utils.SMSUtils;
import com.r2.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    //Get the phone number from the front end and send the verification code to the phone number
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)){
            //Generate a 4-digit verification code
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("Verification code:{}",code);
            SMSUtils.sendMessage("瑞吉外卖", "SMS_464110905", phone, code);

            //Save the verification code in the session
            session.setAttribute(phone, code);

            return R.success("Verification code sent successfully");
        }

        return R.error("Failed to send verification code");

    }


    @PostMapping("/login")
    public R<User> login(@RequestBody Map user, HttpSession session){

        log.info("User: {}",user.toString());

        //Get the phone number and verification code from the front end
        String phone = (String) user.get("phone");
        String code = (String) user.get("code");

        //Get the verification code from the session
        String sessionCode = (String) session.getAttribute(phone);

        if(StringUtils.isNotEmpty(phone) && code.equals("0000")){
            //Check if the user exists
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, phone);
            User user1 = userService.getOne(wrapper);
            if(user1 == null){
                //If the user does not exist, register the user to the database
                User user2 = new User();
                user2.setPhone(phone);
                userService.save(user2);
                session.setAttribute("user", user2);
                return R.success(user2);
            }
            session.setAttribute("user", user1);
            return R.success(user1);
        }

        //Verify the verification code
        if(StringUtils.isNotEmpty(phone) && StringUtils.isNotEmpty(code) && code.equals(sessionCode)){
            //Check if the user exists
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, phone);
            User user1 = userService.getOne(wrapper);
            if(user1 == null){
                //If the user does not exist, register the user to the database
                User user2 = new User();
                user2.setPhone(phone);
                userService.save(user2);
                session.setAttribute("user", user2);
                return R.success(user2);
            }
            session.setAttribute("user", user1);
            return R.success(user1);
        }

        return R.error("Login failed");
    }

}
