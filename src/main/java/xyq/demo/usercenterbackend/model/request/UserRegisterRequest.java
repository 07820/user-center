package xyq.demo.usercenterbackend.model.request;


import lombok.Data;

import java.io.Serializable;

//用户注册请求体
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 1L;
//    在你的 UserRegisterRequest 类中，serialVersionUID 被显式设置为 1L，这意味着你将类的序列化版本固定在 1。
//    如果你在未来对这个类进行了修改，而不改变 serialVersionUID，那么仍然可以反序列化旧版本的数据，不会因为类的结构变化而抛出异常。

    private String userAccount;
    private String userPassword;
    private String checkPassword;

}
