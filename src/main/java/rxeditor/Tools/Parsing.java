package rxeditor.Tools;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import rxeditor.Models.Datatype;
import rxeditor.Models.SQLValue;

/**
 * Class containing tools related to text parsing.
 */
public class Parsing {

    /**
     * Uses Pattern and Matcher to look for matches using the given regular
     * expression in a string. When a match is found, its index range is stored
     * in the HashMap returned by the method.
     *
     * @param regex Regular expression to be used for matching.
     * @param source The string to be searched for matches.
     * @return HashMap containing the index ranges of all matches in the string.
     * Key represents match start position, value represents match end position.
     *
     */
    public static HashMap<Integer, Integer> matchRanges(String regex, String source) {
        if (tryPatternCompile(regex) && !regex.isEmpty()) {
            HashMap<Integer, Integer> ranges = new HashMap<>();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(source);
            while (matcher.find()) {
                ranges.put(matcher.start(), matcher.end());
            }
            return ranges;
        }
        return null;
    }

    /**
     * Returns a string containing all regex matches in the parameter string
     * separated by newlines.
     *
     * @param regex Regular expression to be used for matching.
     * @param source The string to be searched for matches.
     * @return String containing all matches separated by newlines.
     */
    public static String extractMatches(String regex, String source) {
        if (tryPatternCompile(regex) && !regex.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(source);
            boolean matched = false;
            while (matcher.find()) {
                sb.append(source.substring(matcher.start(), matcher.end()));
                sb.append("\n");
                matched = true;
            }
            if (matched) {
                sb.replace(sb.length() - 1, sb.length(), "");
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * Generates a CSV/DSV-style string from the source string given as a
     * parameter.
     *
     * @param source Source text.
     * @param regex Regular expression used for determining delimiter positions.
     * @param delimiter Delimiter character to be used for separating values.
     * @param beginning If true, delimiter will be placed before the regex
     * match.
     * @param both If true, delimiter will be placed before and after the regex
     * match. If "beginning" and "both" are false, delimiter will be placed
     * after the regex match.
     * @return
     */
    public static String generateCSV(String source, String regex, String delimiter, boolean beginning, boolean both) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String replacement = matcher.group().trim();
            if (beginning) {
                matcher.appendReplacement(buffer, delimiter + replacement);
            } else if (both) {
                matcher.appendReplacement(buffer, delimiter + replacement + delimiter);
            } else {
                matcher.appendReplacement(buffer, replacement + delimiter);
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    /**
     * A match and replace method. Replaces regex matches in the source string
     * with the value of the replacement string.
     *
     * @param source Source text to be searched for matches.
     * @param regex Regular expression to be used for matching.
     * @param replacement The string matches will be replaced with.
     * @return The produced string.
     */
    public static String matchAndReplace(String source, String regex, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, replacement);
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    /**
     * Generates SQL insert statements based on CSV/DSV data. Automatically
     * inserts NULL in columns missing from String[] "values".
     *
     * @param source Source text.
     * @param delimiter Column delimiter character sequence.
     * @param tablename Table name in SQL INSERT statement.
     * @param columns Number of columns.
     * @param serial If true, 'default' will be inserted in the first column.
     * @param datatypes Column data types in an ordered list.
     * @return SQL insert statement(s).
     */
    public static String generateSQL(String source, String delimiter,
            String tablename, int columns, boolean serial,
            List<Datatype> datatypes) {
        StringBuilder sb = new StringBuilder();
        String[] lines = source.split("\n");
        for (int currentLine = 0; currentLine < lines.length; currentLine++) {
            String[] values = lines[currentLine].split(delimiter, columns);
            sb.append("INSERT INTO ");
            sb.append(tablename);
            if (serial) {
                sb.append(" VALUES (default, ");
            } else {
                sb.append(" VALUES (");
            }
            for (int currentValue = 0; currentValue < values.length; currentValue++) {
                SQLValue columnValue = new SQLValue(values[currentValue].trim(), datatypes.get(currentValue));
                sb.append(columnValue.toString());
                if (currentValue < values.length - 1) {
                    sb.append(", ");
                } else {
                    if (values.length < columns) {
                        for (int diff = 0; diff < columns - values.length; diff++) {
                            sb.append(", NULL");
                        }
                    }
                    sb.append(");");
                    if (currentLine < lines.length - 1) {
                        sb.append("\n");
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * Method for determining if a string can be parsed as an integer.
     *
     * @param value The string to be parsed.
     * @return True if value may successfully be converted to int.
     */
    public static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Method for determining if a string can be compiled as a regular
     * expression.
     *
     * @param regex The string to be compiled.
     * @return True if string can be compiled.
     */
    public static boolean tryPatternCompile(String regex) {
        try {
            Pattern.compile(regex);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

}
