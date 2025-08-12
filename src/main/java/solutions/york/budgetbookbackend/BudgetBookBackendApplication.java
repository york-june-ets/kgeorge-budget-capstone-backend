package solutions.york.budgetbookbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BudgetBookBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudgetBookBackendApplication.class, args);
    }

}
