import jdk.jshell.execution.Util;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize authentication object
        Authentication auth = new Authentication();

        // Load add the users from the DB
        Users.load();

        // Load all the products from the DB
        Products.load();

        // get auth state

        String controller = ""; // the controller shows different actions that we can perform
        // possibilities: "auth", "inventory", "shopper"

        boolean feedbackToQuit = false;
        while(true) {
            if (feedbackToQuit) break;
            if (!auth.isAuthenticated()) {
                // show options for authenticated users
                System.out.println("AUTHENTICATION OPTIONS");

                if (!controller.equals("auth")) {
                    // print the new commands
                    printAuthOptions();
                    controller = "auth";
                }
                feedbackToQuit = authController(auth);
            } else if (auth.getCurrentUser().getRole().equals("admin")) {
                System.out.println("ADMIN OPTIONS");

                if (!controller.equals("inventory")) {
                    // print the new commands
                    printAdminOptions();
                    controller = "inventory";
                }
                // inventory controller
                feedbackToQuit = adminController(auth);
            } else {
                System.out.println("SHOPPER OPTIONS");
                if (!controller.equals("shopper")) {
                    // print the new commands
                    printShopperOptions();
                    controller = "shopper";
                }
                // shopper controller
                feedbackToQuit = shopperController(auth);
            }

            System.out.println();
        }


    }

    public static void printAuthOptions() {
        String[][] options = {
                {"options", "Show all the available commands", ""},
                {"create-user", "Create a new account in the system", ""},
                {"login", "Login into the system", ""},
                {"", "", ""},
                {"exit", "Exit the system", ""}
        };

        Utilities.printOptions(options);
    }

    public static void printAdminOptions() {
        String[][] options = {
                {"user", "Get the current user", ""},
                {"show-users", "Show the list of all the users in database", ""},
                {"update-password", "Update the current password", ""},
                {"logout", "Logout from system", ""},
                {"", "", ""},
                {"create-product", "Create a new product in the database", ""},
                {"update-product", "Update information about a particular product", ""},
                {"show-products", "Show all products in the database", ""},
                {"search", "Search for a particular product. For example 'search category cheese' or 'search price 23.99'.", "search [filter] [value]"},
                {"", "Filter by category, name, id, price, discount", ""},
                {"", "", ""},
                {"exit", "Exit the system", ""}
        };

        Utilities.printOptions(options);
    }

    public static void printShopperOptions() {
        String[][] options = {
                {"options", "Print available commands", ""},
                {"user", "Get the current user", ""},
                {"update-password", "Update the current password", ""},
                {"logout", "Logout from system", ""},
                {"", "", ""},
                {"search", "Search for a particular product. For example 'search category cheese' or 'search price 23.99'.", "search [filter] [value]"},
                {"", "Filter by category, name, id, price, discount", ""},
                {"", "Price and discount filters return products with price equal or less than the given.", ""},
                {"", "", ""},
                {"exit", "Exit the system", ""}
        };

        Utilities.printOptions(options);
    }

    public static boolean authController(Authentication auth) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("> ");
        String fullCommand = scanner.nextLine();
        String option = fullCommand.split(" ")[0];

        switch(option) {
            case "options": {
                printAuthOptions();
                break;
            }
            case "create-user": {
                auth.createUser();
                break;
            }
            case "login": {
                auth.loginUser();
                break;
            }
            case "exit": {
                return true;
            }
            default: {
                System.out.println("Invalid command. Not found.");
            }
        }
        return false;
    }

    public static boolean adminController(Authentication auth) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("> ");
        String fullCommand = scanner.nextLine();
        String option = fullCommand.split(" ")[0];

        switch(option) {
            case "options": {
                printAdminOptions();
                break;
            }
            case "user": {
                System.out.println(auth.getCurrentUser());
                break;
            }
            case "logout": {
                auth.logout();
                break;
            }
            case "show-users": {
                Users.tabloidPrint();
                break;
            }
            case "update-password": {
                auth.updatePassword();
                break;
            }
            case "create-product": {
                Product product = Products.createProduct();
                Products.appendProduct(product);
                break;
            }
            case "show-products": {
                Products.tabloidPrint();
                break;
            }
            case "update-product": {
                Products.updateProductWithInputs();
                break;
            }
            case "search": {
                String[] values = fullCommand.split(" ");
                if (values.length < 3) {
                    System.out.println("Invalid search command. Expected format: search [filter] [value]");
                    break;
                }
                Products.tabloidPrint(Products.search(values[1], Utilities.joinArray(values, 2)));
                break;
            }
            case "exit": {
                return true;
            }
            default: {
                System.out.println("Invalid command. Not found.");
            }
        }
        return false;
    }

    public static boolean shopperController(Authentication auth) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("> ");
        String fullCommand = scanner.nextLine();
        String option = fullCommand.split(" ")[0];

        switch(option) {
            case "options": {
                printShopperOptions();
                break;
            }
            case "user": {
                System.out.println(auth.getCurrentUser());
                break;
            }
            case "logout": {
                auth.logout();
                break;
            }
            case "search": {
                String[] values = fullCommand.split(" ");
                if (values.length < 3) {
                    System.out.println("Invalid search command. Expected format: search [filter] [value]");
                    break;
                }
                Products.tabloidPrint(Products.search(values[1], Utilities.joinArray(values, 2)));
                break;
            }
            case "exit": {
                return true;
            }
            default: {
                System.out.println("Invalid command. Not found.");
            }
        }
        return false;
    }

}