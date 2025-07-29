package solutions.york.budgetbookbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.york.budgetbookbackend.model.Account;
import solutions.york.budgetbookbackend.model.Customer;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomer(Customer customer);
}
