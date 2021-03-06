package jmri.jmrit.ussctc;

import java.util.*;

import jmri.*;
import jmri.util.JUnitUtil;

import org.junit.Assert;
import org.junit.jupiter.api.*;

/**
 * Tests for OccupancyLock classes in the jmri.jmrit.ussctc package
 *
 * @author Bob Jacobsen Copyright 2007
 */
public class OccupancyLockTest {

    @Test
    public void testEmpty() {
        ArrayList<NamedBeanHandle<Sensor>> list = new ArrayList<>();

        OccupancyLock lock = new OccupancyLock(list);

        Assert.assertTrue(lock.isLockClear(Lock.turnoutLockLogger));
    }

    @Test
    public void testOneInListPass() throws JmriException {
        ArrayList<NamedBeanHandle<Sensor>> list = new ArrayList<>();

        Sensor s = InstanceManager.getDefault(jmri.SensorManager.class).provideSensor("IS1");
        NamedBeanHandle<Sensor> h = InstanceManager.getDefault(NamedBeanHandleManager.class).getNamedBeanHandle("IS1", s);

        list.add(h);
        s.setState(Sensor.INACTIVE);

        OccupancyLock lock = new OccupancyLock(list);

        Assert.assertTrue(lock.isLockClear(Lock.turnoutLockLogger));
    }

    @Test
    public void testOneFailActive() throws JmriException {
        ArrayList<NamedBeanHandle<Sensor>> list = new ArrayList<>();

        Sensor s = InstanceManager.getDefault(jmri.SensorManager.class).provideSensor("IS1");
        NamedBeanHandle<Sensor> h = InstanceManager.getDefault(NamedBeanHandleManager.class).getNamedBeanHandle("IS1", s);

        list.add(h);
        s.setState(Sensor.ACTIVE);

        OccupancyLock lock = new OccupancyLock(list);

        Assert.assertTrue(!lock.isLockClear(Lock.turnoutLockLogger));
    }

    @Test
    public void testOneFailStringArrayCtor() throws JmriException {

        InstanceManager.getDefault(jmri.SensorManager.class).provideSensor("IS1");

        OccupancyLock lock = new OccupancyLock(new String[]{"IS1"});

        Assert.assertTrue(!lock.isLockClear(Lock.turnoutLockLogger));
    }

    @Test
    public void testOneFailSingleStringCtor() throws JmriException {

        InstanceManager.getDefault(jmri.SensorManager.class).provideSensor("IS1");

        OccupancyLock lock = new OccupancyLock("IS1");

        Assert.assertTrue(!lock.isLockClear(Lock.turnoutLockLogger));
    }

    @Test
    public void testSecondFailActive() throws JmriException {
        ArrayList<NamedBeanHandle<Sensor>> list = new ArrayList<>();

        Sensor s = InstanceManager.getDefault(jmri.SensorManager.class).provideSensor("IS1");
        NamedBeanHandle<Sensor> h = InstanceManager.getDefault(NamedBeanHandleManager.class).getNamedBeanHandle("IS1", s);

        list.add(h);
        s.setState(Sensor.INACTIVE);

        s = InstanceManager.getDefault(jmri.SensorManager.class).provideSensor("IS2");
        h = InstanceManager.getDefault(NamedBeanHandleManager.class).getNamedBeanHandle("IS2", s);

        list.add(h);
        s.setState(Sensor.ACTIVE);

        OccupancyLock lock = new OccupancyLock(list);

        Assert.assertTrue(!lock.isLockClear(Lock.turnoutLockLogger));
    }

    @Test
    public void testOneFailInconsistent() throws JmriException {
        ArrayList<NamedBeanHandle<Sensor>> list = new ArrayList<>();

        Sensor s = InstanceManager.getDefault(jmri.SensorManager.class).provideSensor("IS1");
        NamedBeanHandle<Sensor> h = InstanceManager.getDefault(NamedBeanHandleManager.class).getNamedBeanHandle("IS1", s);

        list.add(h);
        s.setState(Sensor.INCONSISTENT);

        OccupancyLock lock = new OccupancyLock(list);

        Assert.assertTrue(!lock.isLockClear(Lock.turnoutLockLogger));
    }

    @Test
    public void testOneFailUnknown() throws JmriException {
        ArrayList<NamedBeanHandle<Sensor>> list = new ArrayList<>();

        Sensor s = InstanceManager.getDefault(jmri.SensorManager.class).provideSensor("IS1");
        NamedBeanHandle<Sensor> h = InstanceManager.getDefault(NamedBeanHandleManager.class).getNamedBeanHandle("IS1", s);

        list.add(h);
        s.setState(Sensor.UNKNOWN);

        OccupancyLock lock = new OccupancyLock(list);

        Assert.assertTrue(!lock.isLockClear(Lock.turnoutLockLogger));
    }

    @BeforeEach
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetProfileManager();
        JUnitUtil.initConfigureManager();
        JUnitUtil.initInternalSensorManager();
    }

    @AfterEach
    public void tearDown() {
        JUnitUtil.tearDown();
    }

}
