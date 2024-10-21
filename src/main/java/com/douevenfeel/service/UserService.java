package com.douevenfeel.service;

import com.douevenfeel.model.Limit;
import com.douevenfeel.model.Operation;
import com.douevenfeel.model.OperationType;
import com.douevenfeel.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static final ArrayList<User> users = new ArrayList<>();
    private static User currentUser;

    public static void load() {
        System.out.println("Загрузка базы данных");
    }

    public static void save() {
        System.out.println("Сохранение базы данных");
    }

    public static boolean register(String username, String password) throws Exception {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                throw new Exception("Username already exists");
            }
        }
        String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(username, hashPassword);
        users.add(user);
        currentUser = user;
        System.out.println("Registered as " + user.getUsername());
        return true;
    }

    public static boolean login(String username, String password) throws Exception {
        if (currentUser != null) throw new Exception("Already logged in");
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (BCrypt.checkpw(password, user.getPassword())) {
                    currentUser = user;
                    System.out.println("Logged in as " + currentUser.getUsername());
                    return true;
                } else {
                    throw new Exception("Incorrect password");
                }
            }
        }
        throw new Exception("Username does not exist");
    }

    public static boolean logout() throws Exception {
        if (currentUser == null) throw new Exception("Not logged in");
        currentUser = null;
        System.out.println("Logged out");
        return true;
    }

    public static void checkLogin() {
        if (currentUser != null)
            System.out.println("Logged in as " + currentUser.getUsername());
        else
            System.out.println("Not logged in");
    }

    public static void addLimit(String name, double limit) throws Exception {
        Limit newLimit = new Limit(name, limit);
        if (currentUser == null) throw new Exception("Not logged in");
        currentUser.getWallet().addLimit(newLimit);
        System.out.println("Limit " + name + " " + limit + " added");
    }

    public static void removeLimit(String name) throws Exception {
        if (currentUser == null) throw new Exception("Not logged in");
        for (Limit l : currentUser.getWallet().getLimits()) {
            if (l.getName().equals(name)) {
                System.out.println("Limit " + name + " " + l.getLimit() + " removed");
                currentUser.getWallet().removeLimit(l);
                return;
            }
        }
        throw new Exception("Limit does not exist");
    }

    public static void getLimits() throws Exception {
        if (currentUser == null) throw new Exception("Not logged in");
        System.out.println("Current limits:");
        for (Limit l : currentUser.getWallet().getLimits())
            System.out.println("\t" + l);
    }

    public static void addIncome(String name, double amount) throws Exception {
        if (currentUser == null) throw new Exception("Not logged in");
        Operation operation = new Operation(name, amount, OperationType.INCOME);
        currentUser.getWallet().addOperation(operation);
        System.out.println("Income " + name + " " + amount + " added");
    }

    public static void addOutcome(String name, double amount) throws Exception {
        if (currentUser == null) throw new Exception("Not logged in");
        Operation operation = new Operation(name, amount, OperationType.OUTCOME);
        currentUser.getWallet().addOperation(operation);
        System.out.println("Outcome " + name + " " + amount + " added");
    }

    public static void getInfo() throws Exception {
        if (currentUser == null) throw new Exception("Not logged in");
        System.out.println("User " + currentUser.getUsername() + "'s info:");
        System.out.println("Current balance " + currentUser.getWallet().getBalance());
        Map<String, Double> incomeMap = new HashMap<>();
        Map<String, Double> outcomeMap = new HashMap<>();
        for (Operation op : currentUser.getWallet().getOperations()) {
            if (op.type() == OperationType.INCOME)
                incomeMap.put(op.name(), incomeMap.getOrDefault(op.name(), 0.0) + op.amount());
            else if (op.type() == OperationType.OUTCOME)
                outcomeMap.put(op.name(), outcomeMap.getOrDefault(op.name(), 0.0) + op.amount());
        }
        double income = 0;
        for (Double i : incomeMap.values()) income += i;
        double outcome = 0;
        for (Double i : outcomeMap.values()) outcome += i;

        System.out.println("All incomes " + income);
        for (Map.Entry<String, Double> entry : incomeMap.entrySet()) {
            System.out.println("\t" + entry.getKey() + " " + entry.getValue());
        }
        System.out.println("All outcomes " + outcome);
        for (Map.Entry<String, Double> entry : outcomeMap.entrySet()) {
            System.out.println("\t" + entry.getKey() + " " + entry.getValue());
        }
        System.out.println("Current limits");
        for (Limit l : currentUser.getWallet().getLimits()) {
            System.out.println("\t" + l.getName() + " " + outcomeMap.getOrDefault(l.getName(), 0.0) + "/" + l.getLimit());
        }
    }
}
