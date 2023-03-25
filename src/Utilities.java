import java.io.FileWriter;

public class Utilities {
    public static String generateUUID() {
        int randomSeed = (int)(Math.random()*Math.pow(10, 8));
        return String.valueOf(randomSeed);
    }
    public static String generateUUID(String name) {
        int randomSeed = (int)(Math.random()*Math.pow(10, 8));
        return name.replace(' ', '-') + "-" + randomSeed;
    }

    public static void printOptions(String[][] options) {
        int maxLengthCommand = "Command".length();
        int maxLengthAction = "Description".length();
        int maxLengthFormat = "Format".length();

        for (String[] option : options) {
            if (option[0].length() > maxLengthCommand) maxLengthCommand = option[0].length();
            if (option[1].length() > maxLengthAction) maxLengthAction = option[1].length();
            if (option[2].length() > maxLengthFormat) maxLengthFormat = option[2].length();
        }

        int totalLength = 3 + maxLengthAction+2+maxLengthCommand+2+maxLengthFormat+2;
        String delimiter = getDelimiterByWidth(totalLength);
        String format = "|"+"%-"+(maxLengthCommand+2)+"s"+"|"+"%-"+(maxLengthAction+2)+"s"+"|"+"%-"+(maxLengthFormat+2)+"s\n";


        System.out.println();
        System.out.printf(format, "Command", "Description", "Format");
        System.out.println(delimiter);
        for (String[] option : options) {
            if (Utilities.joinArray(option).trim().equals("")) {
                System.out.println(delimiter);
            }
            else if (option[0].equals("") && option[2].equals("")) {
                System.out.printf(" "+"%-"+(maxLengthCommand+2)+"s"+" "+"%-"+(maxLengthAction+2)+"s"+" "+"%-"+(maxLengthFormat+2)+"s\n", option[0], option[1], option[2]);
            }
            else System.out.printf(format, option[0], option[1], option[2]);
        }
        System.out.println(delimiter);
        System.out.println();

    }

    public static String joinArray(String[] values) {
        return joinArray(values, 0, values.length);
    }
    public static String joinArray(String[] values, int from) {
        return joinArray(values, from, values.length);
    }
    public static String joinArray(String[] values, int from, int to) {
        String str = "";
        for (int i = from; i < to; i++) {
            str+=values[i];
            if (i < to-1) str+=" ";
        }
        return str;
    }

    public static String[] sliceArray(String[] values, int from) {
        String[] str = new String[values.length-from];
        int j = 0;
        for (int i = from; i < values.length; i++) {
            str[j] = values[i];
            j++;
        }
        return str;
    }
    public static String getDelimiterByWidth(int width) {
        String str = "";
        for (int i = 0 ; i < width; i++) str+="-";
        return str;
    }
}
