package xyq.demo.usercenterbackend.model.request;


import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import java.io.Serializable;

//用户注册请求体
@Data
public class UserLoginInRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userAccount;
    private String userPassword;
//    private HttpServletRequest request;


}
