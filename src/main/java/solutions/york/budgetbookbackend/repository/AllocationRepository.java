package solutions.york.budgetbookbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.york.budgetbookbackend.model.Allocation;

public interface AllocationRepository extends JpaRepository<Allocation, Long> {
}
