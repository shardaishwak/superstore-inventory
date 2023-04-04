import java.util.Scanner;

public class Checkout {
    private String name;
    private String address;
    private long PHnum;
    private long Cnum;

    public void CheckoutDisplay() {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the name for the order: ");
        name = s.nextLine();
        System.out.println("Enter the billing Address: ");
        address = s.nextLine();

        PHnum = getPhoneInput(s, true);
        Cnum = getCardInput(s, true);

    }
//for Phone number
    public long getPhoneInput(Scanner s, boolean WithValidation) {
        while(true) {
            try {
                System.out.println("Enter the Phone number: ");
                long phoneNumber = s.nextLong();

                if (WithValidation && !PhisValid(phoneNumber)) {
                    throw new Exception("Invalid Phone Number. Must have 10 digits");
                }

                return phoneNumber;
            } catch(Exception err) {
                System.out.println("ERROR: " + err.getMessage());
            }
        }
    }

    public static boolean PhisValid(long PHnum){
        boolean isTenDigits = false;
        int size = 0;
        while(PHnum > 0) {
            PHnum /= 10;
            size++;
        }
        if (size==10){
            isTenDigits = true;
        }
        if (size!=10){
            isTenDigits = false;
        }
        return isTenDigits;
    }

//    for Card Number
    public long getCardInput(Scanner s, boolean WithValidation) {
        while(true) {
            try {
                System.out.println("Enter the Card number: ");
                long creditCardNumber = s.nextLong();

                if (WithValidation && !CardIsValid(creditCardNumber)) {
                    throw new Exception("Invalid Card Number.");
                }

                return creditCardNumber;
            } catch(Exception err) {
                System.out.println("ERROR: " + err.getMessage());
            }
        }
    }


//check if a card is valid: if the sum of odd places + 2 sum of even places is a multiple of 10 = valid
    public boolean CardIsValid(long Cnum) {
        // check if the prefix is valid

        CreditCardValidator validator = new CreditCardValidator();

        int evenSum = validator.sumOfDoubleEvenPlaces(Cnum);
        int oddSum = validator.sumOfOddPlaces(Cnum);

        boolean areDigitsValid = (evenSum + oddSum) % 10 == 0;

        boolean isPrefixMatch = (
                validator.prefixMatch(Cnum, 4) ||
                        validator.prefixMatch(Cnum, 5) ||
                        validator.prefixMatch(Cnum, 35) ||
                        validator.prefixMatch(Cnum, 6));

        return areDigitsValid && isPrefixMatch;
    }

}





