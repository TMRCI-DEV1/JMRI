package jmri.jmrix.maple.nodeconfig;

import java.awt.GraphicsEnvironment;

import jmri.jmrix.maple.MapleSystemConnectionMemo;
import jmri.util.JUnitUtil;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.Assume;

/**
 * Test simple functioning of NodeConfigFrame
 *
 * @author Paul Bender Copyright (C) 2016
 */
public class NodeConfigFrameTest {

    private MapleSystemConnectionMemo memo = null;

    @Test
    public void testMemoCtor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        NodeConfigFrame action = new NodeConfigFrame(memo);
        Assert.assertNotNull("exists", action);
    }

    @Test
    public void testInitComponents() throws Exception{
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        NodeConfigFrame t = new NodeConfigFrame(memo);
        // for now, just makes sure there isn't an exception.
        t.initComponents();
        t.dispose();
    }

    @Test
    public void testGetTitle(){
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        NodeConfigFrame t = new NodeConfigFrame(memo);
        t.initComponents();
        Assert.assertEquals("title", "Configure Maple Nodes", t.getTitle());
        t.dispose();
    }

    @BeforeEach
    public void setUp() {
        JUnitUtil.setUp();
        jmri.util.JUnitUtil.resetProfileManager();

        memo = new MapleSystemConnectionMemo();
    }

    @AfterEach
    public void tearDown() {

        memo.dispose();
        JUnitUtil.clearShutDownManager();
        JUnitUtil.tearDown();
    }
}
