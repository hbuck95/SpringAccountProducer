package com.bae.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bae.models.Account;
import com.bae.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository repo;

	@Override
	public List<Account> getAccounts() {
		return repo.findAll();
	}

	@Override
	public Account getAccount(Long id) {
		Optional<Account> account = repo.findById(id);
		return account.orElse(new Account());
	}

	@Override
	public Account addAccount(Account account) {
		return repo.save(account);
	}

	@Override
	public ResponseEntity<Object> deleteAccount(Long id) {
		if (accountExists(id)) {
			repo.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	@Override
	public ResponseEntity<Object> updateAccount(Account account, Long id) {
		if (accountExists(id)) {
			account.setId(id);
			repo.save(account);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	private boolean accountExists(Long id) {
		Optional<Account> accountOptional = repo.findById(id);
		return accountOptional.isPresent();
	}

}