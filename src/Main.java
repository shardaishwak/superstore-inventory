import jdk.jshell.execution.Util;

import java.util.ArrayList;
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

        // Load all orders from the DB
        Orders.load();




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
                {"delete-users", "Delete one or more users", "delete-users [id] [id] ..."},
                {"delete-account", "Delete your account", ""},
                {"", "", ""},
                {"create-product", "Create a new product in the database", ""},
                {"update-product", "Update information about a particular product", ""},
                {"show-products", "Show all products in the database", ""},
                {"search", "Search for a particular product. For example 'search category cheese' or 'search price 23.99'.", "search [filter] [value]"},
                {"", "Filter by category, name, id, price, discount", ""},
                {"", "Price and discount filters return products with price equal or less than the given.", ""},
                {"delete-products", "Delete a particular product by ID. One or more product id separated by space.", "delete-products [id] [id] ..."},
                {"", "", ""},
                {"show-orders", "Show the list of all the orders", ""},
                {"search-order", "search an order by ID", "search-order [id]"},
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
                {"show-all-products", "Show all products in the database", ""},
                {"show-orders", "Show the list of all the orders", ""},
                {"show-cart", "Show the user's shopping cart", ""},
                {"add-product-to-cart", "Add a product to cart", ""},
                {"remove-product-from-cart", "Remove a product to cart", ""},
                {"checkout", "Checkout the current cart", ""},
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
                Users.tabloidPrint(auth.getCurrentUser().getID());
                break;
            }
            case "delete-users": {
                String[] IDs = Utilities.sliceArray(fullCommand.split(" "), 1);
                for (String id : IDs) {
                    if (id.equals(auth.getCurrentUser().getID())) {
                        System.out.println("Error for "+ id + ": This is your account. Use delete-account to delete your personal account.");
                    } else {
                        if (Users.deleteUser(id)) System.out.println("User " + id + " deleted.");
                        else System.out.println("Error deleting user " + id);
                    }
                }

                break;
            }
            case "delete-account": {
                //logout
                String id = auth.getCurrentUser().getID();
                auth.logout();
                Users.deleteUser(id);
                //Users.deleteUser
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
            case "delete-products": {
                String[] IDs = Utilities.sliceArray(fullCommand.split(" "), 1);
                for (String id : IDs) {
                    if (Products.deleteProductById(id)) System.out.println("Product " + id + " deleted.");
                    else System.out.println("Error deleting product " + id);
                }

                break;
            }
            case "show-orders": {
                Orders.tabloidPrint();
                break;
            }
            case "search-order": {
                Order order = Orders.getOrder(fullCommand.split(" ")[1]);
                if (order == null) {
                    System.out.println("Order not found.");
                } else {
                    ArrayList<Order> orders = new ArrayList<>();
                    orders.add(order);
                    Orders.tabloidPrint(orders);
                }
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
        ShoppingCart cart = new ShoppingCart(auth.getCurrentUser().getID());
        Scanner scanner = new Scanner(System.in);

        System.out.print("> ");
        String fullCommand = scanner.nextLine();
        String option = fullCommand.split(" ")[0];

        // create a shopping cart class: initialize a new order or get the current cart

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
            case "show-orders": {
                ArrayList<Order> orders = Orders.findOrdersByUserId(auth.getCurrentUser().getID());
                if (orders.size() == 0) System.out.println("No order found.");
                else {
                    Orders.tabloidPrint(orders);
                }
                break;

            }
            case "show-cart": {
                cart.print();
                break;
            }
            case "add-product-to-cart": {
                cart.addProductToCartWithInputs();
                break;
            }
            case "remove-product-from-cart": {
                cart.removeProductFromCartWithInputs();
                break;
            }
            case "checkout": {
                cart.checkout();
                break;
            }
            case "show-all-products": {
                Products.tabloidPrint();
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