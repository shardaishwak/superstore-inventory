import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Users {
    // List of all the users in the cache
    private static ArrayList<User> users = new ArrayList<>();
    // DB file path
    public static String path = "./src/DB/users.txt";

    // Constructor if required
    public Users() {
        load();
    }

    /*
        Load data from the DB
     */
    public static void load() {
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
        users = bubbleSort(temp_users);
    }

    /*
        Sync current cache with the database
     */
    public static void syncUsers() {
        try {
            // Update the file with sorting
            PrintWriter writer = new PrintWriter(path);
            users = bubbleSort(users);
            for (User i : bubbleSort(users)) {
                writer.println(i.toStringWithPassword());
            }
            writer.close();
        } catch(Exception err) {
            System.out.println(err.getMessage());
        }
    }


    /*
        CRUD operations
     */

    /*
        Get the users
     */
    public static ArrayList<User> getUsers() {
        return users;
    }
    /*
        Add a new user to the list
     */
    public static void appendUser(User user) {
        // get list of users
        ArrayList<User> temp_users = users;
        // Add the user
        temp_users.add(user);
        // Update the cache
        users = temp_users;

        syncUsers();
    }
    /*
        Update the users list
     */
    public static User updateUser(String ID, User new_user) {
        ArrayList<User> temp_users = users;
        int index = findUserByID(ID);

        if (index == -1) {
            System.out.println("ERROR:USERS:UPDATE-USER:User not found");
            return null;
        }

        temp_users.set(index, new_user);

        users = temp_users;
        syncUsers();
        return new_user;
    }

    /*
        Find a user by Email
     */
    public static int findUserByEmail(String email) {
        ArrayList<User> users = getUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(email)) return i;
        }
        return -1;
    }

    /*
        Find a user by ID
        O(n)
     */
    public static int findUserByID(String id) {
        int low = 0;
        int high = users.size()-1;

        while(low <= high) {
            int mid = (high+low)/2;
            if (users.get(mid).getID().equals(id)) return mid;
            else if (users.get(mid).getID().compareTo(id) > 0) high = mid - 1;
            else low = mid + 1;
        }
        return -1;
    }

    /*
        Delete a user and all data associated to it.
     */
    public static boolean deleteUser(String id) {
        // Remove the items from bucket
        int index = Users.findUserByID(id);
        if (index == -1) {
            System.out.println("User not found in DB. Delete failed.");
            return false;
        }

        users.remove(index);
        syncUsers();

        return true;
    }




    /*
        Sort the list by:
            - Name
            - Email
            - Id
     */
    public static ArrayList<User> bubbleSort(ArrayList<User> temp_users, String sortBy, boolean descending) {
        ArrayList<User> users = (ArrayList<User>) temp_users.clone();
        for (int i = users.size() - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                int comparison;
                if (sortBy.equals("name")) {
                    comparison = users.get(j).getName().compareTo(users.get(j+1).getName());
                }
                else if (sortBy.equals("email")) {
                    comparison = users.get(j).getEmail().compareTo(users.get(j+1).getEmail());
                }
                else {
                    comparison = users.get(j).getID().compareTo(users.get(j+1).getID());
                }

                if (descending ? comparison < 0 : comparison > 0) {
                    User temp = users.get(j);
                    users.set(j, users.get(j+1));
                    users.set(j+1, temp);
                }
            }
        }
        return users;
    }

    public static ArrayList<User> bubbleSort(ArrayList<User> users) {
        return bubbleSort(users, "id", false);
    }

    /*
        Return a tabloid printout of the users
     */
    public static void tabloidPrint() {

        tabloidPrint(bubbleSort(users), null);
    }
    public static void tabloidPrint(String currentUserId) {

        tabloidPrint(bubbleSort(users), currentUserId);
    }

    public static void tabloidPrint(ArrayList<User> users, String currentUserId) {
        if (users == null) {
            System.out.println("No user found.");
            return;
        }
        int maxName = 0;
        int maxID = 0;
        int maxEmail = 0;
        int maxRole = 0;
        for (User user : users) {
            int idLength = (user.getID() + (currentUserId != null && currentUserId.equals(user.getID()) ? " (you)" : "")).length();
            if (idLength > maxID) {
                maxID = idLength;
            }
            if (user.getName().length() > maxName) maxName = user.getName().length();
            if (user.getEmail().length() > maxEmail) maxEmail = user.getEmail().length();
            if (user.getRole().length() > maxRole) maxRole = user.getRole().length();
        }


        String format = "|"+"%-"+(maxID+2)+"s"+"|"+"%-"+(maxName+2)+"s"+"|"+"%-"+(maxEmail+2)+"s"+"|"+"%-"+(maxRole+2)+"s\n";
        System.out.println();
        System.out.printf(format, "ID", "Name", "Email", "Role");
        for (User user : users) {
            System.out.printf(format, (user.getID() + (currentUserId != null && currentUserId.equals(user.getID()) ? " (you)" : "")), user.getName(), user.getEmail(), user.getRole());
        }
        System.out.println();
    }
}
