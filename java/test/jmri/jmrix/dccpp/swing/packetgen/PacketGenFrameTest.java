package jmri.jmrix.dccpp.swing.packetgen;

import java.awt.GraphicsEnvironment;

import jmri.util.JUnitUtil;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.Assume;

/**
 * Test simple functioning of PacketGenFrame
 *
 * @author Paul Bender Copyright (C) 2016
 */
public class PacketGenFrameTest {

    jmri.jmrix.dccpp.DCCppSystemConnectionMemo memo = null;

    @Test
    public void testCtor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        PacketGenFrame action = new PacketGenFrame(memo);
        Assert.assertNotNull("exists", action);
    }

    @BeforeEach
    public void setUp() {
        JUnitUtil.setUp();
        jmri.jmrix.dccpp.DCCppInterfaceScaffold t = new jmri.jmrix.dccpp.DCCppInterfaceScaffold(new jmri.jmrix.dccpp.DCCppCommandStation());
        memo = new jmri.jmrix.dccpp.DCCppSystemConnectionMemo(t);

        jmri.InstanceManager.store(memo, jmri.jmrix.dccpp.DCCppSystemConnectionMemo.class);
    }

    @AfterEach
    public void tearDown() {        
        JUnitUtil.clearShutDownManager(); // put in place because AbstractMRTrafficController implementing subclass was not terminated properly
        JUnitUtil.resetWindows(false,false);
        JUnitUtil.tearDown();
    }
}
