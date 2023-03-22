public class CreditCardValidator {

    // Get the number of digits in the card
    public static int getSize(long number) {
        int digits = 0;
        while(number > 0) {
            number /= 10;
            digits++;

        }
        return digits;
    }

    // Get the first k digits for the card
    public static long getPrefix(long number, int k) {
        int totalDigits = getSize(number);
        if (k >= totalDigits) return number;

        while(totalDigits != k) {
            number/=10;
            totalDigits--;
        }
        return number;
    }

    // Check if number d is prefix of the card
    public static boolean prefixMatch(long number, int d) {
        int prefix_match_digits = getSize(d);
        long prefix = getPrefix(number, prefix_match_digits);

        return prefix == d;
    }

    // Return 1 digit or return sum of all digits if number has more than 1 digit
    public static int getDigit(int number) {
        if (number < 10) return number;

        int sum = 0;
        while(number > 0) {
            sum+=number%10;
            number/=10;
        }

        return sum;
    }

    // Return the sum of even places beginning from right. All doubled
    public static int sumOfDoubleEvenPlaces(long number) {
        int track = 0;

        int sum = 0;
        while(number > 0) {
            int currentDigit = (int) (number % 10);

            if (track == 1) {
                // do the sum by getting the digit
                sum+=getDigit((currentDigit)*2);
            }
            number/=10;
            track^=1;
        }
        return sum;
    }

    // Return the sum of odd places starting from right
    public static int sumOfOddPlaces(long number) {
        int track = 0;

        int sum = 0;
        while(number > 0) {
            int currentDigit = (int) (number % 10);

            if (track == 0) {
                // do the sum by getting the digit
                sum+=getDigit((currentDigit));
            }
            number/=10;
            track^=1;
        }
        return sum;
    }
}
