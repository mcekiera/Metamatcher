package Metamatcher;


import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class MetamatcherTest{
    Metamatcher matcher;

    @Before
    public void before(){
        String pattern = "([a-z])\\d+(\\?)";
        matcher = new Metamatcher(pattern);
    }

    @Test
    public void testStart() throws Exception {
        assertEquals(0,matcher.usePattern("([a-z])").start(1));
        assertEquals(6,matcher.usePattern("(a(a(a(a))))").start(4));
        assertEquals(8, matcher.usePattern("(?:a(a(a(a))))").start(3));
        assertEquals(11,matcher.usePattern("(?:a)(a(a(a(a))))").start(4));
        assertEquals(7,matcher.usePattern("(?=(a(a(a))))").start(3));
    }

    @Test
    public void testEnd() throws Exception {
        assertEquals(7,matcher.usePattern("([a-z])").end(1));
        assertEquals(9,matcher.usePattern("(a(a(a(a))))").end(4));
        assertEquals(11,matcher.usePattern("(?:a(a(a(a))))").end(3));
        assertEquals(14,matcher.usePattern("(?:a)(a(a(a(a))))").end(4));
        assertEquals(10,matcher.usePattern("(?=(a(a(a))))").end(3));
    }

    @Test
    public void testGroup() throws Exception {
        assertEquals("([a-z])",matcher.usePattern("([a-z])").group(1));
        assertEquals("(a)",matcher.usePattern("(a(a(a(a))))").group(4));
        assertEquals("(a)",matcher.usePattern("(?:a)(a(a(a(a))))").group(4));
        assertEquals("(a(a))",matcher.usePattern("(?=(a(a(a))))").group(2));
        assertEquals("[a-z]", matcher.usePattern("[a-z]").group(0));
        assertEquals("(a)",matcher.usePattern("(?<=(a))(?=\\d)").group(1));
    }

    @Test
    public void testGroupCount() throws Exception {
        assertEquals(1,matcher.usePattern("([a-z])").groupCount());
        assertEquals(4,matcher.usePattern("(a(a(a(a))))").groupCount());
        assertEquals(3,matcher.usePattern("(?:a(a(a(a))))").groupCount());
        assertEquals(4,matcher.usePattern("(?:a)(a(a(a(a))))").groupCount());
        assertEquals(3,matcher.usePattern("(?=(a(a(a))))").groupCount());
        assertEquals(0,matcher.usePattern("[a-z]").groupCount());
        assertEquals(1,matcher.usePattern("(?<=(a))(?=\\d)").groupCount());
    }

    @Test
    public void testToString() throws Exception {
        String toString = "Groups count: 1\n" +
                "group(0) 0-8\t([a-z])+\n" +
                "group(1) 0-7\t([a-z])\n";
        assertEquals(toString,matcher.usePattern("([a-z])+").toString());
    }

    @Test
    public void testGetGroups() throws Exception {
        TreeMap<Integer,Integer> test = new TreeMap<Integer, Integer>();
        test.put(0,8);
        test.put(3,7);
        test.put(8,12);
        assertEquals(test, matcher.usePattern("(aa(cv))(at)").getGroups());

    }

    @Test
    public void testIsCapturingGroup() throws Exception {
        assertTrue(matcher.isCapturingGroup("([a-z])"));
        assertTrue(matcher.isCapturingGroup("(?<name>[a-z])"));
        assertFalse(matcher.isCapturingGroup("(?:[a-z])"));
        assertFalse(matcher.isCapturingGroup("(?=[a-z])"));
        assertFalse(matcher.isCapturingGroup("(?![a-z])"));
        assertFalse(matcher.isCapturingGroup("(?<=[a-z])"));
        assertFalse(matcher.isCapturingGroup("(?<![a-z])"));
        assertFalse(matcher.isCapturingGroup("(?>[a-z])"));
        assertFalse(matcher.isCapturingGroup("\\(?>[a-z])"));
    }

    @Test
    public void testReplaceWithSpaces() throws Exception {
        assertEquals("       ", matcher.replaceWithSpaces("([a-z])"));
        assertEquals("    ", matcher.replaceWithSpaces("(az)"));
        assertEquals("          ", matcher.replaceWithSpaces("([a-z0-9])"));
        assertEquals("  ", matcher.replaceWithSpaces("()"));
    }

    @Test
    public void testUseStringPattern() throws Exception {
        Metamatcher meta = new  Metamatcher("([a-z])+");
        meta.usePattern("(\\d+:\\d+)");
        assertEquals("(\\d+:\\d+)", meta.getPattern());
    }

    @Test
    public void testUsePatternClassPattern1() throws Exception {
        Metamatcher meta = new  Metamatcher("([a-z])+");
        Pattern pattern = Pattern.compile("(\\d+:\\d+)");
        meta.usePattern(pattern);
        assertEquals("(\\d+:\\d+)", meta.getPattern());
    }

    @Test
    public void testGetPattern() throws Exception {
        Metamatcher meta = new  Metamatcher("([a-z])+");
        assertEquals("([a-z])+",meta.getPattern());
    }

    @Test
    public void testStartByName() throws Exception {
        assertEquals(0,matcher.usePattern("(?<name>regex)").start("name"));
    }

    @Test
    public void testEndByName() throws Exception {
        assertEquals(14,matcher.usePattern("(?<name>regex)").end("name"));

    }

    @Test
    public void testGroupByName() throws Exception {
        assertEquals(("(?<name>regex)"),matcher.usePattern("(?<name>regex)").group("name"));

    }

    @Test
    public void testIsNamedGroup() throws Exception {
        assertTrue(matcher.isNamedGroup("(?<name>regex)"));
        assertFalse(matcher.isNamedGroup("(regex)"));
    }

    @Test
    public void testGetNamedGroup() throws Exception {
        assertEquals("name",matcher.getNamedGroup("(?<name>regex)"));
    }
}