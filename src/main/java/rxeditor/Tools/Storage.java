package rxeditor.Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import rxeditor.Models.Entry;

/**
 * Class for methods related to file operations.
 *
 */
public class Storage {

    /**
     * Saves an expression with a name and a category in a file. One line in the
     * file contains one saved expression with its name and category.
     *
     * @param entry Regex to be saved as Entry.
     * @param filename Name of file to save in.
     * @return Boolean value for whether the save was successful.
     */
    public static boolean saveRegexToFile(Entry entry, String filename) {
        try {
            File list = new File(System.getProperty("user.dir") + "\\" + filename);
            if (!list.exists()) {
                list.createNewFile();
            }
            entry.setDelimiter(";:");
            writeFile(list, entry.toString(), true);
            return true;
        } catch (IOException e) {
            System.out.println("Error while saving to file");
            return false;
        }
    }

    /**
     * Returns a list of Entry objects representing regular expressions
     * contained in the lines of a file.
     *
     * @return List of Entry objects.
     */
    public static List<Entry> loadRegexFile(String filename) {
        List<Entry> entries = new ArrayList<>();
        try {
            Files.lines(Paths.get(System.getProperty("user.dir") + "\\" + filename))
                    .map(entry -> entry.split(";:", 3))
                    .forEach(entry -> entries.add(new Entry(entry[0], entry[1], entry[2])));
            return entries;
        } catch (IOException e) {
            System.out.println("Error while loading from file");
            return entries;
        }
    }

    /**
     * Method for reading the contents of a file.
     *
     * @param file The file to be read.
     * @return The contents of the read file as a string.
     */
    public static String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            Files.lines(Paths.get(file.getAbsolutePath())).forEach(line -> {
                sb.append(line);
                sb.append("\n");
            });
        } catch (IOException e) {
            return "";
        }
        return sb.toString();
    }

    /**
     * Method for writing a string to a file.
     *
     * @param file The file to write to.
     * @param content The string to write to the file.
     * @param append Append when true, overwrite when false.
     * @return Returns true if successful.
     */
    public static boolean writeFile(File file, String content, boolean append) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, append), StandardCharsets.UTF_8);
            writer.write(content);
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Method for reading custom style properties that cannot be otherwise
     * assigned from the CSS resource file. It is retrieved as a stream to
     * enable access in jar.
     *
     * @param selector The property name.
     * @return String containing all properties associated with the name given.
     */
    public static String getCSSProperties(String selector) {
        InputStream in = MethodHandles.lookup().lookupClass().
                getClassLoader().getResourceAsStream("editorstyle.css");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String css = reader.lines().collect(Collectors.joining("\n"));
        StringBuilder sb = new StringBuilder();
        Scanner scanner = new Scanner(css);
        while (scanner.hasNext()) {
            String nextline = scanner.nextLine();
            if (nextline.contains(selector)) {
                String propertyline = scanner.nextLine();
                while (!propertyline.trim().equals("}")) {
                    sb.append(propertyline);
                    propertyline = scanner.nextLine();
                }
            }
        }
        return sb.toString();
    }

}
