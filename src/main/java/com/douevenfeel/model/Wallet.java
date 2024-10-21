package com.douevenfeel.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Wallet {
    private final List<Operation> operations;
    private final Set<Limit> limits;
    private double balance;

    public Wallet() {
        this.operations = new ArrayList<>();
        this.limits = new HashSet<>();
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public Set<Limit> getLimits() {
        return limits;
    }

    public void addOperation(Operation operation) throws Exception {
        if (operation.type() == OperationType.INCOME) balance += operation.amount();
        else if (balance < operation.amount())
            throw new Exception("Not enough money, you need " + (operation.amount() - balance) + " more");
        else balance -= operation.amount();
        this.operations.add(operation);
    }

    public void addLimit(Limit limit) {
        this.limits.add(limit);
    }

    public void removeLimit(Limit limit) {
        this.limits.remove(limit);
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "operations=" + operations +
                ", limits=" + limits +
                ", balance=" + balance +
                '}';
    }
}
