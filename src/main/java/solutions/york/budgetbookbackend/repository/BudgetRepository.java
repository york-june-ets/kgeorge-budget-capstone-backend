package solutions.york.budgetbookbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.york.budgetbookbackend.model.Budget;
import solutions.york.budgetbookbackend.model.Category;
import solutions.york.budgetbookbackend.model.Customer;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    public List<Budget> findByCustomer(Customer customer);
    public Optional<Budget> findByCategory(Category category);
}
