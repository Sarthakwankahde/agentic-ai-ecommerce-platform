package com.sarthak.agenticai.repository;

import com.sarthak.agenticai.dto.CustomerGrowthDto;
import com.sarthak.agenticai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    long count();
    @Query("""
SELECT new com.sarthak.agenticai.dto.CustomerGrowthDto(
    FUNCTION('TO_CHAR', u.createdAt, 'Mon'),
    COUNT(u.id)
)
FROM User u
GROUP BY FUNCTION('TO_CHAR', u.createdAt, 'Mon'),
         FUNCTION('MONTH', u.createdAt)
ORDER BY FUNCTION('MONTH', u.createdAt)
""")
    List<CustomerGrowthDto> getCustomerGrowthAnalytics();
}