package xyq.demo.usercenterbackend.controller;


import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyq.demo.usercenterbackend.model.User;
import xyq.demo.usercenterbackend.model.request.UserLoginInRequest;
import xyq.demo.usercenterbackend.model.request.UserRegisterRequest;
import xyq.demo.usercenterbackend.service.UserService;

@RestController
@RequestMapping ("/user")
public class UserController {

   @Resource
   private UserService userService;

    @PostMapping("/register")
     public Long userRegister(@RequestBody UserRegisterRequest userRegisterrequest) {
    //1.校验

        if(userRegisterrequest == null){
            return null;
        }


        String userAccount = userRegisterrequest.getUserAccount();
        String userPassword = userRegisterrequest.getUserPassword();
        String checkPassword = userRegisterrequest.getCheckPassword();

if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return null;
        }


        long id = userService.userRegister(userAccount, userPassword, checkPassword);
             return id;
    }


    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginInRequest userLoginInRequest,HttpServletRequest request) {
        //1.校验

        if(userLoginInRequest == null){
            return null;
        }


        String userAccount = userLoginInRequest.getUserAccount();
        String userPassword = userLoginInRequest.getUserPassword();


        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }

        return userService.userLogin(userAccount, userPassword, request);
    }


}
