package com.douevenfeel.model;

public record Operation(String name, double amount, OperationType type) {

    @Override
    public String toString() {
        return name + " " + amount + " " + type;
    }


}
