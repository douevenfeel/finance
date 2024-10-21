package com.douevenfeel;

import com.douevenfeel.service.UserService;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {
            UserService.load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Scanner scanner = new Scanner(System.in);
        String command;

        while (true) {
            printMenu();
            command = scanner.nextLine();

            try {
                switch (command) {
                    case "/register":
                        System.out.print("Введите имя пользователя: ");
                        String usernameRegister = scanner.nextLine();
                        System.out.print("Введите пароль: ");
                        String passwordRegister = scanner.nextLine();
                        UserService.register(usernameRegister, passwordRegister);
                        break;
                    case "/login":
                        System.out.print("Введите имя пользователя: ");
                        String usernameLogin = scanner.nextLine();
                        System.out.print("Введите пароль: ");
                        String passwordLogin = scanner.nextLine();
                        UserService.login(usernameLogin, passwordLogin);
                        break;

                    case "/logout":
                        UserService.logout();
                        break;
                    case "/checkLogin":
                        UserService.checkLogin();
                        break;

                    case "/addLimit":
                        System.out.print("Введите категорию: ");
                        String categoryLimit = scanner.nextLine();
                        System.out.print("Введите сумму лимита: ");
                        double limitAmount = Double.parseDouble(scanner.nextLine());
                        UserService.addLimit(categoryLimit, limitAmount);
                        break;

                    case "/addIncome":
                        System.out.print("Введите категорию дохода: ");
                        String categoryIncome = scanner.nextLine();
                        System.out.print("Введите сумму дохода: ");
                        double incomeAmount = Double.parseDouble(scanner.nextLine());
                        UserService.addIncome(categoryIncome, incomeAmount);
                        break;

                    case "/addOutcome":
                        System.out.print("Введите категорию расхода: ");
                        String categoryOutcome = scanner.nextLine();
                        System.out.print("Введите сумму расхода: ");
                        double outcomeAmount = Double.parseDouble(scanner.nextLine());
                        UserService.addOutcome(categoryOutcome, outcomeAmount);
                        break;

                    case "/getLimits":
                        UserService.getLimits();
                        break;

                    case "/getInfo":
                        UserService.getInfo();
                        break;

                    case "/removeLimit":
                        System.out.print("Введите категорию: ");
                        String removeLimit = scanner.nextLine();
                        UserService.removeLimit(removeLimit);
                        break;

                    case "/exit":
                        System.out.println("Выход из программы.");
                        UserService.save();
                        scanner.close();
                        return;

                    default:
                        System.out.println("Неизвестная команда. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    public static void printMenu() {
        System.out.println(""" 
                \n
                Доступные команды:
                /register - Регистрация пользователя
                /login - Вход в систему
                /checkLogin - Проверка авторизации
                /logout - Выход из системы \n
                /addLimit - Добавить лимит
                /getLimits - Получить лимиты
                /removeLimit - Удалить лимит \n
                /addIncome - Добавить доход
                /addOutcome - Добавить расход \n
                /getInfo - Получить информацию о пользователе \n
                /exit - Выход из программы
                """);
    }
}