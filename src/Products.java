import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Products {
    // Global container for all the products
    private static ArrayList<Product> products = new ArrayList<>();
    // Path to the products file
    public static String path = "./src/DB/products.txt";

    // Load all the products constructor
    public Products() {
        LoadProducts();
    }

    // Load all products method: called in constructor or anywhere in the app
    // It fetches all the products and saves them in cache
    public static void LoadProducts() {
        // creating empty temporary array that will store the products list
        ArrayList<Product> tproducts = new ArrayList<Product>();

        try {
            // Readint the path file
            File file = new File(path);
            Scanner fileScanner = new Scanner(file);

            while(fileScanner.hasNext()) {
                // Getting the data line by line
                String line = fileScanner.nextLine();
                String data[] = line.split("~");

                // Getting the data from the line
                String ID = data[0];
                String name = data[1];
                String description = data[2];
                String quantity = data[3];
                String price = data[4];
                String discount = data[5];
                String category = data[6];


                // Instatiating a new product and storing in the temporary array
                tproducts.add(new Product(ID, name, description, Integer.valueOf(quantity), Double.valueOf(price), Double.valueOf(discount), category));
            }
            fileScanner.close();

        } catch(Exception err) {
            System.out.println(err.getMessage());
        }
        // Update the cache.
        products = tproducts;
    }
    public static ArrayList<Product> getProducts() {
        // REturn the list of products
        return products;
    }
    // Add a new product to the list
    // Update the database file as well as local cache
    // iWill apply the bubble sort sorting, by index as ID
    // We will use binary search to search by ID nlogN
    // all the other search will be O(n)
    public static void appendProduct(Product product) {
        // get list of products
        ArrayList<Product> temp_products = products;
        // Add the product
        temp_products.add(product);
        //products = binarySort(products);
        try {
            // Update the file with sorting
            PrintWriter writer = new PrintWriter(path);
            for (Product i : temp_products) {
                writer.println(i);
            }
            writer.close();
        } catch(Exception err) {
            System.out.println(err.getMessage());
        }
        // Update the cache
        products = temp_products;
    }

    // Prompts for creating a new product
    public static Product createProduct() {

        // only inputs from user
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("The system will ask some prompts for the product");
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Category");
        String category = scanner.nextLine();

        System.out.print("Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        // instantiate a new product
        Product product = new Product(name, description, quantity, price, category);

        System.out.println("Product created: " + product);
        System.out.println();

        // return the new product
        return product;
    }

    // Update a product by ID
    public static Product updateProduct(String ID, Product new_product) {
        ArrayList<Product> temp_products = products;
        // Find the product
        int index = findProductByID(ID);

        // Check if it exists
        if (index == -1) {
            System.out.println("ERROR:PRODUCTS:UPDATE-product: PRODUCT not found.");
            return null;
        }

        // Update the position with new list
        temp_products.set(index, new_product);

        try {
            // Update the file
            FileWriter writer = new FileWriter(path);
            for (Product product : temp_products) {
                writer.write(product.toString());
            }
            writer.close();
        } catch(Exception err) {
            System.out.println(err.getMessage());
        }
        // update cache
        products = temp_products;
        return new_product;
    }

    // TODO: Delete product by ID
    public static boolean deleteProductById(String ID) {return false;}

    public static int findProductByID(String ID) {
        ArrayList<Product> products = getProducts();

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getID().equals(ID)) return i;
        }
        return -1;
    }
    public static int findProductsByName(String ID) {

        // can find more than one product!
        return -1;
    }
    public static int findProductsByPrice(double price) {
        return -1;
    }
    public static int findProductsByQuantity(int quantity) {
        return -1;
    }
    public static int findProductsByDiscount(double discount) {
        return -1;
    }

    public static int findProductsByCategory(String category) {return -1;}


    // Binary search for ID
    public static ArrayList<Product> binarySort(ArrayList<Product> products) {
        for (int i = products.size() - 1; i >= 0; i--) {
            for (int j = 0; j < i-1; j++) {
                if (products.get(j).getName().compareTo(products.get(j+1).getName()) > 0) {
                    Product temp = products.get(j);
                    products.set(j, products.get(j+1));
                    products.set(j+1, temp);
                }
            }
        }
        return products;
    }


    // Print the tabloid format of the products
    public static void tabloidPrint() {
        int maxName = "Name".length();
        int maxID = "ID".length();
        int maxDescription = "Description".length();
        int maxCategory = "Category".length();
        int maxQuantity = "Quantity".length();
        int maxPrice = "Price".length();
        int maxDiscount = "Discount".length();
        for (Product product : products) {
            if (product.getID().length() > maxID) maxID = product.getID().length();
            if (product.getName().length() > maxName) maxName = product.getName().length();
            if (product.getDescription().length() > maxDescription) maxDescription = product.getDescription().length();
            if (product.getCategory().length() > maxCategory) maxCategory = product.getCategory().length();
            if (String.valueOf(product.getQuantity()).length() > maxQuantity) maxQuantity = String.valueOf(product.getQuantity()).length();
            if (String.valueOf(product.getPrice()).length() > maxPrice) maxPrice = String.valueOf(product.getPrice()).length();
            if (String.valueOf(product.getDiscount()).length() > maxDiscount) maxDiscount = String.valueOf(product.getDiscount()).length();
        }


        String format = "|"+"%-"+(maxID+2)+"s"+"|"+"%-"+(maxName+2)+"s"+"|"+"%-"+(maxCategory+2)+"s"+"|"+"%-"+(maxDescription+2)+"s"+"|"+"%-"+(maxQuantity+2)+"s"+"|"+"%-"+(maxPrice+5)+"s"+"|"+"%-"+(maxDiscount+2)+"s\n";
        System.out.println();
        System.out.printf(format, "ID", "Name", "Category","Description", "Qty. ", "Price", "Discount");
        for (Product product : getProducts()) {
            System.out.printf(format, product.getID(), product.getName(), product.getCategory(), product.getDescription(), String.valueOf(product.getQuantity()), String.valueOf(product.getPrice()) + " CA$", String.valueOf(product.getDiscount()));
        }
        System.out.println();
    }



}
