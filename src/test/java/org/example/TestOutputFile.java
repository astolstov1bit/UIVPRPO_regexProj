package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class TestOutputFile {


    private static final String OUTPUT_FILENAME = "output.txt";
    private static final String OUTPUT_REFNAME = "output - reference.txt";
    String currentDir = System.getProperty("user.dir");

    @Test
    public void TestAppOutputEqualsRef() throws IOException {
        App.main(null);
        String pathOutFileApp = Paths.get(currentDir, OUTPUT_FILENAME).toString();
        String pathOutFileRef = Paths.get(currentDir, OUTPUT_REFNAME).toString();
        assertTrue(compareFiles(pathOutFileApp, pathOutFileRef));
    }


    public static boolean compareFiles(String file1Path, String file2Path) throws IOException {
        try (BufferedReader reader1 = new BufferedReader(new FileReader(file1Path));
             BufferedReader reader2 = new BufferedReader(new FileReader(file2Path))) {

            String line1 = reader1.readLine();
            String line2 = reader2.readLine();

            while (line1 != null && line2 != null) {
                if (!line1.equals(line2)) {
                    return false;
                }
                line1 = reader1.readLine();
                line2 = reader2.readLine();
            }

            // Check if both files have reached EOF
            return line1 == null && line2 == null;
        }

    }
}