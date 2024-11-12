package pbl.GNUB;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableBatchProcessing // Spring Batch 활성화
@SpringBootApplication(exclude = SecurityAutoConfiguration.class) // security config 로그인 화면 없앰
public class GnubApplication {

    public static void main(String[] args) {
        SpringApplication.run(GnubApplication.class, args);
    }


}