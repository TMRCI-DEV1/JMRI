package jmri.jmrit.logixng.implementation.swing;

import java.io.IOException;

import jmri.util.JUnitUtil;

import org.junit.*;

/**
 * Test ErrorHandlingDialog_MultiLine
 * 
 * @author Daniel Bergqvist 2021
 */
public class ErrorHandlingDialog_MultiLineTest {

    @Test
    public void testCtor() {
        ErrorHandlingDialog_MultiLine obj = new ErrorHandlingDialog_MultiLine();
        Assert.assertNotNull(obj);
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() throws IOException {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initConfigureManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initLogixNGManager();
    }

    @After
    public void tearDown() {
        jmri.jmrit.logixng.util.LogixNG_Thread.stopAllLogixNGThreads();
        JUnitUtil.tearDown();
    }
    
}