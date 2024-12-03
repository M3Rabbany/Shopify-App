package com.enigmacamp.shopify.repository;

import com.enigmacamp.shopify.entity.Customer;
import com.enigmacamp.shopify.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> , JpaSpecificationExecutor<Customer> {

    @Query("SELECT t FROM Transaction t WHERE t.customer.id = :customerId")
    List<Transaction> findByCustomerId(
            @Param("customerId") String customerId
    );

    @Query(
            value = "UPDATE m_customer " +
                    "SET status = :status " +
                    "WHERE id = :id",
            nativeQuery = true
    )
    void updateStatus(String id, Boolean status);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Customer> findFirstById(String id);
}
