package com.bae.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bae.models.Account;

@Repository
public interface AccountRepository extends MongoRepository<Account, Long> {
}