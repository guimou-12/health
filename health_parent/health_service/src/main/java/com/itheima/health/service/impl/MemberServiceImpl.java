package com.itheima.health.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: MemberServiceImpl$
 * @Author Admin
 * @Date: 2020-10-30$ 21:16$
 * @Version 1.0
 */
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    /** 功能描述:
    通过手机号码查会员
    * @return: com.itheima.health.pojo.Member
    * @Author: admin
    * @Date: 2020-10-30 21:20
    */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /** 功能描述:
    新增会员
    * @return: void
    * @Author: admin
    * @Date: 2020-10-30 21:20
    */
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }
}
