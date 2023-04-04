import java.util.ArrayList;

public class Order {
    // Order ID
    private String id;
    // Id of user who made the order
    private String userId;

    // List of products in the order
    private ArrayList<Product> products;
    // Status of the order
    private String status; // completed, in-process
    private double promotionDiscount;

    /*
        Instantiate a newly created order
     */
    public Order(String userId, ArrayList<Product> products) {
        this.id = Utilities.generateUUID();
        this.userId = userId;
        this.products = products;
        this.status = "in-progress";
        this.promotionDiscount = 0;
    }
    public Order(String userId) {
        this.id = Utilities.generateUUID();
        this.userId = userId;
        this.products = new ArrayList<>();
        this.status = "in-progress";
        this.promotionDiscount = 0;
    }
    /*
        Instantiate an already created order from the Database, if necessary in any circumstance
     */
    public Order(String id, String userId, ArrayList<Product> products, String status, double promotionDiscount) {
        this.id = id;
        this.userId = userId;
        this.products = products;
        this.status = status;
        this.promotionDiscount = promotionDiscount;
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
        double total = 0;
        for (Product product : this.products) {
            total+=product.getPrice()*product.getQuantity();
        }
        return total;
    }


    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public Product getProduct(String id) {
        int index = findProductById(id);
        if (index == -1) return null;
        return this.products.get(index);
    }
    public void appendProduct(Product product) {
        this.products.add(product);
    }

    public void updateProduct(String id, Product product) {
        int index = findProductById(id);
        this.products.set(index, product);
    }
    public void removeProduct(int index) {
        this.products.remove(index);
    }
    public int findProductById(String id) {
        for (int i = 0; i < this.products.size(); i++) {
            if (this.products.get(i).getID().equals(id)) return i;
        }

        return -1;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPromotionDiscount() {return this.promotionDiscount;}
    public void setPromotionDiscount(double value) {this.promotionDiscount = value;}

    public String toString() {
        return this.id + "~" + this.userId + "~" + stringifyProducts(this.products) + "~" + this.status + "~" + this.promotionDiscount;
    }
    /*
        Related to toString which acts as the order stringifier
     */
    public static Order parse(String line, String delimiter) {
        // Split using the delimiter
        String data[] = line.split(delimiter);

        // Getting the data from the line
        String ID = data[0];
        String userId = data[1];
        // parser the products using the method below
        ArrayList<Product> products = parseProducts(data[2]);
        String status = data[3];
        String promotionalDiscount = data[4];

        // returning a new order
        return new Order(ID, userId,products, status, Double.parseDouble(promotionalDiscount));
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
        if (elements.length() == 0) return new ArrayList<>();
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
