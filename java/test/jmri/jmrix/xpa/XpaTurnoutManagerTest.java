package jmri.jmrix.xpa;

import jmri.util.JUnitUtil;
import org.junit.Assert;
import org.junit.jupiter.api.*;

/**
 * Tests for the jmri.jmrix.xpa.XpaTurnoutManager class
 *
 * @author Paul Bender Copyright (C) 2012,2016
 */
public class XpaTurnoutManagerTest extends jmri.managers.AbstractTurnoutMgrTestBase {

    private XpaSystemConnectionMemo memo = null;

    @Override
    public String getSystemName(int i) {
        return "PT" + i;
    }


    @Test
    public void testCtor() {
        Assert.assertNotNull(l);
    }

    @Override
    @BeforeEach
    public void setUp() {
        JUnitUtil.setUp();
        memo = new XpaSystemConnectionMemo();
        memo.setXpaTrafficController(new XpaTrafficController());
        l = new XpaTurnoutManager(memo);
    }

    @AfterEach
    public void tearDown() {
        memo = null;
        l = null;
        JUnitUtil.tearDown();
    }

}
