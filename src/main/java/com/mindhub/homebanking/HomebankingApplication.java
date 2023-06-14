package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return args -> {

			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Juan", "Bili", "jbili@mindhub.com");

			Account account1 = new Account("VIN001", LocalDate.now(), 9000.00);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 3000.00);
			Account account3 = new Account("VIN003", LocalDate.now().plusDays(2), 1000.00);
			Account account4 = new Account("VIN004", LocalDate.now().plusDays(2), 6000.00);

			Transaction transaction1 = new Transaction(TransactionType.CREDITO, 15000.00, LocalDateTime.now(),"Deposit money into account");
			Transaction transaction2 = new Transaction(TransactionType.DEBITO, -8000.00, LocalDateTime.now(), "Payment to Suppliers");
			Transaction transaction3 = new Transaction(TransactionType.CREDITO, 2000.00, LocalDateTime.now(), "Reimbursement");
			Transaction transaction4 = new Transaction(TransactionType.DEBITO, -2500.00, LocalDateTime.now(), "Car insurance");
			Transaction transaction5 = new Transaction(TransactionType.CREDITO, 7500.00, LocalDateTime.now(), "Deposit money into account");
			Transaction transaction6 = new Transaction(TransactionType.DEBITO, -2000.00, LocalDateTime.now(), "Light tax payment");

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account2.addTransaction(transaction5);
			account2.addTransaction(transaction6);

			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);

			clientRepository.save(client1);
			clientRepository.save(client2);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
		};
	}

}
