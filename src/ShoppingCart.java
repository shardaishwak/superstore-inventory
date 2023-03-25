import java.util.ArrayList;
import java.util.Scanner;

// Shopping cart is an order that has status in-progress
// same model, just status change
public class ShoppingCart {
    private String orderId;
    private String userId;

    public ShoppingCart(String userId) {
        this.userId = userId;
        ArrayList<Order> orders = Orders.getShoppingCart(userId);
        Order shopping_cart;
        // create a new order in-progress for a new user
        if (orders.size() == 0) {
            shopping_cart = new Order(userId);
            Orders.appendOrder(shopping_cart);
        } else {
            shopping_cart = orders.get(0);
        }
        this.orderId = shopping_cart.getId();
    }

    public Order getCart() {
        return Orders.getOrder(this.orderId);
    }

    public void addProductToCartWithInputs() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the product ID: ");
        String productId = scanner.nextLine();
        System.out.println("Enter the quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        Orders.appendProductToOrder(this.orderId, productId, quantity);
    }

    public void removeProductFromCartWithInputs() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the product ID: ");
        String productId = scanner.nextLine();


        Orders.removeProductFromOrder(this.orderId, productId);
    }

    public void checkout() {
        Order cart = getCart();
        if (cart.getProducts().size() == 0) {
            System.out.println("Nothing to checkout. Cart is empty.");
            return;
        }
        System.out.println();
        System.out.println("Here is the summary");
        Products.tabloidPrint(cart.getProducts());

        System.out.println("Total cost: " + cart.getTotalPrice() + "CA$");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Would you like to proceed with the checkout (y/n)? ");
        String option = scanner.nextLine();
        if (!option.equals("y")&& !option.equals("yes")) {
            System.out.println("Checkout cancelled. All the products are in the cart. No debit.");
            return;
        }


        // Initialize the credit card class
        Checkout checkout = new Checkout();
        checkout.CheckoutDisplay();


        System.out.println("Processing...");
        System.out.println("Processing...success.");




        Orders.updateStatusToComplete(this.orderId);
        System.out.println("Checkout completed.");
        System.out.println("===============================");
        System.out.println("Thank you for shopping with us.");
    }

    public void print() {
        Order cart = getCart();
        System.out.println("SHOPPING CART");

        System.out.println("Products: ");
        Products.tabloidPrint(cart.getProducts());

        System.out.println("Total cost: " + cart.getTotalPrice() + "CA$");
    }
}

