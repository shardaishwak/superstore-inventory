
/*
    user authentication system

    Features:
        - Create user
        - Get user info: name, email, ID
        - Update password
        - Update email or name
        - Delete user: delete also all the items associated to user ID

    TODO: create a settings file and Settings that save the current settings such as the logged user Id, so that we do not need to ask it again
 */

import java.util.Scanner;

public class Authentication {
    private User currentUser;

    public void createUser() {
        if (this.currentUser != null) {
            System.out.println("You need to logout first. You are currently authenticated as "+this.currentUser.getName());
            return;
        }
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the name: ");
        String name = scanner.nextLine();

        String email = getEmailInput(scanner, true);
        String password = getPasswordInput(scanner, true);

        System.out.println("Are you an inventory manager (you can update inventory) (yes or no)?: ");
        String role = scanner.nextLine();
        if (role.equals("yes")) role = "admin";
        else role = "consumer";


        // Add the user to the file
        // return the user with new creation
        User user = new User(name, email, password, role);
        this.currentUser = user;
        Users.appendUser(user);

        System.out.println("Logged in as:");
        System.out.println(user);
    }

    public void loginUser() {
        if (this.currentUser != null) {
            System.out.println("You need to logout first. You are currently authenticated as "+this.currentUser.getName());
            return;
        }
        Scanner scanner = new Scanner(System.in);


        String email = getEmailInput(scanner, false);
        String password = getPasswordInput(scanner, false);

        int userIndex = Users.findUserByEmail(email);
        if (userIndex == -1) {
            System.out.println("User not found in the DB.");
            return;
        }

        User user = Users.getUsers().get(userIndex);

        // validate password
        if (!user.validatePassword(password)) {
            System.out.println("Invalid password.");
            return;
        }


        // Add the user to the file
        // return the user with new creation
        this.currentUser = user;
        System.out.println("Logged in as:");
        System.out.println(user);
    }

    public void updatePassword() {

        // Get the user logged in before updating the password
        if (this.currentUser == null) this.loginUser();

        Scanner scanner = new Scanner(System.in);

        String new_password = getPasswordInput(scanner, true);

        this.currentUser.setPassword(new_password);
        // update the list

        Users.updateUser(this.currentUser.getID(), this.currentUser);
        System.out.println("Password Updated!");
    }


    public void logout() {
        this.currentUser = null;
    }

    public boolean isAuthenticated() {
        return this.currentUser != null;
    }

    // Inputs
    public String getEmailInput(Scanner scanner, boolean withValidation) {
        while(true) {
            try {
                System.out.println("Enter the email: ");
                String email = scanner.nextLine();

                if (withValidation && email.indexOf('@') == -1) throw new Exception("Invalid email format. Should be yourname@something.other");
                if (withValidation && isEmailExist(email)) throw new Exception("Email already in user.");

                return email;
            } catch(Exception err) {
                System.out.println("ERROR: " + err.getMessage());
            }
        }
    }
    public String getPasswordInput(Scanner scanner, boolean withValidation) {
        while(true) {
            try {
                System.out.println("Enter the password: ");
                String password = scanner.nextLine();

                if (withValidation && !validatePassword(password)) {
                    throw new Exception("Invalid password. Must include an uppercase, lowercase and number");
                }

                return password;
            } catch(Exception err) {
                System.out.println("ERROR: " + err.getMessage());
            }
        }
    }


    // Helpers
    public boolean isEmailExist(String email) {
        return Users.findUserByEmail(email) != -1;
    }

    public boolean validatePassword(String password) {
        boolean isUppercase = false;
        boolean isLowercase = false;
        boolean isNumber = false;

        for (int i = 0 ; i < password.length(); i++) {
            char c = password.charAt(i);
            if (c >= 'A' && c <= 'Z') isUppercase = true;
            if (c >= 'a' && c <= 'z') isLowercase = true;
            if (c >= '0' && c <= '9') isNumber = true;
        }

        return isLowercase && isUppercase && isNumber;
    }


    public User getCurrentUser() {
        return this.currentUser;
    }
}
