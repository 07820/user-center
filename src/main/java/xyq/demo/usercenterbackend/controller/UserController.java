package xyq.demo.usercenterbackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import xyq.demo.usercenterbackend.model.User;
import xyq.demo.usercenterbackend.model.request.UserLoginInRequest;
import xyq.demo.usercenterbackend.model.request.UserRegisterRequest;
import xyq.demo.usercenterbackend.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static xyq.demo.usercenterbackend.constant.UserConstant.ADMIN_ROLE;
import static xyq.demo.usercenterbackend.constant.UserConstant.USER_SESSION_KEY;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterrequest) { //userRegisterrequest是用来存储前端发送json注册信息转换成的对象
                            //转化的注释                //难怪UserRegisterRequest要定义在model包下

        //如果请求为空，返回null
        if (userRegisterrequest == null) {
            return null;
        }
        //暂时寄存一下userRegisterrequest的信息
        String userAccount = userRegisterrequest.getUserAccount();
        String userPassword = userRegisterrequest.getUserPassword();
        String checkPassword = userRegisterrequest.getCheckPassword();

        //检查是否有空值
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }

        //没问题就传给service层
        long id = userService.userRegister(userAccount, userPassword, checkPassword);
        return id;
    }


    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginInRequest userLoginInRequest, HttpServletRequest request) {

        //如果请求为空，返回null
        if (userLoginInRequest == null) {
            return null;
        }
        //取出其中的内容再次检查非空
        //暂时寄存一下userLoginInRequest中的信息，用于初步检查并传递给service层
        String userAccount = userLoginInRequest.getUserAccount();
        String userPassword = userLoginInRequest.getUserPassword();

        //检查信息是否有空值
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;  //注意返回值要求是User
        }

        return userService.userLogin(userAccount, userPassword, request);
    }


    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request) {
       //如果不是管理员，返回空列表
        if (!isAdmin(request)) {
            return new ArrayList<>();
        }
        //如果是管理员，就查找用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }

        //return userService.list(queryWrapper);
        //将查找到的用户列表，转换成安全用户列表
        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSafetyUser(user)).toList();

    }
    //
    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request) { //原来删除的返回值全部都是boolean类型
        //如果不是管理员，返回false
        if (!isAdmin(request)) {
            return false;
        }
        if (id <= 0) {
            return false;
        }
        //如果是管理员，就删除用户
        return userService.removeById(id);
    }


    //是否为管理员？
    private boolean isAdmin(HttpServletRequest request) {
        // 获取当前会话对象中的用户信息，注意返回值是给Object类型！
        Object userObj = request.getSession().getAttribute(USER_SESSION_KEY);
        // 将获取到的对象转换为 User 类型
        User user = (User) userObj;
        // 检查用户是否存在以及用户角色是否为管理员
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

}

