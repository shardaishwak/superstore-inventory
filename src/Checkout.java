import java.util.Scanner;

public class Checkout {
    private String name;
    private String address;
    private long PHnum;
    private long Cnum;

    public void CheackoutDisplay() {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the name: ");
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
                PHnum = s.nextLong();

                if (WithValidation && !PhisValid(PHnum)) {
                    throw new Exception("Invalid Phone Number. Must have 10 digits");
                }

                return PHnum;
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
                Cnum = s.nextLong();

                if (WithValidation && !CardIsValid(Cnum)) {
                    throw new Exception("Invalid Card Number.");
                }

                return Cnum;
            } catch(Exception err) {
                System.out.println("ERROR: " + err.getMessage());
            }
        }
    }


//check if a card is valid: if the sum of odd places + 2 sum of even places is a multiple of 10 = valid
    public static boolean CardIsValid(long Cnum) {
        // check if the prefix is valid

        int evenSum = CreditCardValidator.sumOfDoubleEvenPlaces(Cnum);
        int oddSum = CreditCardValidator.sumOfOddPlaces(Cnum);

        boolean areDigitsValid = (evenSum + oddSum) % 10 == 0;

        boolean isPrefixMatch = (
                CreditCardValidator.prefixMatch(Cnum, 4) ||
                CreditCardValidator.prefixMatch(Cnum, 5) ||
                CreditCardValidator.prefixMatch(Cnum, 35) ||
                CreditCardValidator.prefixMatch(Cnum, 6));

        return areDigitsValid && isPrefixMatch;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getPHnum() {
        return PHnum;
    }

    public void setPHnum(long PHnum) {
        this.PHnum = PHnum;
    }

    public long getCnum() {
        return Cnum;
    }

    public void setCnum(long cnum) {
        Cnum = cnum;
    }
}





