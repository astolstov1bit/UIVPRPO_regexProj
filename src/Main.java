import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String INPUT_FILENAME = "input.txt";
    private static final String OUTPUT_FILENAME = "output.txt";

    public static void main(String[] args) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILENAME));
            BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILENAME));

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                System.out.println();
                System.out.println("input line: " + line);

                String[] fields = line.split("\\|");
                if (fields.length != 4 || anyEmpty(fields)) {
                    System.out.println("Invalid line format");
                    continue;
                }

                String name = fields[0];
                String age = fields[1];
                String tel = fields[2];
                String mail = fields[3];

                Pattern namePattern = Pattern.compile("([^|\\s]+ *[^|]+)");
                Pattern agePattern = Pattern.compile("[0-9]+");
                Pattern telPattern = Pattern.compile("([\\+]?[(]?([0-9]\\W*){3}[)]?[-\\s\\.]?([0-9]\\W*){3}[-\\s\\.]?([0-9]\\W*){4,6})");
                Pattern mailPattern = Pattern.compile("[^@ \\t\\r\\n]+[^.]@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+");

                String resultLine = name + "|" + age + "|" + tel + "|" + mail;
                writer.write(resultLine);
                writer.newLine();
                System.out.println("output line: " + resultLine);
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static boolean anyEmpty(String[] array) {
        for (String s : array) {
            if (s.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }


}
