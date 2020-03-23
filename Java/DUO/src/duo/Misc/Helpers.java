package duo.Misc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Helpers {

    public static String readFile(final String file) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        final StringBuilder stringBuilder = new StringBuilder();
        final String ls = System.getProperty("line.separator");

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

    public static void leave() {
        System.exit(0);
    }
}
