package solutions.york.budgetbookbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.york.budgetbookbackend.model.Category;
import solutions.york.budgetbookbackend.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCustomerAndName(Customer customer, String name);
    List<Category> findByCustomer(Customer customer);
}
