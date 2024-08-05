package xyq.demo.usercenterbackend.service;

import xyq.demo.usercenterbackend.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 徐岩奇
* @description 针对表【user(user)】的数据库操作Service
* @createDate 2024-08-05 09:55:10
*/
public interface UserService extends IService<User> {



   long userRegister(String userAccount, String userPassword,String checkPassword);


}
