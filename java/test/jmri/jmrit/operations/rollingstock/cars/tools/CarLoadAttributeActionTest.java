package jmri.jmrit.operations.rollingstock.cars.tools;

import java.awt.GraphicsEnvironment;
import jmri.jmrit.operations.rollingstock.cars.tools.CarLoadAttributeAction;
import jmri.jmrit.operations.rollingstock.cars.tools.CarLoadEditFrame;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Paul Bender Copyright (C) 2017	
 */
public class CarLoadAttributeActionTest {

    @Test
    public void testCTor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        CarLoadEditFrame f = new CarLoadEditFrame();
        CarLoadAttributeAction t = new CarLoadAttributeAction("Test Action",f);
        Assert.assertNotNull("exists",t);
    }

    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }

    // private final static Logger log = LoggerFactory.getLogger(CarLoadAttributeActionTest.class);

}