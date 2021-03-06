package jmri.jmrix.can.adapters.lawicell;

import jmri.jmrix.can.CanReply;
import jmri.util.JUnitUtil;

import org.junit.Assert;
import org.junit.jupiter.api.*;

/**
 * Tests for the jmri.jmrix.can.adapters.lawicell.Reply class
 *
 * @author Bob Jacobsen Copyright 2008, 2009
 * @author Steve Young Copyright 2022 ( added RTR Can Frame support )
*/
public class ReplyTest extends jmri.jmrix.AbstractMessageTest {

    // t123412345678
    @Test
    public void testOne() {

        Reply g = new Reply("t123412345678\r");

        CanReply r = g.createReply();

        Assert.assertEquals("extended", false, r.isExtended());
        Assert.assertEquals("rtr", false, r.isRtr());
        Assert.assertEquals("header", 0x123, r.getHeader());
        Assert.assertEquals("num elements", 4, r.getNumDataElements());
        Assert.assertEquals("el 0", 0x12, r.getElement(0));
        Assert.assertEquals("el 1", 0x34, r.getElement(1));
        Assert.assertEquals("el 2", 0x56, r.getElement(2));
        Assert.assertEquals("el 3", 0x78, r.getElement(3));
    }

    // T0000F00D0
    @Test
    public void testTwo() {

        Reply g = new Reply("T0000F00D0\r");

        CanReply r = g.createReply();

        Assert.assertEquals("extended", true, r.isExtended());
        Assert.assertEquals("rtr", false, r.isRtr());
        Assert.assertEquals("header", 0xF00D, r.getHeader());
        Assert.assertEquals("num elements", 0, r.getNumDataElements());
    }

    @Test
    public void testThree() {

        Reply g = new Reply("T00000123412345678\r");

        CanReply r = g.createReply();

        Assert.assertEquals("extended", true, r.isExtended());
        Assert.assertEquals("rtr", false, r.isRtr());
        Assert.assertEquals("header", 0x123, r.getHeader());
        Assert.assertEquals("num elements", 4, r.getNumDataElements());
        Assert.assertEquals("el 0", 0x12, r.getElement(0));
        Assert.assertEquals("el 1", 0x34, r.getElement(1));
        Assert.assertEquals("el 2", 0x56, r.getElement(2));
        Assert.assertEquals("el 3", 0x78, r.getElement(3));
    }

    @Test
    public void testRtRStandard() {
        Reply g = new Reply("r1230\r");
        
        CanReply r = g.createReply();
        Assertions.assertNotNull(r,"canreply created from lawicell reply");
        Assert.assertEquals("extended", false, r.isExtended());
        Assert.assertEquals("rtr", true, r.isRtr());
        Assert.assertEquals("header", 0x123, r.getHeader());
        Assert.assertEquals("num elements", 0, r.getNumDataElements());
    }

    @Test
    public void testRtRExtended() {
        Reply g = new Reply("R0000F00D0\r");
        
        CanReply r = g.createReply();
        Assertions.assertNotNull(r,"canreply extended created from lawicell reply");
        Assert.assertEquals("extended", true, r.isExtended());
        Assert.assertEquals("rtr", true, r.isRtr());
        Assert.assertEquals("header", 0xF00D, r.getHeader());
        Assert.assertEquals("num elements", 0, r.getNumDataElements());
    }

    @Test
    public void testIgnoreFrame() {
        Reply g = new Reply("B"); // random letter
        CanReply r = g.createReply();
        Assertions.assertNull(r,"no canframe created");
    }

    @Override
    @BeforeEach
    public void setUp() {
        JUnitUtil.setUp();
        m = new Reply("t123412345678\r");
    }

    @AfterEach
    @Override
    public void tearDown() {
        m = null;
        JUnitUtil.tearDown();
    }
}
