import java.util.ArrayList;

public class OrderHistory {
    // Order ID
    private String id;
    // Id of user who made the order
    private String userId;
    // Total order price
    private double totalPrice;
    // List of products in the order
    private ArrayList<Product> products;
    // Status of the order
    private String status; // created, pending

    /*
        Instantiate a newly created order
     */
    public OrderHistory(String userId, ArrayList<Product> products) {
        this.id = Utilities.generateUUID("order");
        this.userId = userId;
        this.products = products;
        this.totalPrice = 0;
        this.status = "created";
        for (Product product : products) {
            this.totalPrice+=product.getPrice();
        }
    }
    /*
        Instantiate an already created order from the Database, if necessary in any circumstance
     */
    public OrderHistory(String id, String userId, double totalPrice, ArrayList<Product> products, String status) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.products = products;
        this.status = status;
    }

    /*
        Setters and getters for the previous fields
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return this.id + "~" + this.userId + "~" + stringifyProducts(this.products) + "~" + this.totalPrice + "~" + this.status;
    }
    /*
        Related to toString which acts as the order stringifier
     */
    public static OrderHistory parse(String line, String delimiter) {
        // Split using the delimiter
        String data[] = line.split(delimiter);

        // Getting the data from the line
        String ID = data[0];
        String userId = data[1];
        // parser the products using the method below
        ArrayList<Product> products = parseProducts(data[2]);
        String totalPrice = data[3];
        String status = data[4];

        // returning a new order
        return new OrderHistory(ID, userId, Double.parseDouble(totalPrice),products, status);
    }


    /*
        Stringify and parser products are related
     */
    /*
        Stringify the products in the format of product1--product2--product2 = "--" delimiter
        product1 is formatted as id+name+description+... = "+ delimiter"
     */
    public static String stringifyProducts(ArrayList<Product> products) {
        String productsString = "";
        for (int i = 0 ; i < products.size(); i++) {
            Product product = products.get(i);
            productsString+=product.toString("+");
            if (i < products.size()-1) productsString+= "--";
        }
        return productsString;
    }
    /*
        Parse the stringified products list using the previous delimiters
     */
    public static ArrayList<Product> parseProducts(String elements) {
        // this delimiter divides one product from another
        // Split the string represeting each product
        String[] prodString = elements.split("--");
        ArrayList<Product> products = new ArrayList<>();
        // Iterate
        for (String prod : prodString) {
            // this delimiter divides one element in product from another.
            // Using "\\+" because "+" is a special delimiter in the list
            products.add(Product.parse(prod, "\\+"));
        }
        return products;
    }
}
