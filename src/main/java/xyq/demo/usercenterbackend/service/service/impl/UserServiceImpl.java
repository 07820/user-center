package xyq.demo.usercenterbackend.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyq.demo.usercenterbackend.model.User;
import xyq.demo.usercenterbackend.service.service.UserService;
import xyq.demo.usercenterbackend.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐岩奇
* @description 针对表【user(user)】的数据库操作Service实现
* @createDate 2024-08-05 09:55:10
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




