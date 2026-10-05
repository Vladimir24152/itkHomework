package org.neoflex;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class BankAccount {
    private final int id;
    private AtomicInteger balance;

    public BankAccount(Integer id, Integer amount) {
        this.id = id;
        this.balance = new AtomicInteger(amount);
    }

    public void deposit(Integer amount){
        if (amount <= 0) throw new IllegalArgumentException("Сумма должна быть положительной");
        balance.getAndAdd(amount);
    }

    public void withdraw(Integer amount){
        if (amount <= 0) throw new IllegalArgumentException("Сумма должна быть положительной");
        balance.updateAndGet(current -> {
            if (current < amount) {
                throw new IllegalStateException("На счете недостаточно средств");
            }
            return current - amount;
        });
    }

    public Integer getBalance(){
        return balance.get();
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        BankAccount that = (BankAccount) object;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
