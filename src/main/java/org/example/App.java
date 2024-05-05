package org.example;

import java.io.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;

/**
 * RegexProject
 *
 */
public class App 
{
    private static final String INPUT_FILENAME = "input.txt";
    private static final String OUTPUT_FILENAME = "output.txt";

    static Logger logger = Logger.getLogger(App.class.getName());
    public static void main(String[] args) {

        ArrayList<String> lines;
        ArrayList<String> resultLines = new ArrayList<>();

        //чтение входящего файла
        lines = readInputFile();

        for (String line : lines)
        {
            line = line.trim();
            logger.info(String.format("input line: %s", line));

            String[] fields = line.split("\\|");
            if (fields.length != 4 || anyEmpty(fields)) {
                logger.info("Invalid line format");
                continue;
            }

            //имя
            String name = fields[0];
            //возраст
            String age = fields[1];
            //телефон
            String tel = fields[2];
            //почта
            String mail = fields[3];

            Pattern namePattern = Pattern.compile("([^|\\s]+ *[^|]+)");
            Pattern agePattern = Pattern.compile("\\d+");
            Pattern telPattern = Pattern.compile("([+]?\\(?(\\d\\W*){3}\\)?[-\\s.]?(\\d\\W*){3}[-\\s.]?(\\d\\W*){4,6})");
            Pattern mailPattern = Pattern.compile("[^@ \\t\\r\\n]+[^.]@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+");

            name = cleanString(name, namePattern, 1);
            name = (name.startsWith("OK")) ? formatName(name): "";

            age = cleanString(age, agePattern, 1);
            age = (age.startsWith("OK"))? age.split("\\|")[1]: "";

            tel = cleanString(tel, telPattern, 0);
            tel = (tel.startsWith("OK"))? formatPhoneNumber(tel): "";

            mail = cleanString(mail, mailPattern, 1);
            mail = (mail.startsWith("OK"))? mail.split("\\|")[1]: "";

            String resultLine = name + "|" + age + "|" + tel + "|" + mail;
            resultLines.add(resultLine);
            logger.info(String.format("output line: %s", resultLine));
        }

        //запись в файл результатов
        writeOutputFile(resultLines);

    }

    private static ArrayList<String> readInputFile() {
        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(App.INPUT_FILENAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                lines.add(line);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "trouble read input file", e);
        }

        return lines;
    }

    private static void writeOutputFile(ArrayList<String> resultLines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(App.OUTPUT_FILENAME))) {
            for (String resultLine : resultLines) {
                writer.write(resultLine);
                writer.newLine();
            }
        }
        catch (IOException e) {
            logger.log(Level.WARNING, "trouble write output file", e);
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

    private static String cleanString(String field, Pattern pattern, int countDoubleSymb) {
        String doubles = ". @()+-";
        for (char doubleSymbol : doubles.toCharArray()) {
            field = field.replaceAll("[" + doubleSymbol + "]+",  String.valueOf(doubleSymbol).repeat(countDoubleSymb));
        }
        field = field.trim();
        String status = "ERR";
        if (pattern.matcher(field).matches()) {
            status = "OK";
        }
        return status + "|" + field;
    }

    private static String formatName(String name) {
        name = name.split("\\|")[1];

        Pattern pattern = Pattern.compile("([А-ЯA-Z][^А-ЯA-Z])");
        Matcher matcher = pattern.matcher(name);
        name = matcher.replaceAll(" $1").trim();

        String[] nameParts = name.split(" ");
        StringBuilder nameBuilder = new StringBuilder();
        for (String part : nameParts) {
            if (!Objects.equals(part, "")) {
                part = part.trim().substring(0, 1).toUpperCase() + part.trim().substring(1).toLowerCase();
                nameBuilder.append(part).append(" ");
            }

        }
        name = nameBuilder.toString().trim();
        return name;
    }

    private static String formatPhoneNumber(String phoneNumber) {
        String[] parts = phoneNumber.split("\\|");
        if (parts[0].equals("OK")) {
            String[] digits = parts[1].replaceAll("\\D", "").split("");
            if (digits[0].equals("8")) {
                digits[0] = "7";
            }

            String num1 = String.join("",digits).substring(1,4);
            String num2 = String.join("",digits).substring(4,7);
            String num3 = String.join("",digits).substring(7);

            return String.format("+%s (%s) %s-%s", digits[0], num1,num2,num3);
        }
        return phoneNumber;
    }
}
