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

    public static void appendFile(String path, String line) {
        try {
            FileWriter writer = new FileWriter(path, true);
            writer.write(line+"\n");
            writer.close();
        } catch(Exception err) {
            System.out.println(err.getMessage());
        }
    }
}
