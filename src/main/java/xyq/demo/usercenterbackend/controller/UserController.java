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

import static xyq.demo.usercenterbackend.constant.UserConstant.ADMIN_ROLE;
import static xyq.demo.usercenterbackend.constant.UserConstant.USER_SESSION_KEY;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterrequest) {
        //1.校验

        if (userRegisterrequest == null) {
            return null;
        }

        String userAccount = userRegisterrequest.getUserAccount();
        String userPassword = userRegisterrequest.getUserPassword();
        String checkPassword = userRegisterrequest.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }


        long id = userService.userRegister(userAccount, userPassword, checkPassword);
        return id;
    }


    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginInRequest userLoginInRequest, HttpServletRequest request) {
        //1.校验

        if (userLoginInRequest == null) {
            return null;
        }


        String userAccount = userLoginInRequest.getUserAccount();
        String userPassword = userLoginInRequest.getUserPassword();


        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request) {

        if (!isAdmin(request)) {
            return new ArrayList<>();

        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }


        return userService.list(queryWrapper);

    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return false;

        }

        if (id <= 0) {

            return false;

        }

        return userService.removeById(id);

    }

    //是否为管理员？
    private boolean isAdmin(HttpServletRequest request) {

        Object userObj = request.getSession().getAttribute(USER_SESSION_KEY);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

}

