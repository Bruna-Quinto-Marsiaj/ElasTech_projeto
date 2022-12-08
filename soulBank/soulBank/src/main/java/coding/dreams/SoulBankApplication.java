package coding.dreams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class  SoulBankApplication {

	//@SpringBootApplication
	public static void main(String[] args) {

		SpringApplication.run(SoulBankApplication.class, args);

	}

}
