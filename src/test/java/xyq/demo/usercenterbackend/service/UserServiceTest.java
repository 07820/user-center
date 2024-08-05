package xyq.demo.usercenterbackend.service;
import java.util.Date;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyq.demo.usercenterbackend.model.User;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {

  @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();

        user.setUserName("testXYQ");
        user.setUserAccount("123");
        user.setAvatarUrl("https://pic.code-nav.cn/user_avatar/1699010057779388418/thumbnail/yarLXtKW-0.webp");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);


         boolean result= userService.save(user);
        System.out.println(user.getId()); //自动返回装入的id
        assertTrue(result);
    }

    @Test
    void userRegister() {

        String userAccount = "yupi";
        String userPassword = "";
        String checkPassword = "123456";
     long result =  userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        userAccount = "yu";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);


        userAccount = "yupi";
        userPassword = "123456";
        checkPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);



//        userAccount = "yupi";
//        userPassword = "12345678";
//        checkPassword = "12345678";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertTrue(result > 0);

    }
}