package jmri.jmrix.openlcb.swing;

import jmri.*;
import jmri.jmrit.beantable.signalmast.SignalMastAddPane;
import jmri.jmrix.openlcb.*;
import jmri.implementation.*;
import jmri.util.*;

import java.util.*;
import javax.swing.*;

import org.junit.*;

import org.netbeans.jemmy.operators.*;

/**
 * @author	Bob Jacobsen Copyright 2018
 */
public class OlcbSignalMastAddPaneTest {

    @Test
    public void testSetMast() {
        OlcbSignalMast s1 = new OlcbSignalMast("IF$vsm:basic:one-searchlight($1)", "user name");
        MatrixSignalMast m1 = new MatrixSignalMast("IF$xsm:basic:one-low($0001)-3t", "user");

        OlcbSignalMastAddPane vp = new OlcbSignalMastAddPane();
        
        Assert.assertFalse(vp.canHandleMast(null));
        Assert.assertTrue(vp.canHandleMast(s1));
        Assert.assertFalse(vp.canHandleMast(m1));
        
        vp.setMast(null);
        vp.setMast(s1);
        vp.setMast(m1);
        JUnitAppender.assertErrorMessage("mast was wrong type: IF$xsm:basic:one-low($0001)-3t jmri.implementation.MatrixSignalMast");

    }

    @Test
    public void testCreateMast() {
        OlcbSignalMastAddPane vp = new OlcbSignalMastAddPane();
        new OlcbSignalMast("IF$vsm:basic:one-searchlight($1)", "no user name");
        
        vp.createMast("AAR-1946", "appearance-PL-2-high.xml", "user name");
                
        Assert.assertNotNull(InstanceManager.getDefault(jmri.SignalMastManager.class).getByUserName("user name"));
        Assert.assertNotNull(InstanceManager.getDefault(jmri.SignalMastManager.class).getBySystemName("IF$vsm:AAR-1946:PL-2-high($0005)"));
        
    }

    @Test
    public void testCreateAndDisableViaGui() {
        Assume.assumeFalse(java.awt.GraphicsEnvironment.isHeadless());
        Assert.assertEquals(0, InstanceManager.getDefault(jmri.SignalMastManager.class).getObjectCount());
        
        OlcbSignalMastAddPane vp = new OlcbSignalMastAddPane();

        vp.setAspectNames(
            java.util.Collections.enumeration(
                java.util.Arrays.asList(
                    new String[]{"Clear","Approach Medium","Advance Approach"})));
        
        JFrame frame = new JFrame("Add/Edit Signal Mast");
        frame.add(vp);
        frame.pack();
        frame.setVisible(true);
        
        JFrameOperator frameOp = new JFrameOperator("Add/Edit Signal Mast");
        JCheckBoxOperator bBox = new JCheckBoxOperator(frameOp, "Approach Medium");
        
        // disable B
        jmri.util.ThreadingUtil.runOnGUI(() -> {
            bBox.push();
            vp.createMast("AAR-1946", "appearance-PL-2-high.xml", "user name 1");
        });

        // check list of SignalMasts
        Assert.assertEquals(1, InstanceManager.getDefault(jmri.SignalMastManager.class).getObjectCount());
        Assert.assertNotNull(InstanceManager.getDefault(jmri.SignalMastManager.class).getByUserName("user name 1"));
        // system name not checked, depends on history of how many SignalMast objects have been created

        // check aspect disabled
        Assert.assertTrue(InstanceManager.getDefault(jmri.SignalMastManager.class).getByUserName("user name 1").isAspectDisabled("Approach Medium"));
        Assert.assertTrue(! InstanceManager.getDefault(jmri.SignalMastManager.class).getByUserName("user name 1").isAspectDisabled("Clear"));

        jmri.util.ThreadingUtil.runOnGUI(() -> {
            frame.dispose();
        });
    }

    @Test
    public void testEditAndDisableViaGui() {
        Assume.assumeFalse(java.awt.GraphicsEnvironment.isHeadless());
        Assert.assertEquals(0, InstanceManager.getDefault(jmri.SignalMastManager.class).getObjectCount());
        OlcbSignalMast mast = new OlcbSignalMast("IF$vsm:basic:one-searchlight($1)", "user name 2");
        InstanceManager.getDefault(jmri.SignalMastManager.class).register(mast);
        Assert.assertEquals(1, InstanceManager.getDefault(jmri.SignalMastManager.class).getObjectCount());
        mast.setAspectDisabled("Stop");
        mast.setAspectDisabled("Unlit"); // we will renable this below
        
        OlcbSignalMastAddPane vp = new OlcbSignalMastAddPane();
        
        vp.setAspectNames(mast.getAllKnownAspects().elements());
        vp.setMast(mast);
              
        JFrame frame = new JFrame("Add/Edit Signal Mast");
        frame.add(vp);
        frame.pack();
        frame.setVisible(true);
        
        JFrameOperator frameOp = new JFrameOperator("Add/Edit Signal Mast");
        JCheckBoxOperator aBox = new JCheckBoxOperator(frameOp, "Approach");
        JCheckBoxOperator uBox = new JCheckBoxOperator(frameOp, "Unlit");
        
        // disable Approach
        jmri.util.ThreadingUtil.runOnGUI(() -> {
            aBox.push(); // this should set disabled
            uBox.push(); // this should set enabled
            vp.createMast("basic", "appearance-one-searchlight.xml", "user name 2");
        });

        // check list of SignalMasts
        Assert.assertEquals(1, InstanceManager.getDefault(jmri.SignalMastManager.class).getObjectCount());
        Assert.assertNotNull(InstanceManager.getDefault(jmri.SignalMastManager.class).getByUserName("user name 2"));
        // system name not checked, depends on history of how many SignalMast objects have been created

        // check correct aspect disabled
        Assert.assertTrue(! InstanceManager.getDefault(jmri.SignalMastManager.class).getByUserName("user name 2").isAspectDisabled("Clear"));
        Assert.assertTrue(InstanceManager.getDefault(jmri.SignalMastManager.class).getByUserName("user name 2").isAspectDisabled("Approach"));
        Assert.assertTrue(InstanceManager.getDefault(jmri.SignalMastManager.class).getByUserName("user name 2").isAspectDisabled("Stop"));
        Assert.assertTrue(! InstanceManager.getDefault(jmri.SignalMastManager.class).getByUserName("user name 2").isAspectDisabled("Unlit"));

        jmri.util.ThreadingUtil.runOnGUI(() -> {
            frame.dispose();
        });
    }

    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.initDefaultUserMessagePreferences();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
}
