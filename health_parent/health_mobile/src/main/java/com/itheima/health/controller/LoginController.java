package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @Description: LoginController$
 * @Author Admin
 * @Date: 2020-10-30$ 21:01$
 * @Version 1.0
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;
    @PostMapping("/check")
    public Result checkMember(@RequestBody Map<String,String> loginInfo, HttpServletResponse res){
        String telephone = loginInfo.get("telephone");
        String validateCode = loginInfo.get("validateCode");

        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        Jedis jedis = jedisPool.getResource();

        String codeInRedis = jedis.get(key);
        if(null == codeInRedis){
            return new Result(false,"请点击重新发送验证码");

        }
        if(!codeInRedis.equals(validateCode)){
            return new Result(false,"验证码输入错误");
        }
        jedis.del(key);

        Member member = memberService.findByTelephone(telephone);
        if(null == member){
            member = new Member();
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            member.setRemark("手机快速注册");
            memberService.add(member);
        }
        //跟踪记录的手机号码，代表会员
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setMaxAge(30*24*60*60);
        cookie.setPath("/");//网站的所有路径 都会发送这个cookie
        res.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
