package com.douevenfeel.model;

public class User {
    private String username;
    private String password;
    private Wallet wallet;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.wallet = new Wallet();
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", wallet=" + wallet + '}';
    }
}
