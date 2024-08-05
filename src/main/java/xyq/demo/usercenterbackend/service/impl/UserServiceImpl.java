package xyq.demo.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.springframework.util.StringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import xyq.demo.usercenterbackend.model.User;
import xyq.demo.usercenterbackend.service.UserService;
import xyq.demo.usercenterbackend.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
* @author 徐岩奇
* @description 针对表【user(user)】的数据库操作Service实现
* @createDate 2024-08-05 09:55:10
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验
//        if(userAccount == null || userPassword == null || checkPassword == null){
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){

             return -1;
        }
        if(userAccount.length() < 4 ){
            return -1;
        }

        if(userPassword.length() < 8|| checkPassword.length() < 8){
            return -1;
        }



        String validPattern = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }


        if(!userPassword.equals(checkPassword)){
            return -1;
        }

//用户不重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if(count > 0) {
            return -1;
        }


//        //加密
//        MessageDigest md5 = MessageDigest.getInstance("MD5");
//         md5.digest("abcd");


        return 0;
    }

}


