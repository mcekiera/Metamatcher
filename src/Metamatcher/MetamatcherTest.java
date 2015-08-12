package Metamatcher;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MetamatcherTest{
    Metamatcher matcher;

    @Before
    public void before(){
        String pattern = "([a-z])\\d+(\\?)";
        matcher = new Metamatcher(pattern);
    }

    @Test
    public void testStart() throws Exception {
        assertEquals(0,new Metamatcher("([a-z])").start(1));
        assertEquals(6,new Metamatcher("(a(a(a(a))))").start(4));
        assertEquals(8, new Metamatcher("(?:a(a(a(a))))").start(3));
        assertEquals(11,new Metamatcher("(?:a)(a(a(a(a))))").start(4));
        assertEquals(7,new Metamatcher("(?=(a(a(a))))").start(3));
    }

    @Test
    public void testEnd() throws Exception {
        assertEquals(7,new Metamatcher("([a-z])").end(1));
        assertEquals(9,new Metamatcher("(a(a(a(a))))").end(4));
        assertEquals(11,new Metamatcher("(?:a(a(a(a))))").end(3));
        assertEquals(14,new Metamatcher("(?:a)(a(a(a(a))))").end(4));
        assertEquals(10,new Metamatcher("(?=(a(a(a))))").end(3));
    }

    @Test
    public void testGroup() throws Exception {
        assertEquals("([a-z])",new Metamatcher("([a-z])").group(1));
        assertEquals("(a)",new Metamatcher("(a(a(a(a))))").group(4));
        assertEquals("(a)",new Metamatcher("(?:a)(a(a(a(a))))").group(4));
        assertEquals("(a(a))",new Metamatcher("(?=(a(a(a))))").group(2));
        assertEquals("[a-z]",new Metamatcher("[a-z]").group(0));
        assertEquals("(a)",new Metamatcher("(?<=(a))(?=\\d)").group(1));
    }

    @Test
    public void testGroupCount() throws Exception {
        assertEquals(1,new Metamatcher("([a-z])").groupCount());
        assertEquals(4,new Metamatcher("(a(a(a(a))))").groupCount());
        assertEquals(3,new Metamatcher("(?:a(a(a(a))))").groupCount());
        assertEquals(4,new Metamatcher("(?:a)(a(a(a(a))))").groupCount());
        assertEquals(3,new Metamatcher("(?=(a(a(a))))").groupCount());
        assertEquals(0,new Metamatcher("[a-z]").groupCount());
        assertEquals(1,new Metamatcher("(?<=(a))(?=\\d)").groupCount());
    }

    @Test
    public void testToString() throws Exception {

    }

    @Test
    public void testGetGroups() throws Exception {

    }

    @Test
    public void testIsCapturingGroup() throws Exception {

    }

    @Test
    public void testReplaceWithSpaces() throws Exception {

    }
}