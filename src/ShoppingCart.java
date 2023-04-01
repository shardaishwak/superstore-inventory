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

        System.out.println("Total cost: " + String.format("%.2f", cart.getTotalPrice()) + " CA$");

        double totalAfterPromotion = promotionApplication();
        cart.setPromotionDiscount(totalAfterPromotion);

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

        System.out.println("Total cost: " + String.format("%.2f", cart.getTotalPrice()) + " CA$");
    }

    public double promotionApplication(){
        Order cart = getCart();
        String[][] promotions = {
                {"GET25", "Get 25% off on orders above $300  ", ""},
                {"SEASON50", "Get 50% off on orders above $5000(limited season offer)", ""},
                {"HELLO100", "Get $100 off on orders above $1000",""},
                {"GOOD20","Get $20 off on orders above 100",""},
                {"none","",""}
        };
        Utilities.printOptions(promotions);

        Double totalAmount =cart.getTotalPrice();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the promotion code: ");
        String fullCommand = scanner.nextLine();
        String promotion = fullCommand.split(" ")[0];

        double promotionalDiscountPercentage = 0;
        double promotionalDiscountAmount = 0;


        switch(promotion){
            case "GET25" : {
                if (totalAmount > 300) {
                    totalAmount = totalAmount - (totalAmount * 0.25);
                    promotionalDiscountPercentage = 0.25;
                } else {
                    System.out.println("You need a total amount of more than $300 to apply this promotion!");
                }
                break;
            }

            case "SEASON50":{
                if (totalAmount > 5000) {
                    totalAmount = totalAmount - (totalAmount * 0.5);
                    promotionalDiscountPercentage = 0.5;
                } else {
                    System.out.println("You need a total amount of more than $5000 to apply this promotion!");
                }
                break;
            }

            case "HELLO100":{
                if (totalAmount > 1000) {
                    totalAmount = totalAmount - 100;
                    promotionalDiscountAmount = 100;
                }
                else {
                    System.out.println("You need a total amount of more than $1000 to apply this promotion!");
                }
                break;
            }

            case "GOOD20":{
                if (totalAmount > 100) {
                    totalAmount = totalAmount - 20;
                    promotionalDiscountAmount = 20;
                }
                else {
                    System.out.println("You need a total amount of more than $100 to apply this promotion!");
                }
                break;
            }

            case "none":{
                break;
            }
        }
        double overallAmount = cart.getTotalPrice()*promotionalDiscountPercentage + promotionalDiscountAmount;



        System.out.println("Overall promotional discount percentage: " + (promotionalDiscountPercentage*100) + "%");
        System.out.println("Overall promotional discount amount: " + (promotionalDiscountAmount) + " CA$");
        System.out.println("You saved: " + overallAmount + " CA$");
        System.out.println("Final cost after promotions: " + String.format("%.2f", totalAmount) + " CA$");

        return overallAmount;
        }

    }



