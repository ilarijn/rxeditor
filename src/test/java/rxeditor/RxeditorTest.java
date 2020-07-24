package rxeditor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import rxeditor.Tools.Storage;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import rxeditor.Models.Datatype;
import rxeditor.Models.Entry;
import rxeditor.Models.SQLValue;
import rxeditor.Tools.Parsing;

public class RxeditorTest {

    @Test
    public void matchRangesAreCorrect() {
        String text = "abc\nabcd\nabcde";
        HashMap<Integer, Integer> expectedRanges = new HashMap<>();
        expectedRanges.put(0, 2);
        expectedRanges.put(4, 6);
        expectedRanges.put(9, 11);
        HashMap<Integer, Integer> actualRanges = Parsing.matchRanges("ab", text);
        assertEquals(expectedRanges.get(0), actualRanges.get(0));
        assertEquals(expectedRanges.get(4), actualRanges.get(4));
        assertEquals(expectedRanges.get(9), actualRanges.get(9));
        assertEquals(null, Parsing.matchRanges("", ""));
        assertEquals(null, Parsing.matchRanges("[a-z[", ""));
    }

    @Test
    public void extractResultIsCorrect() {
        String text = "abc\nabcd\nabcde";
        String extractResult = Parsing.extractMatches("ab", text);
        assertEquals("ab\nab\nab", extractResult);
        assertEquals(null, Parsing.extractMatches("", ""));
        assertEquals(null, Parsing.extractMatches("[a-z[", ""));
        assertTrue(Parsing.extractMatches("[a-z]", "").isEmpty());
    }

    @Test
    public void csvFormIsCorrect() {
        String text = "abc\nabcd\nabcde";
        String csvResultBeginning = Parsing.generateCSV(text, "ab", ",", true, false);
        String csvResultEnd = Parsing.generateCSV(text, "ab", ",", false, false);
        String csvResultBoth = Parsing.generateCSV(text, "ab", ",", false, true);
        assertEquals(",abc\n,abcd\n,abcde", csvResultBeginning);
        assertEquals("ab,c\nab,cd\nab,cde", csvResultEnd);
        assertEquals(",ab,c\n,ab,cd\n,ab,cde", csvResultBoth);
    }

    @Test
    public void matchAndReplaceWorks() {
        String text = "abc\nabcd\nabcde";
        String replaced = Parsing.matchAndReplace(text, "ab", "xy");
        assertEquals("xyc\nxycd\nxycde", replaced);
    }

    @Test
    public void sqlFormIsCorrect() {
        String text = "1, abc, true\n2,, false\n3, ghi,\n ";
        List<Datatype> datatypes = new ArrayList();
        datatypes.add(Datatype.INT);
        datatypes.add(Datatype.VARCHAR);
        datatypes.add(Datatype.BOOLEAN);
        String sql = Parsing.generateSQL(text, ",", "test table", 3, true, datatypes);
        assertEquals("INSERT INTO test table VALUES (default, 1, 'abc', 'true');\n"
                + "INSERT INTO test table VALUES (default, 2, NULL, 'false');\n"
                + "INSERT INTO test table VALUES (default, 3, 'ghi', NULL);\n"
                + "INSERT INTO test table VALUES (default, NULL, NULL, NULL);",
                sql);
        sql = Parsing.generateSQL(text, ",", "test table", 4, false, datatypes);
        assertEquals("INSERT INTO test table VALUES (1, 'abc', 'true', NULL);\n"
                + "INSERT INTO test table VALUES (2, NULL, 'false', NULL);\n"
                + "INSERT INTO test table VALUES (3, 'ghi', NULL, NULL);\n"
                + "INSERT INTO test table VALUES (NULL, NULL, NULL, NULL);",
                sql);
    }

    @Test
    public void tryParseIntWorks() {
        assertTrue(Parsing.tryParseInt("123"));
        assertFalse(Parsing.tryParseInt("abc"));
    }

    @Test
    public void tryPatternCompileWorks() {
        assertTrue(Parsing.tryPatternCompile("[a-z]"));
        assertFalse(Parsing.tryPatternCompile("[a-z["));
    }

    @Test
    public void saveWorks() {
        Entry entry1 = new Entry("test category", "test name", "ab");
        Entry entry2 = new Entry("", "", "ab");
        assertTrue(Storage.saveRegexToFile(entry1, "test.txt"));
        assertTrue(Storage.saveRegexToFile(entry2, "test.txt"));
    }

    @Test
    public void loadWorks() {
        List<Entry> entries = Storage.loadRegexFile("test.txt");
        boolean isCorrect = false;
        if (entries.get(0).getCategory().equals("TEST CATEGORY")
                && entries.get(0).getName().equals("test name")
                && entries.get(0).getRegex().equals("ab")) {
            isCorrect = true;
        }
        assertTrue(isCorrect);
        File list = new File(System.getProperty("user.dir") + "\\" + "test.txt");
        list.delete();
    }

    @Test
    public void writeFileWorks() {
        File file = new File(System.getProperty("user.dir") + "\\" + "writetest.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            fail("Test file creation failed.");
        }
        assertTrue(Storage.writeFile(file, "abc\ndef", false));
        file.delete();
    }

    @Test
    public void readAndWriteFileWorks() {
        File file = new File(System.getProperty("user.dir") + "\\" + "readtest.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            fail("Test file creation failed.");
        }
        Storage.writeFile(file, "abc\ndef\n", false);
        String contents = Storage.readFile(file);
        assertEquals("abc\ndef\n", contents);

        Storage.writeFile(file, "abc\ndef\n", true);
        contents = Storage.readFile(file);
        assertEquals("abc\ndef\nabc\ndef\n", contents);

        file.delete();
    }

    @Test
    public void getCssPropertiesWorks() {
        String css = Storage.getCSSProperties(".defaultregex").trim();
        assertTrue(css.contains("-fx-font-family: \"Consolas\";"));
        assertTrue(css.contains("-fx-font-size: 13px;"));
    }

    @Test
    public void entryEqualsWorks() {
        Entry entry1 = new Entry("cat", "name", "[a-z]");
        Entry entry2 = new Entry("cat", "name", "[A-Z]");
        Entry entry3 = new Entry("", "name", "[a-z]");
        Entry entry4 = new Entry("", "name", "[A-Z]");
        Entry entry5 = new Entry("", "", "[A-Z]");
        Entry entry6 = new Entry("", "[A-Z]", "[a-z]");
        Entry entry7 = new Entry("", "CAT", "[a-z]");
        String test = "";
        assertTrue(entry1.equals(entry1));
        assertTrue(entry1.equals(entry2));
        assertTrue(entry3.equals(entry4));
        assertFalse(entry1.equals(entry3));
        assertTrue(entry5.equals(entry6));
        assertFalse(entry4.equals(entry6));
        assertFalse(entry5.equals(test));
        assertTrue(entry7.equals(entry1));
        assertFalse(entry3.equals(entry1));
    }

    @Test
    public void testSqlValue() {
        SQLValue value1 = new SQLValue("true", Datatype.BOOLEAN);
        SQLValue value2 = new SQLValue("123", Datatype.DATE);
        SQLValue value3 = new SQLValue("123", Datatype.FLOAT);
        SQLValue value4 = new SQLValue("123", Datatype.INT);
        SQLValue value5 = new SQLValue("abc", Datatype.VARCHAR);
        SQLValue value6 = new SQLValue("", Datatype.VARCHAR);
        assertEquals("'true'", value1.toString());
        assertEquals("'123'", value2.toString());
        assertEquals("123", value3.toString());
        assertEquals("123", value4.toString());
        assertEquals("'abc'", value5.toString());
        assertEquals("NULL", value6.toString());
    }

}
