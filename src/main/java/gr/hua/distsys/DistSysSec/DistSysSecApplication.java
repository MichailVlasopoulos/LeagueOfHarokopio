package gr.hua.distsys.DistSysSec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;

@SpringBootApplication
public class DistSysSecApplication {
	public static void main(String[] args) {
		SpringApplication.run(DistSysSecApplication.class, args);
	}
}
