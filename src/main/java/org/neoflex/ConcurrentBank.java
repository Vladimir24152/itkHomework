package org.neoflex;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentBank {

    private Map<Integer, BankAccount> bankAccountMap = new ConcurrentHashMap<>();
    private AtomicInteger idCount = new AtomicInteger(0);

    public BankAccount createAccount(Integer amount){
        BankAccount account = new BankAccount(idCount.addAndGet(1), amount);
        bankAccountMap.put(idCount.get(), account);
        return account;
    }

    public void transfer(BankAccount accountFrom, BankAccount accountTo, Integer amount){
        if (amount <= 0) throw new IllegalArgumentException("Сумма должна быть положительной");
        if (accountFrom.equals(accountTo)) throw new IllegalArgumentException("Нельзя совершить перевод в рамках одного и того же счета");

        BankAccount account1 = selectBankAccountWithLittleId(accountFrom, accountTo);
        BankAccount account2 = selectBankAccountWithBigId(accountFrom, accountTo);

        synchronized (account1){
            synchronized (account2){
                accountFrom.withdraw(amount);
                accountTo.deposit(amount);
            }
        }

    }

    private BankAccount selectBankAccountWithLittleId(BankAccount account1, BankAccount account2){
        return account1.getId() > account2.getId() ? account2 : account1;
    }

    private BankAccount selectBankAccountWithBigId(BankAccount account1, BankAccount account2){
        return account1.getId() < account2.getId() ? account2 : account1;
    }

    public int getTotalBalance() {
        int total = 0;
        for (BankAccount a : bankAccountMap.values()) total += a.getBalance();
        return total;
    }
}
