package org.sid.ebakingbackend.repositories;

import org.sid.ebakingbackend.entities.BankAccount;
import org.sid.ebakingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
