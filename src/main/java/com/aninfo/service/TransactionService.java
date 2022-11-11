package com.aninfo.service;

import com.aninfo.exceptions.DepositNegativeSumException;
import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.exceptions.InvalidTransactionTypeException;
import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.repository.AccountRepository;
import com.aninfo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Transaction createTransaction(Transaction transaction) {

        double amount = transaction.getAmount();

        if (amount <= 0){
            throw new DepositNegativeSumException("Cannot deposit negative sums");
        }

        long associatedCbu = transaction.getAssociatedCbu();
        Account account = accountRepository.findAccountByCbu(associatedCbu);

        if (Objects.equals(transaction.getType(), "deposit")){
            account.setBalance(account.getBalance() + amount);
            accountRepository.save(account);
            return transactionRepository.save(transaction);
        }

        if (Objects.equals(transaction.getType(), "withdraw")){

            if (account.getBalance() < amount){
                throw new InsufficientFundsException("Insufficient funds");
            }
            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);
            return transactionRepository.save(transaction);
        }

        throw new InvalidTransactionTypeException("Invalid transaction type");

    }

    public Collection<Transaction> getTransactionsByCbu(Long cbu) {

        Collection<Transaction> allTransactions = transactionRepository.findAll();
        Collection<Transaction> result = new ArrayList<>();

        for (Transaction transaction : allTransactions) {
            if (transaction.getAssociatedCbu() == cbu){
                result.add(transaction);
            }
        }

        return result;

    }

    public Collection<Transaction> getTransactions() {

        return transactionRepository.findAll();
    }


    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);

    }

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public void deleteById(Long id) {
        Transaction transaction = transactionRepository.findTransactionById(id);

        String type = transaction.getType();

        Account account = accountRepository.findAccountByCbu(transaction.getAssociatedCbu());

        if (Objects.equals(type, "deposit")){
            account.setBalance(account.getBalance() - transaction.getAmount());
        }

        if (Objects.equals(type, "withdraw")){
            account.setBalance(account.getBalance() + transaction.getAmount());
        }

        accountRepository.save(account);
        transactionRepository.deleteById(id);
    }

}
