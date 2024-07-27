package com.learn.repository;

import com.learn.entity.Accounts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository  extends JpaRepository<Accounts,Long> {


    Optional<Accounts> findByCustomerId(Long aLong);


    /*Note -here we are deleting customer Account record  by cusomertId (that is not primary key ).if we will
    want to delete with account info by AccountId(Primary key)
    then no need to create any method because jpa already have deleteById() option for primary key.
     */
    @Transactional
    //@Modifying
    void deleteByCustomerId(Long customerId);
}
