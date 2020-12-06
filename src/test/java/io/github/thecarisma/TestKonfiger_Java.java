package io.github.thecarisma;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class TestKonfiger_Java {

    @Test
    public void validateKonfigerStringStreamEntries() {
        Konfiger konfiger = new Konfiger("\n" +
                "String=This is a string\n" +
                "Number=215415245\n" +
                "Float=56556.436746\n" +
                "Boolean=true\n", false);

        Assert.assertEquals(konfiger.get("String"), "This is a string");
        Assert.assertEquals(konfiger.get("Number"), "215415245");
        Assert.assertEquals(konfiger.get("Float"), "56556.436746");
        Assert.assertNotEquals(konfiger.get("Number"), "true");
        Assert.assertEquals(konfiger.get("Boolean"), "true");
        konfiger.put("String", "This is an updated string");
        Assert.assertEquals(konfiger.get("String"), "This is an updated string");
    }

    @Test
    public void Validate_Konfiger_Entries_Get_Method() {
        Konfiger konfiger = new Konfiger(new File("src/test/resources/test.config.ini"));
        konfiger.put("One", konfiger);
        konfiger.put("Two", "\"hello\", \"world\"");
        konfiger.put("Three", 3);
        konfiger.putInt("Four", 4);
        konfiger.putBoolean("Five", true);
        konfiger.put("Six", false);
        konfiger.put("Seven", "121251656.1367367263726");
        konfiger.putFloat("Eight", 0.21f);

        Assert.assertNotEquals(konfiger.get("One"), konfiger.toString());
        Assert.assertEquals(konfiger.get("Two"), "\"hello\", \"world\"");
        Assert.assertEquals(konfiger.get("Three"), "3");
        Assert.assertEquals(konfiger.get("Four"), "4");
        Assert.assertEquals(konfiger.get("Five"), "true");
        Assert.assertEquals(konfiger.get("Six"), "false");
        Assert.assertEquals(konfiger.get("Seven"), "121251656.1367367263726");
        Assert.assertEquals(konfiger.get("Eight"), "0.21");
    }

    @Test
    public void Validate_LazyLoad_Konfiger_Entries_Get_With_Fallback() {
        Konfiger konfiger = new Konfiger(new File("src/test/resources/test.config.ini"), true);

        Assert.assertEquals(konfiger.get("Occupation", "Pen Tester"), "Software Engineer");
        Assert.assertEquals(konfiger.get("Hobby", "Worm Creation"), "i don't know");
        Assert.assertNull(konfiger.get("Fav OS"));
        Assert.assertNotNull(konfiger.get("Fav OS", "Whatever get work done"));
    }

    @Test
    public void Validate_Konfiger_Entries_Get_Returned_Types() {
        Konfiger konfiger = new Konfiger("");
        konfiger.put("One", konfiger);
        konfiger.putLong("Two", 123456789);
        konfiger.putBoolean("Bool", true);
        konfiger.putFloat("Float", 123.56F);
        konfiger.putString("Dummy", "Noooooo 1");
        konfiger.putString("Dummy2", "Noooooo 2");

        Assert.assertEquals(konfiger.get("Two"), "123456789");
        Assert.assertEquals(konfiger.getLong("Two"), 123456789);
        Assert.assertNotEquals(konfiger.getLong("Two"), "123456789");

        Assert.assertEquals(konfiger.get("Bool"), "true");
        Assert.assertFalse(konfiger.getBoolean("Two"));
        Assert.assertNotEquals(konfiger.getBoolean("Two"), true);
        Assert.assertNotEquals(konfiger.getBoolean("Two"), "true");

        Assert.assertEquals(konfiger.get("Float"), "123.56");
        Assert.assertEquals(konfiger.getFloat("Float"), 123.56F, 0.00f);
        Assert.assertNotEquals(konfiger.getFloat("Float"), "123.56");
    }

    @Test
    public void Validate_Konfiger_Default_Value_For_Non_Existing_Key() {
        Konfiger konfiger = new Konfiger("");

        Assert.assertNull(konfiger.get("Name"));
        Assert.assertNotEquals(konfiger.getString("Name"), null);
        Assert.assertEquals(konfiger.getString("Name"), "");
        Assert.assertNotEquals(konfiger.get("Name", "Adewale Azeez"), null);
        Assert.assertEquals(konfiger.get("Name", "Adewale Azeez"), "Adewale Azeez");
        Assert.assertFalse(konfiger.getBoolean("CleanupOnClose"));
        Assert.assertNotEquals(konfiger.getBoolean("CleanupOnClose", true), false);
        Assert.assertEquals(konfiger.getLong("TheNumber"), 0);
        Assert.assertEquals(konfiger.getLong("TheNumber", 123), 123);
        Assert.assertEquals(konfiger.getFloat("TheNumber"), 0.0, 0.0F);
        Assert.assertNotEquals(konfiger.getFloat("TheNumber"), 0.1);
    }

    @Test
    public void testRemoveEntrySize() {
        Konfiger konfiger = new Konfiger("One=111,Two=222,Three=333", true, '=', ',');

        Assert.assertEquals(konfiger.size(), 3);
        Assert.assertNotEquals(konfiger.get("Two"), null);
        Assert.assertEquals(konfiger.remove("Two"), "222");
        Assert.assertNull(konfiger.get("Two"));
        Assert.assertEquals(konfiger.size(), 2);
        Assert.assertEquals(konfiger.remove(0), "111");
        Assert.assertEquals(konfiger.size(), 1);
        Assert.assertEquals(konfiger.get("Three"), "333");
    }

    @Test
    public void testLazySize() {
        Konfiger konfiger = new Konfiger("One=111,Two=222,Three=333", true, '=', ',');

        Assert.assertEquals(konfiger.lazySize(), 0);
        Assert.assertNotEquals(konfiger.lazySize(), 3);
        Assert.assertEquals(konfiger.get("One"), "111");
        Assert.assertEquals(konfiger.lazySize(), 1);
        Assert.assertEquals(konfiger.get("Two"), "222");
        Assert.assertEquals(konfiger.lazySize(), 2);
        Assert.assertEquals(konfiger.get("Three"), "333");
        Assert.assertEquals(konfiger.lazySize(), 3);
        Assert.assertEquals(konfiger.lazySize(), konfiger.size());
    }

    @Test
    public void testRemoveEntryAndLazySize() {
        Konfiger konfiger = new Konfiger("One=111,Two=222,Three=333", true, '=', ',');

        Assert.assertEquals(konfiger.lazySize(), 0);
        Assert.assertNotEquals(konfiger.lazySize(), 3);
        Assert.assertNotEquals(konfiger.get("Two"), null);
        Assert.assertEquals(konfiger.lazySize(), 2);
        Assert.assertEquals(konfiger.remove("Two"), "222");
        Assert.assertNotEquals(konfiger.lazySize(), 2);
        Assert.assertEquals(konfiger.lazySize(), 1);
        Assert.assertNull(konfiger.get("Two"));
        Assert.assertEquals(konfiger.lazySize(), 2);
        Assert.assertEquals(konfiger.remove(0), "111");
        Assert.assertEquals(konfiger.lazySize(), 1);
        Assert.assertEquals(konfiger.get("Three"), "333");
        Assert.assertEquals(konfiger.lazySize(), 1);
    }

    @Test
    public void Set_Get_Delimeter_And_Seperator() {
        Konfiger konfiger = new Konfiger(new File("src/test/resources/test.config.ini"), true);

        Assert.assertEquals(konfiger.getSeparator(), '\n');
        Assert.assertEquals(konfiger.getDelimiter(), '=');
        Assert.assertTrue(konfiger.toString().split("\n").length > 2);
        konfiger.setSeparator('-');
        konfiger.setDelimiter('+');
        Assert.assertEquals(konfiger.getSeparator(), '-');
        Assert.assertEquals(konfiger.getDelimiter(), '+');
        Assert.assertEquals(konfiger.toString().split("\n").length, 1);
    }

    @Test
    public void Escaping_And_Unescaping_Entries_And_Save() {
        KonfigerStream ks = new KonfigerStream(new File("src/test/resources/test.config.ini"));
        KonfigerStream ks1 = new KonfigerStream(new File("src/test/resources/test.txt"), ':',  ',');
        Konfiger konfiger = new Konfiger(ks, true);
        Konfiger konfiger1 = new Konfiger(ks1, true);

        Assert.assertEquals(konfiger.get("Hobby"), "i don't know");
        Assert.assertEquals(konfiger1.get("Hobby"), konfiger.get("Hobby"));
        Assert.assertEquals(konfiger1.get("Hobby"), "i don't know");
        konfiger.save("src/test/resources/test.config.ini");

        KonfigerStream newKs = new KonfigerStream(new File("src/test/resources/test.config.ini"));
        Konfiger newKonfiger = new Konfiger(newKs, true);
        Konfiger newKonfiger1 = new Konfiger(new File("src/test/resources/test.txt"), true, ':',  ',');
        Assert.assertEquals(konfiger.toString(), newKonfiger.toString());
        Assert.assertEquals(konfiger1.toString(), newKonfiger1.toString());
    }

    @Test
    public void Test_Complex_And_Confusing_Seperator() {
        Konfiger konfiger = new Konfiger("Occupation=Software En^gineergLocation=Ni^geriagState=La^gos", false, '=', 'g');

        Assert.assertEquals(konfiger.size(), 3);
        Assert.assertTrue(konfiger.toString().contains("^g"));
        for (Map.Entry<String, String> entry : konfiger.entries()) {
            Assert.assertFalse(entry.getValue().contains("^g"));
        }
        konfiger.setSeparator('f');
        Assert.assertEquals(konfiger.get("Occupation"), "Software Engineer");
        konfiger.setSeparator('\n');
        Assert.assertFalse(konfiger.toString().contains("^g"));
        Assert.assertEquals(konfiger.size(), 3);
        for (Map.Entry<String, String> entry : konfiger.entries()) {
            Assert.assertFalse(entry.getValue().contains("\\g"));
        }
    }

    @Test
    public void Append_New_Unparsed_Entries_From_String_And_File() {
        Konfiger konfiger = new Konfiger("");

        Assert.assertEquals(konfiger.size(), 0);
        konfiger.appendString("Language=English");
        Assert.assertEquals(konfiger.size(), 1);
        Assert.assertNull(konfiger.get("Name"));
        Assert.assertNotEquals(konfiger.get("Name"), "Adewale Azeez");
        Assert.assertEquals(konfiger.get("Language"), "English");

        konfiger.appendFile(new File("src/test/resources/test.config.ini"));
        Assert.assertNotEquals(konfiger.get("Name"), null);
        Assert.assertEquals(konfiger.get("Name"), "Adewale Azeez");
    }

    @Test
    public void Test_Prev_And_Current_Cache() {
        Konfiger konfiger = new Konfiger("");

        konfiger.put("Name", "Adewale");
        konfiger.put("Project", "konfiger");
        konfiger.putInt("Year", 2020);

        Assert.assertEquals(konfiger.getInt("Year"), 2020);
        Assert.assertEquals(konfiger.get("Project"), "konfiger");
        Assert.assertEquals(konfiger.get("Name"), "Adewale");
        Assert.assertEquals(konfiger.getInt("Year"), 2020);
        Assert.assertEquals(konfiger.currentCachedObject[0], "Name");
        Assert.assertEquals(konfiger.prevCachedObject[0], "Year");
        Assert.assertEquals(konfiger.currentCachedObject[1], "Adewale");
        Assert.assertEquals(konfiger.prevCachedObject[1], "2020");
        Assert.assertEquals(konfiger.get("Name"), "Adewale");
        Assert.assertEquals(konfiger.get("Name"), "Adewale");
        Assert.assertEquals(konfiger.get("Project"), "konfiger");
        Assert.assertEquals(konfiger.get("Name"), "Adewale");
        Assert.assertEquals(konfiger.get("Name"), "Adewale");
        Assert.assertEquals(konfiger.get("Name"), "Adewale");
        Assert.assertEquals(konfiger.currentCachedObject[0], "Project");
        Assert.assertEquals(konfiger.prevCachedObject[0], "Name");
        Assert.assertEquals(konfiger.currentCachedObject[1], "konfiger");
        Assert.assertEquals(konfiger.prevCachedObject[1], "Adewale");
    }

    @Test
    public void testTheSinglePairCommentingInStringStreamKonfiger() {
        KonfigerStream ks = new KonfigerStream("Name:Adewale Azeez,;Project:konfiger,Date:April 24 2020", ':', ',');
        Konfiger kon = new Konfiger(ks);
        for (String key : kon.keys()) {
            Assert.assertNotEquals(kon.getString(key), "Project");
        }
        Assert.assertEquals(kon.size(), 2);
    }

    @Test
    public void Test_Contains_With_Lazy_Load() {
        KonfigerStream ks = new KonfigerStream(new File("src/test/resources/test.comment.inf"));
        ks.setCommentPrefixes("[", ";", "//", "@", "<>");
        Konfiger kon = new Konfiger(ks, true);

        Assert.assertTrue(kon.contains("File"));
        Assert.assertTrue(kon.contains("Project"));
        Assert.assertTrue(kon.contains("Author"));
    }

    @Test
    public void Read_Multiline_Entry_From_File_Stream() {
        KonfigerStream ks = KonfigerStream.builder()
                .withFile(new File("src/test/resources/test.contd.conf"))
                .ignoreInlineComment()
                .build();
        Konfiger kon = new Konfiger(ks, true);

        Assert.assertTrue(kon.getString("ProgrammingLanguages").indexOf("Kotlin, NodeJS, Powershell, Python, Ring, Rust") > 0);
        Assert.assertEquals(kon.get("ProjectName"), "konfiger");
        Assert.assertTrue(kon.getString("Description").endsWith(" in other languages and off the Android platform."));
    }

    @Test
    public void Check_Size_In_LazyLoad_And_No_LazyLoad() {
        KonfigerStream ks = KonfigerStream.builder()
                .withFile(new File("src/test/resources/test.contd.conf"))
                .ignoreInlineComment()
                .build();
        Konfiger kon = new Konfiger(ks, false);
        KonfigerStream ks1 = KonfigerStream.builder()
                .withFile(new File("src/test/resources/test.contd.conf"))
                .ignoreInlineComment()
                .build();
        Konfiger kon1 = new Konfiger(ks1, true);

        Assert.assertTrue(kon.size() > 0);
        Assert.assertTrue(kon1.size() > 0);
        Assert.assertFalse(kon.isEmpty());
        Assert.assertFalse(kon1.isEmpty());
        Assert.assertEquals(kon1.size(), kon1.size());
    }

    @Test
    public void Check_putComment_In_The_Konfiger_Object() {
        Konfiger kon = new Konfiger("Name:Adewale Azeez,//Project:konfiger,Date:April 24 2020", false, ':', ',');
        kon.putComment("A comment at the end");

        Assert.assertTrue(kon.toString().contains("//:A comment"));
    }

    @Test
    public void Validate_Konfiger_Entries_With_Case_Sensitivity() {
        Konfiger kon = new Konfiger("String=This is a string\n" +
                "Number=215415245");

        kon.setCaseSensitivity(true);
        Assert.assertTrue(kon.isCaseSensitive());
        try {
            Assert.assertEquals(kon.get("STRING"), "This is a string");
            Assert.assertEquals(kon.get("NUMBER"), "215415245");
            Assert.assertEquals(1, 0);
        } catch (AssertionError ex) {
            Assert.assertTrue(true);
        }

        kon.setCaseSensitivity(false);
        Assert.assertFalse(kon.isCaseSensitive());
        Assert.assertEquals(kon.get("STRING"), "This is a string");
        Assert.assertEquals(kon.get("NUMBER"), "215415245");

        Assert.assertEquals(kon.get("strING"), "This is a string");
        Assert.assertEquals(kon.get("nuMBer"), "215415245");

        Assert.assertEquals(kon.get("STRiNg"), "This is a string");
        Assert.assertEquals(kon.get("nUMbeR"), "215415245");

        Assert.assertEquals(kon.get("string"), "This is a string");
        Assert.assertEquals(kon.get("number"), "215415245");
    }

    @Test
    public void Check_The_UpdateAt_Method() {
        Konfiger kon = new Konfiger("Name:Adewale Azeez,//Project:konfiger,Date:April 24 2020", false, ':', ',');

        Assert.assertEquals(kon.get("Date"), "April 24 2020");
        Assert.assertEquals(kon.get("Name"), "Adewale Azeez");
        kon.updateAt(1, "12 BC");
        kon.updateAt(0, "Thecarisma");
        Assert.assertEquals(kon.get("Date"), "12 BC");
        Assert.assertEquals(kon.get("Name"), "Thecarisma");
    }

    @Test
    public void Save_Content_And_Validate_Saved_Content() {
        Konfiger kon = new Konfiger("Name=Adewale Azeez,Date=April 24 2020,One=111,Two=222,Three=333", false, '=', ',');

        Assert.assertEquals(kon.size(), 5);
        kon.save("src/test/resources/konfiger.conf");
        Konfiger kon2 = new Konfiger(new File("src/test/resources/konfiger.conf"), false, '=', ',');
        Assert.assertEquals(kon.toString(), kon.toString());
        Assert.assertEquals(kon2.size(), 5);
    }

    @Test
    public void testEntryListener() {
        EntryListener entryListener = new EntryListener() {
            @Override
            public boolean entryAdded(String section, String key, String value) {
                return false;
            }

            @Override
            public boolean entryRemoved(String section, String key, String value) {
                return false;
            }

            @Override
            public boolean entryChanged(String section, String key, String value, String newValue) {
                return false;
            }
        };
    }

}
