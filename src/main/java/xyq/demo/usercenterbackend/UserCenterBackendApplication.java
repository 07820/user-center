package xyq.demo.usercenterbackend;

import jakarta.annotation.security.RunAs;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@RunWith(SpringRunner.class)
@MapperScan("xyq.demo.usercenterbackend.mapper")
@SpringBootApplication
public class UserCenterBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterBackendApplication.class, args);
    }

}
