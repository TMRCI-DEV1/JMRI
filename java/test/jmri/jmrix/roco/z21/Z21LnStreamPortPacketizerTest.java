package jmri.jmrix.roco.z21;

import jmri.jmrix.loconet.LocoNetSystemConnectionMemo;
import jmri.util.JUnitUtil;

import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Tests for jmri.jmrix.loconet.streamport.StreamPortPacketizer
 *
 * @author Bob Jacobsen Copyright (C) 2002
 * @author Paul Bender Copyright (C) 2018
 */
public class Z21LnStreamPortPacketizerTest extends jmri.jmrix.loconet.streamport.LnStreamPortPacketizerTest {

    private LocoNetSystemConnectionMemo memo;
    private Z21LnStreamPortController apc;

    private DataOutputStream ostream;  // Traffic controller writes to this

    private DataInputStream istream;   // so the traffic controller can read from this

    @Override
    @BeforeEach
    public void setUp() {
        JUnitUtil.setUp();
        memo = new LocoNetSystemConnectionMemo();
        lnp = new Z21LnStreamPortPacketizer(memo);
        memo.setLnTrafficController(lnp);
        try {
           PipedInputStream tempPipe;
           tempPipe = new PipedInputStream();
           ostream = new DataOutputStream(new PipedOutputStream(tempPipe));

           tempPipe = new PipedInputStream();
           istream = new DataInputStream(tempPipe);
           apc = new Z21LnStreamPortController(memo, istream, ostream, "Test Stream Port");
       } catch (java.io.IOException ioe) {
           Assert.fail("failed to initialize port controller");
       }
    }

    @Override
    @AfterEach
    public void tearDown() {
        memo.dispose();
        memo = null;
        lnp.terminateThreads();
        lnp = null;
        apc.dispose();
        apc = null;
        istream = null;
        ostream = null;
        JUnitUtil.tearDown();
    }

    @Override
    @Test
    @Disabled("may be causing hang on travis and appveyor")
    public void testStartThreads() {
       ((Z21LnStreamPortPacketizer)lnp).connectPort(apc);
       lnp.startThreads();
    }

}
