package com.sarthak.agenticai.repository;

import com.sarthak.agenticai.entity.Address;
import com.sarthak.agenticai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository
        extends JpaRepository<Address, Long> {

    // Get all addresses of a user
    List<Address> findByUser(User user);

    // Get specific address of a user
    Optional<Address> findByIdAndUser(
            Long addressId,
            User user);

    // Find default address
    Optional<Address> findByUserAndIsDefaultTrue(
            User user);
}