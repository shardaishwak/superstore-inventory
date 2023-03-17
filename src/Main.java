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

        //Products.tabloidPrint(Products.findProductsByCategory());


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
                if (!controller.equals("shopper")) {
                    // print the new commands
                    controller = "shopper";
                }
                // shopper controller
            }

            System.out.println();
        }


    }

    public static void printAuthOptions() {
        System.out.println();

        System.out.println("| COMMAND           | ACTION");
        System.out.println("| options           | Simply print the options");
        System.out.println("| create-user       | Create a new user");
        System.out.println("| login             | Login");

        System.out.println("| exit              | exit system");
        System.out.println();
    }

    public static void printAdminOptions() {
        System.out.println();


        System.out.println("| Command           | Action");

        System.out.println("| user              | Get current user");
        System.out.println("| show-users        | Show all users in database");
        System.out.println("| update-password   | Update password");
        System.out.println("| logout            | Logout");


        System.out.println();
        System.out.println("| create-product    | Create a new product");
        System.out.println("| update-product    | Update a product by a particular field");
        System.out.println("| show-products     | Show all products in database");
        System.out.println("| exit              | Exit system");
        System.out.println();
    }

    public static boolean adminController(Authentication auth) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the option: ");
        String option = scanner.nextLine();

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
            case "exit": {
                return true;
            }
            default: {
                System.out.println("Invalid command. Not found.");
            }
        }
        return false;
    }
    public static boolean authController(Authentication auth) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the option: ");
        String option = scanner.nextLine();

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
            case "user": {
                System.out.println(auth.getCurrentUser());
                break;
            }
            case "logout": {
                auth.logout();
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