import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Products {
    public static String path = "./src/DB/products.txt";
    public static ArrayList<Product> getProducts() {
        // Retrieve all the products
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            File file = new File(path);
            Scanner fileScanner = new Scanner(file);

            while(fileScanner.hasNext()) {
                String line = fileScanner.nextLine();
                String data[] = line.split("~");

                String ID = data[0];
                String name = data[1];
                String description = data[2];
                String quantity = data[3];
                String price = data[4];
                String discount = data[5];
                String category = data[6];



                products.add(new Product(ID, name, description, Integer.valueOf(quantity), Double.valueOf(price), Double.valueOf(discount), category));
            }
            fileScanner.close();

        } catch(Exception err) {
            System.out.println(err.getMessage());
        }
        return products;
    }

    public static void appendProduct(Product product) {
        Utilities.appendFile(Products.path, product.toString());
    }

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




        Product product = new Product(name, description, quantity, price, category);

        System.out.println("Product created: " + product);
        System.out.println();

        return product;
    }

    public static Product updateProduct(String ID, Product new_product) {
        ArrayList<Product> products = getProducts();
        int index = findProductByID(ID);

        if (index == -1) {
            System.out.println("ERROR:PRODUCTS:UPDATE-product: PRODUCT not found.");
            return null;
        }

        products.set(index, new_product);

        try {
            FileWriter writer = new FileWriter(path);
            for (Product product : products) {
                writer.write(product.toString());
            }
            writer.close();
        } catch(Exception err) {
            System.out.println(err.getMessage());
        }
        return new_product;
    }

    // TODO: Delete product by ID

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


    public static void tabloidPrint() {
        int maxName = "Name".length();
        int maxID = "ID".length();
        int maxDescription = "Description".length();
        int maxCategory = "Category".length();
        int maxQuantity = "Quantity".length();
        int maxPrice = "Price".length();
        int maxDiscount = "Discount".length();
        for (Product product : getProducts()) {
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
