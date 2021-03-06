package jmri.jmrix.rps.swing.polling;

import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.Assume;
import org.netbeans.jemmy.operators.JFrameOperator;

import jmri.jmrix.rps.RpsSystemConnectionMemo;

/**
 * Tests for the jmri.jmrix.rps.swing.polling package.
 *
 * @author Bob Jacobsen Copyright 2008
 */
public class PollTableActionTest {

    private RpsSystemConnectionMemo memo = null;

    // Show the window
    @Test
    public void testDisplay() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        new PollTableAction(memo).actionPerformed(null);
        // confirm window was created
        JFrame f = JFrameOperator.waitJFrame("RPS Polling Control", true, true);
        Assert.assertNotNull("found frame", f);
        f.dispose();
    }

    @BeforeEach
    public void setUp(){
        jmri.util.JUnitUtil.setUp();
        jmri.util.JUnitUtil.resetProfileManager();
        jmri.util.JUnitUtil.initRosterConfigManager();
        memo = new RpsSystemConnectionMemo();
    }

    @AfterEach
    public void tearDown() {
        memo = null;
        jmri.util.JUnitUtil.clearShutDownManager();
        jmri.util.JUnitUtil.tearDown();
    }
}
