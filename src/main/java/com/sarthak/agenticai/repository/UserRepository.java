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
    'Test',
    COUNT(u)
)
FROM User u
""")
    List<CustomerGrowthDto> getCustomerGrowthAnalytics();}