public class Product {
    private String name;
    private String description;
    private String ID;
    private String category;
    private int quantity;
    private double price;
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
        return this.ID+"~"+this.name+"~"+this.description+"~"+this.quantity+"~"+this.price+"~"+this.discount+"~"+this.category;
    }
}
