package solutions.york.budgetbookbackend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import solutions.york.budgetbookbackend.model.Auth;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    @Transactional
    void deleteByToken(String token);
}
