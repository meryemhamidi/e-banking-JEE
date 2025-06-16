package org.sid.ebakingbackend;

import org.sid.ebakingbackend.dtos.BankAccountDTO;
import org.sid.ebakingbackend.dtos.CurrentBankAccountDTO;
import org.sid.ebakingbackend.dtos.CustomerDTO;
import org.sid.ebakingbackend.dtos.SavingBankAccountDTO;
import org.sid.ebakingbackend.entities.*;
import org.sid.ebakingbackend.enums.AccountStatus;
import org.sid.ebakingbackend.enums.OperationType;
import org.sid.ebakingbackend.exceptions.BalanceNotSufficientException;
import org.sid.ebakingbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebakingbackend.exceptions.CustomerNotFoundException;
import org.sid.ebakingbackend.repositories.AccountOperationRepository;
import org.sid.ebakingbackend.repositories.BankAccountRepository;
import org.sid.ebakingbackend.repositories.CustomerRepository;
import org.sid.ebakingbackend.services.BankAccountService;
import org.sid.ebakingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbakingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbakingBackendApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            if (bankAccountService.listCustomers().isEmpty()) {
                Stream.of("Hassan","Imane","Mohamed").forEach(name -> {
                    CustomerDTO customer = new CustomerDTO();
                    customer.setName(name);
                    customer.setEmail(name + "@gmail.com");
                    bankAccountService.saveCustomer(customer);
                });

                bankAccountService.listCustomers().forEach(customer -> {
                    try {
                        bankAccountService.saveCurrentBankAccount(Math.random() * 90000, 9000, customer.getId());
                        bankAccountService.saveSavingBankAccount(Math.random() * 120000, 5.5, customer.getId());
                    } catch (CustomerNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
        };
    }
    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("Hassan","Yassine","Aicha").forEach(name->{
                Customer customer=new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount=new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreateAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount=new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreateAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);

            });
            bankAccountRepository.findAll().forEach(acc->{
                for (int i = 0; i <10 ; i++) {
                    AccountOperation accountOperation=new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }

            });
        };

    }

}