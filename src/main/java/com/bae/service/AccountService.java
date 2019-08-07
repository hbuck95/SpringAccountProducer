package com.bae.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.bae.models.Account;

public interface AccountService {

	List<Account> getAccounts();

	Account getAccount(Long id);

	Account addAccount(Account account);

	ResponseEntity<Object> deleteAccount(Long id);

	ResponseEntity<Object> updateAccount(Account account, Long id);
}