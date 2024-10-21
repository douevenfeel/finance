package com.douevenfeel.model;

public class Limit {
    private final String name;
    private double limit;

    public Limit(String name, double limit) {
        this.name = name;
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", limit: " + limit;
    }
}
