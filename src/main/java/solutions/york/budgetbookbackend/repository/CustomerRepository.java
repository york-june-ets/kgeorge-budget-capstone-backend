package solutions.york.budgetbookbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.york.budgetbookbackend.model.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
}
