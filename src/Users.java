// Handle all the logic about creating or writing users
// Move the logic of user from authentication to here

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

// TODO cache users: make app more efficient: do not read all times!

public class Users {
    private static ArrayList<User> users = new ArrayList<>();
    public static String path = "./src/DB/users.txt";

    public Users() {
        LoadUsers();
    }

    public static void LoadUsers() {
        ArrayList<User> temp_users = new ArrayList<User>();
        try {
            File file = new File(path);
            Scanner fileScanner = new Scanner(file);

            while(fileScanner.hasNext()) {
                String line = fileScanner.nextLine();
                String data[] = line.split("~");

                String ID = data[0];
                String name = data[1];
                String email = data[2];
                String password = data[3];
                String role = data[4];

                temp_users.add(new User(ID, name, email, password, role));
            }
            fileScanner.close();

        } catch(Exception err) {
            System.out.println(err.getMessage());
        }
        users = temp_users;
    }
    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void appendUser(User user) {
        // get list of users
        ArrayList<User> temp_users = users;
        // Add the user
        temp_users.add(user);
        //user = binarySort(user);
        try {
            // Update the file with sorting
            PrintWriter writer = new PrintWriter(path);
            for (User i : temp_users) {
                writer.println(i);
            }
            writer.close();
        } catch(Exception err) {
            System.out.println(err.getMessage());
        }
        // Update the cache
        users = temp_users;
    }
    public static User updateUser(String ID, User new_user) {
        ArrayList<User> temp_users = users;
        int index = findUserByID(ID);

        if (index == -1) {
            System.out.println("ERROR:USERS:UPDATE-USER:User not found");
            return null;
        }

        temp_users.set(index, new_user);

        try {
            FileWriter writer = new FileWriter(path);
            for (User user : temp_users) {
                writer.write(user.toStringWithPassword());
            }
            writer.close();
        } catch(Exception err) {
            System.out.println(err.getMessage());
        }
        users = temp_users;
        return new_user;
    }

    // implement binary search
    public static int findUserByEmail(String email) {
        ArrayList<User> users = getUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(email)) return i;
        }
        return -1;
    }
    // implement binary search
    public static int findUserByID(String id) {
        ArrayList<User> users = getUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getID().equals(id)) return i;
        }
        return -1;
    }

    // TODO: Delete user by ID

    public static void tabloidPrint() {

        int maxName = 0;
        int maxID = 0;
        int maxEmail = 0;
        int maxRole = 0;
        for (User user : users) {
            if (user.getID().length() > maxID) maxID = user.getID().length();
            if (user.getName().length() > maxName) maxName = user.getName().length();
            if (user.getEmail().length() > maxEmail) maxEmail = user.getEmail().length();
            if (user.getRole().length() > maxRole) maxRole = user.getRole().length();
        }


        String format = "|"+"%-"+(maxID+2)+"s"+"|"+"%-"+(maxName+2)+"s"+"|"+"%-"+(maxEmail+2)+"s"+"|"+"%-"+(maxRole+2)+"s\n";
        System.out.println();
        System.out.printf(format, "ID", "Name", "Email", "Role");
        for (User user : getUsers()) {
            System.out.printf(format, user.getID(), user.getName(), user.getEmail(), user.getRole());
        }
        System.out.println();
    }
}
