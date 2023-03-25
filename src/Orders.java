import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Orders {
    private static ArrayList<Order> orders;
    private static String path = "./src/DB/order-histories.txt";

    /**
        Load orders from the Databse
     */
    public static void load() {
        // creating empty temporary array that will store the products list
        ArrayList<Order> temp_orders = new ArrayList<Order>();

        try {
            // Readint the path file
            File file = new File(path);
            Scanner fileScanner = new Scanner(file);

            while(fileScanner.hasNext()) {
                // Getting the data line by line
                String line = fileScanner.nextLine();
                // Instatiating a new product and storing in the temporary array
                // parse each order
                temp_orders.add(Order.parse(line, "~"));
            }
            fileScanner.close();

        } catch(Exception err) {
            System.out.println(err.getMessage());
        }
        // Update the cache.
        orders = temp_orders;
    }

    /**
        Add a new order to the current cache and database
     */
    public static boolean appendOrder(Order order) {
        orders.add(order);
        syncOrders();
        return true;
    }

    /**
     * Add a product to the order list
     * @param orderId
     * @param productId
     * @param quantity
     * @return
     */
    public static boolean appendProductToOrder(String orderId, String productId, int quantity) {
        // ask id
        // ask quantity
        Order order = getOrder(orderId);
        Product product = Products.getProduct(productId);
        if (order == null) {
            System.out.println("Order not found.");
            return false;
        }
        if (product == null) {
            System.out.println("Product not found.");
            return false;
        }
        if (product.getQuantity() == 0) {
            System.out.println("Product " + product.getName() + " is out of stock.");
            return false;
        }
        if (product.getQuantity() < quantity) {
            System.out.println("There are only " + product.getQuantity() + " items available. Cannot satisfy your need.");
            return false;
        }

        // User already added the product
        // Just update the quantity to add
        // create a copy of the item
        Product new_prod = new Product(product.getID(), product.getName(), product.getDescription(), product.getQuantity(), product.getPrice(), product.getCategory());
        if (order.findProductById(productId) != -1) {
            new_prod.setQuantity(order.getProduct(productId).getQuantity()+quantity);
            order.updateProduct(productId, new_prod);
        } else {
            new_prod.setQuantity(quantity);
            order.appendProduct(new_prod);
        }
        // update the inventory
        Products.updateProductQuantity(productId, -quantity);
        syncOrders();

        return true;
    }

    /**
     * Checkout the current order
     * @param orderId
     * @return
     */
    public static boolean updateStatusToComplete(String orderId) {
        // update all products quantity
        // change the status
        Order order = getOrder(orderId);
        if (order == null) {
            System.out.println("Order not found.");
            return false;
        }

        order.setStatus("completed");
        syncOrders();
        return true;
    }

    /**
     * Remove a product from the order list
     * @param orderId
     * @param productId
     * @return
     */
    public static boolean removeProductFromOrder(String orderId, String productId) {
        Order order = getOrder(orderId);
        int productIndex = order.findProductById(productId);
        if (productIndex == -1) {
            System.out.println("product not found.");
            return false;
        }
        int quantity = order.getProducts().get(productIndex).getQuantity();

        // remove the product from the order
        order.removeProduct(productIndex);
        // incraes the quantity of the product
        Products.updateProductQuantity(productId, quantity);
        syncOrders();
        return true;
    }

    /**
        Syncronize the current order cache to the database
     */
    public static void syncOrders() {
        try {
            // Update the file with sorting
            PrintWriter writer = new PrintWriter(path);
            // will not sort it.
            for (Order i : (orders)) {
                writer.println(i);
            }
            writer.close();
        } catch(Exception err) {
            System.out.println(err.getMessage());
        }
    }
    /**
        Retrieve the list of orders
     */
    public static ArrayList<Order> getOrders() {
        return orders;
    }
    /**
        Retrieve a single order by Id
     */
    public static Order getOrder(String id) {
        int index = findOrderById(id);
        if (index == -1) return null;
        else return orders.get(index);
    }
    /**
        Find orders by user id
        If activeOnly: show the shopping cart
     */

    public static ArrayList<Order> findOrdersByUserId(String userId) {
        ArrayList<Order> temp_orders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getUserId().equals(userId) && order.getStatus().equals("completed")) temp_orders.add(order);
        }
        return temp_orders;
    }

    public static ArrayList<Order> getShoppingCart(String userId) {
        ArrayList<Order> temp_orders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getUserId().equals(userId) && order.getStatus().equals("in-progress")) temp_orders.add(order);
        }
        return temp_orders;
    }

    /**
        Find index of order by ID
     */
    public static int findOrderById(String id) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId().equals(id)) return i;
        }
        return -1;
    }


    /**
        Sort orders by ID
     */
    public static ArrayList<Order> bubbleSort(ArrayList<Order> orders) {
        for (int i = orders.size() - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                if (orders.get(j).getId().compareTo(orders.get(j+1).getId()) > 0) {
                    Order temp = orders.get(j);
                    orders.set(j, orders.get(j+1));
                    orders.set(j+1, temp);
                }
            }
        }
        return orders;

    }

    /**
        Print tabloid format of the orders
     */
    public static void tabloidPrint(ArrayList<Order> orders) {
        if (orders == null) {
            System.out.println("No order found.");
            return;
        }
        System.out.println();
        for (Order order : orders) {
            System.out.println("OrderID: " + order.getId());
            System.out.println("User ID: " + order.getUserId());
            System.out.println("Total cost: " + String.format("%.2f",order.getTotalPrice()) + "CA$");
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
