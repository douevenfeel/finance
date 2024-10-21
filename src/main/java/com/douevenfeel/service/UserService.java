package com.douevenfeel.service;

import com.douevenfeel.model.Limit;
import com.douevenfeel.model.Operation;
import com.douevenfeel.model.OperationType;
import com.douevenfeel.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static final String PATH = "src/main/resources/users.json";
    private static ArrayList<User> users = new ArrayList<>();
    private static User currentUser;

    public static void load() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(PATH);
        if (!file.exists()) throw new Exception("База данных не найдена");
        users = objectMapper.readValue(file, new TypeReference<>() {
        });
        System.out.println("База данных загружена");
    }

    public static void save() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(PATH);
            String data = objectMapper.writeValueAsString(users);
            Files.writeString(Paths.get(file.toURI()), data);
            System.out.println("Сохранение базы данных");
        } catch (Exception e) {
            System.out.println("Ошибка сохранения базы данных");
            System.out.println(e.getMessage());
        }
    }

    public static boolean register(String username, String password) throws Exception {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                throw new Exception("Пользователь с таким именем уже существует");
            }
        }
        String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(username, hashPassword);
        users.add(user);
        currentUser = user;
        System.out.println("Зарегистрирован пользователь " + user.getUsername());
        return true;
    }

    public static boolean login(String username, String password) throws Exception {
        if (currentUser != null) throw new Exception("Вход не выполнен");
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (BCrypt.checkpw(password, user.getPassword())) {
                    currentUser = user;
                    System.out.println("Вход выполнен " + currentUser.getUsername());
                    return true;
                } else {
                    throw new Exception("Неверный пароль");
                }
            }
        }
        throw new Exception("Пользователь не найден");
    }

    public static boolean logout() throws Exception {
        if (currentUser == null) throw new Exception("Вход не выполнен");
        currentUser = null;
        System.out.println("Выход выполнен");
        return true;
    }

    public static void checkLogin() {
        if (currentUser != null)
            System.out.println("Текущий пользователь " + currentUser.getUsername());
        else
            System.out.println("Вход не выполнен");
    }

    public static void addLimit(String name, double limit) throws Exception {
        if (currentUser == null) throw new Exception("Вход не выполнен");
        Limit newLimit = new Limit(name, limit);
        for (Limit l : currentUser.getWallet().getLimits()) {
            if (l.getName().equals(name)) {
                System.out.println("Лимит " + name + " " + limit + " обновлен");
                l.setLimit(limit);
                return;
            }
        }
        currentUser.getWallet().addLimit(newLimit);
        System.out.println("Лимит " + name + " " + limit + " добавлен");
    }

    public static void removeLimit(String name) throws Exception {
        if (currentUser == null) throw new Exception("Вход не выполнен");
        for (Limit l : currentUser.getWallet().getLimits()) {
            if (l.getName().equals(name)) {
                System.out.println("Лимит " + name + " " + l.getLimit() + " удален");
                currentUser.getWallet().removeLimit(l);
                return;
            }
        }
        throw new Exception("Лимит не найден");
    }

    public static void getLimits() throws Exception {
        if (currentUser == null) throw new Exception("Вход не выполнен");
        if (currentUser.getWallet().getLimits().isEmpty()) System.out.println("Лимиты не установлены");
        else {
            System.out.println("Текущие лимиты:");
            for (Limit l : currentUser.getWallet().getLimits())
                System.out.println("\t" + l);
        }
    }

    public static void addIncome(String name, double amount) throws Exception {
        if (currentUser == null) throw new Exception("Вход не выполнен");
        Operation operation = new Operation(name, amount, OperationType.INCOME);
        currentUser.getWallet().addOperation(operation);
        System.out.println("Поступление " + name + " " + amount + " добавлено");
    }

    public static void addOutcome(String name, double amount) throws Exception {
        if (currentUser == null) throw new Exception("Вход не выполнен");
        Operation operation = new Operation(name, amount, OperationType.OUTCOME);
        currentUser.getWallet().addOperation(operation);
        System.out.println("Расход " + name + " " + amount + " добавлен");
    }

    public static void getInfo() throws Exception {
        if (currentUser == null) throw new Exception("Вход не выполнен");
        System.out.println("Информация о пользователе " + currentUser.getUsername() + ":");
        System.out.println("Текущий баланс " + currentUser.getWallet().getBalance());
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

        if (incomeMap.isEmpty()) System.out.println("Нет поступлений");
        else {
            System.out.println("Все поступления " + income);
            for (Map.Entry<String, Double> entry : incomeMap.entrySet()) {
                System.out.println("\t" + entry.getKey() + " " + entry.getValue());
            }
        }
        if (outcomeMap.isEmpty()) System.out.println("Нет расходов");
        else {
            System.out.println("Все траты " + outcome);
            for (Map.Entry<String, Double> entry : outcomeMap.entrySet()) {
                System.out.println("\t" + entry.getKey() + " " + entry.getValue());
            }
        }
        if (currentUser.getWallet().getLimits().isEmpty()) System.out.println("Нет лимитов");
        else {
            System.out.println("Текущие лимиты:");
            for (Limit l : currentUser.getWallet().getLimits()) {
                System.out.println("\t" + l.getName() + " " + outcomeMap.getOrDefault(l.getName(), 0.0) + "/" + l.getLimit());
            }
        }
    }
}
