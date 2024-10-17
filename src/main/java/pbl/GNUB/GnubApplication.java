package pbl.GNUB;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class GnubApplication {

	public static void main(String[] args) {
		SpringApplication.run(GnubApplication.class, args);
	}

}