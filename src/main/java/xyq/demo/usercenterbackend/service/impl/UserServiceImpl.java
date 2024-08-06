package xyq.demo.usercenterbackend.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.springframework.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import xyq.demo.usercenterbackend.model.User;
import xyq.demo.usercenterbackend.service.UserService;
import xyq.demo.usercenterbackend.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
* @author 徐岩奇
* @description 针对表【user(user)】的数据库操作Service实现
* @createDate 2024-08-05 09:55:10
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;


    private static final String SALT = "XYQ";
    //用户登录态键
    private static final String USER_SESSION_KEY ="userSessionKey";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验
//        if(userAccount == null || userPassword == null || checkPassword == null){
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {

            return -1;
        }
        if (userAccount.length() < 4) {
            return -1;
        }

        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }


        //
        String validPattern = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }


        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

        //用户不重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        //long count = this.count(queryWrapper);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }


//        //加密

//        MessageDigest md5 = MessageDigest.getInstance("MD5");

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());


        //all pass,then save
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);

        boolean saveResult = this.save(user);

        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User doLogin(String userAccount, String userPassword, String checkPassword, HttpServletRequest request) {


            //1.校验
//        if(userAccount == null || userPassword == null || checkPassword == null){
            if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {

                return null;
            }
            if (userAccount.length() < 4) {
                return null;
            }

            if (userPassword.length() < 8 || checkPassword.length() < 8) {
                return null;
            }


            //
            String validPattern = "\\pP|\\pS|\\s+";
            Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
            if (matcher.find()) {
                return null;
            }

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            //用户存在吗
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);


      if(user==null) {
          log.info("user log in failed");
          return null;  //等会改成自定义异常
      }


//用户脱敏,模仿DTO类，避免敏感数据返回给前端
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserName(user.getUserName());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
//        safetyUser.setUserPassword("");
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
//        safetyUser.setUpdateTime(new Date());
//        safetyUser.setIsDelete(0);

        //记录用户登录态！
      request.getSession().setAttribute(USER_SESSION_KEY,user);

        return safetyUser;
        }
    }




