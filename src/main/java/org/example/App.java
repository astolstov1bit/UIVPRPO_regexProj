package org.example;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RegexProject
 *
 */
public class App 
{
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

                name = cleanString(name, namePattern, 1);
                if (name.startsWith("OK")) {
                    name = formatName(name);
                } else {
                    name = "";

                }

                age = cleanString(age, agePattern, 1);
                if (age.startsWith("OK")) {
                    age = age.split("\\|")[1];
                } else {
                    age = "";

                }

                tel = cleanString(tel, telPattern, 0);
                if (tel.startsWith("OK")) {
                    tel = formatPhoneNumber(tel);
                } else {
                    tel = "";
                }

                mail = cleanString(mail, mailPattern, 1);
                if (mail.startsWith("OK")) {
                    mail = mail.split("\\|")[1];
                } else {
                    mail = "";
                }

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
            if (part != "") {
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
