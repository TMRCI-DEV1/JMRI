package jmri.jmrit.vsdecoder;

import jmri.util.JUnitUtil;

import org.junit.Assert;
import org.junit.jupiter.api.*;

/**
 *
 * @author Paul Bender Copyright (C) 2017
 */
public class VSDecoderManagerThreadTest {

    @Test
    public void testInstance() {
        VSDecoderManagerThread t = VSDecoderManagerThread.instance();
        Assert.assertNotNull("exists",t);
        // the instance method starts a thread, make sure it goes away.
        t.kill();
    }

    @BeforeEach
    public void setUp() {
        JUnitUtil.setUp();
    }

    @AfterEach
    public void tearDown() {
        JUnitUtil.removeMatchingThreads("VSDecoderManagerThread");
        JUnitUtil.tearDown();
    }

    // private final static Logger log = LoggerFactory.getLogger(VSDecoderManagerThreadTest.class);

}
