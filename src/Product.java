public class Product {
    private String name;
    private String description;
    private String ID;
    private String category;
    private int quantity;
    private double price;
    // Percentage value of the discount
    private double discount;

    public Product(String ID, String name, String description, int quantity, double price, double discount, String category) {
        this.name = name;
        this.ID = ID;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.category = category;
    }
    public Product(String ID, String name, String description, int quantity, double price, String category) {
        this.name = name;
        this.ID = ID;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.discount = 0;
        this.category = category;
    }

    public Product(String name, String description, int quantity, double price, String category) {
        this(Utilities.generateUUID("product"), name, description, quantity, price, category);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price * (1-this.discount);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString() {
        // order matters: from DB
        return toString("~");
    }
    // Stringify the product using custom delimiter
    public String toString(String delimiter) {
        return this.ID+delimiter+this.name+delimiter+this.description+delimiter+this.quantity+delimiter+this.price+delimiter+this.discount+delimiter+this.category;
    }

    // Parsing the products that were stringified before suing toString(delimiter) method
    public static Product parse(String line, String delimiter) {
        String data[] = line.split(delimiter);

        // Getting the data from the line
        String ID = data[0];
        String name = data[1];
        String description = data[2];
        String quantity = data[3];
        String price = data[4];
        String discount = data[5];
        String category = data[6];

        return new Product(ID, name, description, Integer.valueOf(quantity), Double.valueOf(price), Double.valueOf(discount), category);
    }
}
