package xyq.demo.usercenterbackend.service.impl;

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
@Slf4j                                             // mapper代码是谁， 存储的类对象是谁
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    //扰乱的盐值
    private static final String SALT = "XYQ";
    //用户登录态键
    public static final String USER_SESSION_KEY = "userSessionKey";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {

        // if(userAccount == null || userPassword == null || checkPassword == null){等效
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }

        if (userAccount.length() < 4) {
            return -1;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }

        //检测特殊字符
        String validPattern = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        //两次密码一致吗
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

        //用户重复吗
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);

        //long count = this.count(queryWrapper); 原来的写法
        long count = userMapper.selectCount(queryWrapper); //目的是查看是否有重复的用户，count
        if (count > 0) {
            return -1;
        }
        //md5加密                字节数组加密为16进制字符串
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //都通过了，再保存进第二个对象，即新的用户对象。
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);

/*
你的服务实现类 UserServiceImpl 继承了 MyBatis-Plus 提供的 ServiceImpl 类。ServiceImpl 类实现了 IService 接口中的所有基础 CRUD 方法。

ServiceImpl 类中的基础 CRUD 方法，例如 save、getById、list、removeById 等，都是通过调用 Mapper 层来实现的。
这些方法已经在 ServiceImpl 类中实现，你无需在 UserServiceImpl 中再次调用 DAO 层的方法。
*/
        //用于将一个实体对象保存到数据库中。查看保存结果
        boolean saveResult = this.save(user);   //this 指Impl类对象，save是继承的方法，返回值是boolean类型
       //true还是false
        if (!saveResult) {
            return -1;
        }
       //返回用户id
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
                                                                           //login方法需要request对象来记录登录态
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }
        //检测特殊字符
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



        if (user == null) {
            log.info("user log in failed");
            return null;  //等会改成自定义异常
        }

        //使用定义的方法来脱敏

        User safetyUser = getSafetyUser(user);

        //记录用户登录态！存储的是完整的信息！
        request.getSession().setAttribute(USER_SESSION_KEY, user);
        //记录完就返回脱敏后的用户信息
        return safetyUser;
    }


    //脱敏
    @Override
    public User getSafetyUser(User OriginUser) {
        User safetyUser = new User();
        safetyUser.setId(OriginUser.getId());
        safetyUser.setUserName(OriginUser.getUserName());
        safetyUser.setUserAccount(OriginUser.getUserAccount());
        safetyUser.setAvatarUrl(OriginUser.getAvatarUrl());
        safetyUser.setGender(OriginUser.getGender());
        safetyUser.setPhone(OriginUser.getPhone());
        safetyUser.setEmail(OriginUser.getEmail());
        safetyUser.setUserStatus(OriginUser.getUserStatus());
        safetyUser.setCreateTime(OriginUser.getCreateTime());
        safetyUser.setUserRole(OriginUser.getUserRole());
        return safetyUser;
    }

}




