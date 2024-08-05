package xyq.demo.usercenterbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class UserCenterBackendApplicationTests {


//    @Test
//    void testDigest() throws NoSuchAlgorithmException {
//
//        MessageDigest md5 = MessageDigest.getInstance("MD5");
//        byte[] bytes= md5.digest("abcd".getBytes(StandardCharsets.UTF_8));
////            String.valueof(bytes);
//        String result = new String(bytes);
//        System.out.println(result);
//    }

    @Test
    void testDigest() throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String newPassword = DigestUtils.md5DigestAsHex(("abcd" + "mypassword").getBytes());
        System.out.println(newPassword);
    }



    @Test
    void contextLoads() {
    }

}
