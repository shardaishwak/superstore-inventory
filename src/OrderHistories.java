import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class OrderHistories {
    private static ArrayList<OrderHistory> orders;
    private static String path = "./src/DB/order-histories.txt";

    /*
        Load orders from the Databse
     */
    public static void load() {
        // creating empty temporary array that will store the products list
        ArrayList<OrderHistory> temp_orders = new ArrayList<OrderHistory>();

        try {
            // Readint the path file
            File file = new File(path);
            Scanner fileScanner = new Scanner(file);

            while(fileScanner.hasNext()) {
                // Getting the data line by line
                String line = fileScanner.nextLine();
                // Instatiating a new product and storing in the temporary array
                // parse each order
                temp_orders.add(OrderHistory.parse(line, "~"));
            }
            fileScanner.close();

        } catch(Exception err) {
            System.out.println(err.getMessage());
        }
        // Update the cache.
        orders = temp_orders;
    }

    /*
        Add a new order to the current cache and database
     */
    public static boolean appendOrder(OrderHistory order) {
        orders.add(order);
        syncOrders();
        return true;
    }

    /*
        Syncronize the current order cache to the database
     */
    public static void syncOrders() {
        try {
            // Update the file with sorting
            PrintWriter writer = new PrintWriter(path);
            // will not sort it.
            for (OrderHistory i : (orders)) {
                writer.println(i);
            }
            writer.close();
        } catch(Exception err) {
            System.out.println(err.getMessage());
        }
    }
    /*
        Retrieve the list of orders
     */
    public static ArrayList<OrderHistory> getOrders() {
        return orders;
    }
    /*
        Retrieve a single order by Id
     */
    public static OrderHistory getOrder(String id) {
        int index = findOrderById(id);
        if (index == -1) return null;
        else return orders.get(index);
    }
    /*
        Find orders by user id
     */
    public static ArrayList<OrderHistory> findOrdersByUserId(String userId) {
        ArrayList<OrderHistory> temp_orders = new ArrayList<>();
        for (OrderHistory order : orders) {
            if (order.getUserId().equals(userId)) temp_orders.add(order);
        }
        return temp_orders;
    }

    /*
        Find index of order by ID
     */
    public static int findOrderById(String id) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId().equals(id)) return i;
        }
        return -1;
    }


    /*
        Sort orders by ID
     */
    public static ArrayList<OrderHistory> bubbleSort(ArrayList<OrderHistory> orders) {
        for (int i = orders.size() - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                if (orders.get(j).getId().compareTo(orders.get(j+1).getId()) > 0) {
                    OrderHistory temp = orders.get(j);
                    orders.set(j, orders.get(j+1));
                    orders.set(j+1, temp);
                }
            }
        }
        return orders;

    }

    /*
        Print tabloid format of the orders
     */
    public static void tabloidPrint(ArrayList<OrderHistory> orders) {
        if (orders == null) {
            System.out.println("No order found.");
            return;
        }
        System.out.println();
        for (OrderHistory order : orders) {
            System.out.println("OrderID: " + order.getId());
            System.out.println("User ID: " + order.getUserId());
            System.out.println("Total cost: " + order.getTotalPrice() + "CA$");
            System.out.println("Order status: " + order.getStatus());
            System.out.println("Products: ");
            Products.tabloidPrint(order.getProducts());
            System.out.println();
        }
        System.out.println();
    }

    public static void tabloidPrint() {
        tabloidPrint(orders);
    }
}
