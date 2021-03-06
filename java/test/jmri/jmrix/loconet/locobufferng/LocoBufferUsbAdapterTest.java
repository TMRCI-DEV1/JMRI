package jmri.jmrix.loconet.locobufferng;

import jmri.util.JUnitUtil;

import org.junit.Assert;
import org.junit.jupiter.api.*;

/**
 *
 * @author Paul Bender Copyright (C) 2017, 2021
 */
public class LocoBufferUsbAdapterTest {

    @Test
    public void testCTor() {
        LocoBufferNGAdapter t = new LocoBufferNGAdapter();
        Assert.assertNotNull("exists",t);
    }

    @BeforeEach
    public void setUp() {
        JUnitUtil.setUp();
    }

    @AfterEach
    public void tearDown() {
        JUnitUtil.tearDown();
    }

    // private final static Logger log = LoggerFactory.getLogger(LocoBufferNGAdapterTest.class);

}
