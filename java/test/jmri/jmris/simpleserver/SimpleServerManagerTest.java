package jmri.jmris.simpleserver;

import jmri.InstanceManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the jmri.jmris.simpleserver.SimpleServerManager class
 *
 * @author Paul Bender
 */
public class SimpleServerManagerTest {

    @Test
    public void testGetInstance() {
        SimpleServerManager a = InstanceManager.getDefault(SimpleServerManager.class);
        assertThat(a).isNotNull();
    }

    @Test
    public void testGetPreferences(){
        SimpleServerManager a = InstanceManager.getDefault(SimpleServerManager.class);
        assertThat(a.getPreferences()).withFailMessage("preferences not created").isNotNull();
    }

    @BeforeEach
    public void setUp() {
        jmri.util.JUnitUtil.setUp();
        jmri.util.JUnitUtil.resetProfileManager();
    }

    @AfterEach
    public void tearDown() {
        jmri.util.JUnitUtil.tearDown();

    }

}
