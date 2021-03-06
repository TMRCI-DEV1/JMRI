package jmri.util;

import java.util.TreeSet;

import org.junit.Assert;
import org.junit.jupiter.api.*;

import jmri.*;

/**
 *
 * @author Paul Bender Copyright (C) 2017
 */
public class NamedBeanUserNameComparatorTest {

    @Test
    public void testNonNullUserNameCases() {
        NamedBeanUserNameComparator<Turnout> t = new NamedBeanUserNameComparator<>();

        Turnout it1 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("IT1");
        Turnout it10 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("IT10");
        Turnout it2 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("IT2");
        it1.setUserName(it1.getSystemName());
        it10.setUserName(it10.getSystemName());
        it2.setUserName(it2.getSystemName());

        Assert.assertEquals("IT1 == IT1", 0, t.compare(it1, it1));

        Assert.assertEquals("IT1 < IT2", -1, t.compare(it1, it2));
        Assert.assertEquals("IT2 > IT1", +1, t.compare(it2, it1));

        Assert.assertEquals("IT10 > IT2", +1, t.compare(it10, it2));
        Assert.assertEquals("IT2 < IT10", -1, t.compare(it2, it10));

        TreeSet<Turnout> set = new TreeSet<>(t);
        set.addAll(InstanceManager.getDefault(TurnoutManager.class).getNamedBeanSet());
        Assert.assertArrayEquals(
                new Turnout[]{it1, it2, it10},
                set.toArray(new Turnout[set.size()]));

        it1.setUserName("A");
        it10.setUserName("B");
        it2.setUserName("C");

        Assert.assertEquals("A == A", 0, t.compare(it1, it1));

        Assert.assertEquals("A < C", -1, t.compare(it1, it2));
        Assert.assertEquals("C > A", +1, t.compare(it2, it1));

        Assert.assertEquals("B < C", -1, t.compare(it10, it2));
        Assert.assertEquals("C > B", +1, t.compare(it2, it10));

        set = new TreeSet<>(t);
        set.addAll(InstanceManager.getDefault(TurnoutManager.class).getNamedBeanSet());
        Assert.assertArrayEquals(
                new Turnout[]{it1, it10, it2},
                set.toArray(new Turnout[set.size()]));
    }

    @Test
    public void testOneLetterCases() {
        NamedBeanUserNameComparator<Turnout> t = new NamedBeanUserNameComparator<>();

        Turnout it1 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("IT1");
        Turnout it10 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("IT10");
        Turnout it2 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("IT2");

        Assert.assertEquals("IT1 == IT1", 0, t.compare(it1, it1));

        Assert.assertEquals("IT1 < IT2", -1, t.compare(it1, it2));
        Assert.assertEquals("IT2 > IT1", +1, t.compare(it2, it1));

        Assert.assertEquals("IT10 > IT2", +1, t.compare(it10, it2));
        Assert.assertEquals("IT2 < IT10", -1, t.compare(it2, it10));

        TreeSet<Turnout> set = new TreeSet<>(t);
        set.addAll(InstanceManager.getDefault(TurnoutManager.class).getNamedBeanSet());
        Assert.assertArrayEquals(
                new Turnout[]{it1, it2, it10},
                set.toArray(new Turnout[set.size()]));
    }

    @Test
    public void testTwoLetterCases() {
        NamedBeanUserNameComparator<Turnout> t = new NamedBeanUserNameComparator<>();

        Turnout i2t1 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("I2T1");
        Turnout i2t10 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("I2T10");
        Turnout i2t2 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("I2T2");

        Assert.assertEquals("I2T1 == I2T1", 0, t.compare(i2t1, i2t1));

        Assert.assertEquals("I2T1 < I2T2", -1, t.compare(i2t1, i2t2));
        Assert.assertEquals("I2T2 > I2T1", +1, t.compare(i2t2, i2t1));

        Assert.assertEquals("I2T10 > I2T2", +1, t.compare(i2t10, i2t2));
        Assert.assertEquals("I2T2 < I2T10", -1, t.compare(i2t2, i2t10));
    }

    @Test
    public void testThreeLetterCases() {
        NamedBeanUserNameComparator<Turnout> t = new NamedBeanUserNameComparator<>();

        Turnout i23t1 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("I23T1");
        Turnout i23t10 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("I23T10");
        Turnout i23t2 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("I23T2");

        Assert.assertEquals("I23T1 == I23T1", 0, t.compare(i23t1, i23t1));

        Assert.assertEquals("I23T1 < I23T2", -1, t.compare(i23t1, i23t2));
        Assert.assertEquals("I23T2 > I23T1", +1, t.compare(i23t2, i23t1));

        Assert.assertEquals("I23T10 > I23T2", +1, t.compare(i23t10, i23t2));
        Assert.assertEquals("I23T2 < I23T10", -1, t.compare(i23t2, i23t10));
    }

    @Test
    public void testForUniqueOrdering() {

        // check the ordering of mix of beans with and without user names
        //    IT3 FOO
        //    IT1 XYZ
        //    IT2
        //    IT4

        NamedBeanUserNameComparator<Turnout> t = new NamedBeanUserNameComparator<>();

        Turnout it1xyz = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("IT1");
        it1xyz.setUserName("XYZ");
        Turnout it2 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("IT2");
        Turnout it3foo = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("IT3");
        it3foo.setUserName("FOO");
        Turnout it4 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("IT4");

        Assert.assertEquals("IT3 < IT1", -1, t.compare(it3foo, it1xyz));
        Assert.assertEquals("IT3 < IT2", -1, t.compare(it3foo, it2));
        Assert.assertEquals("IT3 < IT4", -1, t.compare(it3foo, it4));
        Assert.assertEquals("IT1 < IT2", -1, t.compare(it1xyz, it2));
        Assert.assertEquals("IT1 < IT4", -1, t.compare(it1xyz, it4));
        Assert.assertEquals("IT2 < IT4", -1, t.compare(it2, it4));

        TreeSet<Turnout> set = new TreeSet<>(t);
        set.addAll(InstanceManager.getDefault(TurnoutManager.class).getNamedBeanSet());
        Assert.assertArrayEquals(
                new Turnout[]{it3foo, it1xyz, it2, it4},
                set.toArray(new Turnout[set.size()]));
    }

    @Test
    public void testForUniqueOrderingWithLS() {
        ((jmri.managers.ProxySensorManager) InstanceManager.getDefault(SensorManager.class)).getDefaultManager();
        // add an LS manager
        var lsm = new jmri.jmrix.internal.InternalSensorManager(
                    new jmri.jmrix.internal.InternalSystemConnectionMemo("L", "LocoNet"));
        ((jmri.managers.ProxySensorManager) InstanceManager.getDefault(SensorManager.class)).addManager(lsm);

        // Check the ordering of mix of beans with and without user names
        // Expect:
        //  IS102    // due to prefer number comparator
        //  IS 101
        //  LS102
        //  LS 101
        //  ISCLOCKRUNNING


        NamedBeanUserNameComparator<Sensor> t = new NamedBeanUserNameComparator<>();

        Sensor is101 = InstanceManager.getDefault(SensorManager.class).provideSensor("IS101");
        is101.setUserName("IS 101");
        Sensor is102 = InstanceManager.getDefault(SensorManager.class).provideSensor("IS102");
        is102.setUserName("IS102");

        Sensor clock = InstanceManager.getDefault(SensorManager.class).provideSensor ("ISCLOCKRUNNING");

        Sensor ls101 = InstanceManager.getDefault(SensorManager.class).provideSensor("LS101");
        ls101.setUserName("LS 101");
        Sensor ls102 = InstanceManager.getDefault(SensorManager.class).provideSensor("LS102");
        ls102.setUserName("LS102");

        Assert.assertEquals("LS101", ls101.getSystemName());
        Assert.assertEquals("IS101", is101.getSystemName()); // checking that no prefixes were added

        Assert.assertEquals("IS102 < IS101", -1, t.compare(is102, is101));
        Assert.assertEquals("IS101 < ISCLOCKRUNNING", -1, t.compare(is101, clock));

        TreeSet<Sensor> set = new TreeSet<>(t);
        set.addAll(InstanceManager.getDefault(SensorManager.class).getNamedBeanSet());
        Assert.assertArrayEquals(
                new Sensor[]{is102, is101, ls102, ls101, clock},  // wrong order - fail
                set.toArray(new Sensor[set.size()]));
    }

    @Test
    public void testMixedUserNamesSystemNamesCase() {
        NamedBeanUserNameComparator<Turnout> c = new NamedBeanUserNameComparator<>();

        Turnout i23t1 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("I23T1");
        Turnout i23t10 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("I23T10");
        Turnout i23t2 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("I23T2");
        Turnout i23t3 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("I23T3");
        Turnout i23t4 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("I23T4");
        Turnout i23t5 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("I23T5");
        Turnout i23t6 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("I23T6");
        Turnout i23t7 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("I23T7");
        Turnout i23t8 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("I23T8");
        Turnout i23t9 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("I23T9");

        i23t3.setUserName("Name 4");
        i23t4.setUserName("Name 3");
        i23t5.setUserName("A name");

        // expected sort order:
        // i23t5 (A Name)
        // i23t4 (Name 3)
        // i23t3 (Name 4)
        // i23t1
        // i23t2
        // i23t6
        // i23t7
        // i23t8
        // i23t9
        // i23t10
        Assert.assertEquals("I23T1 == I23T1", 0, c.compare(i23t1, i23t1));
        Assert.assertEquals("I23T2 == I23T2", 0, c.compare(i23t2, i23t2));
        Assert.assertEquals("I23T3 == I23T3", 0, c.compare(i23t3, i23t3));
        Assert.assertEquals("I23T4 == I23T4", 0, c.compare(i23t4, i23t4));
        Assert.assertEquals("I23T5 == I23T5", 0, c.compare(i23t5, i23t5));
        Assert.assertEquals("I23T6 == I23T6", 0, c.compare(i23t6, i23t6));
        Assert.assertEquals("I23T7 == I23T7", 0, c.compare(i23t7, i23t7));
        Assert.assertEquals("I23T8 == I23T8", 0, c.compare(i23t8, i23t8));
        Assert.assertEquals("I23T9 == I23T9", 0, c.compare(i23t9, i23t9));
        Assert.assertEquals("I23T10 == I23T10", 0, c.compare(i23t10, i23t10));

        Assert.assertEquals("I23T1 < I23T2", -1, c.compare(i23t1, i23t2));
        Assert.assertEquals("I23T2 > I23T1", +1, c.compare(i23t2, i23t1));

        Assert.assertEquals("I23T10 > I23T2", +1, c.compare(i23t10, i23t2));
        Assert.assertEquals("I23T2 < I23T10", -1, c.compare(i23t2, i23t10));

        Assert.assertEquals("I23T4 < I23T3", -1, c.compare(i23t4, i23t3));
        Assert.assertEquals("I23T3 > I23T4", +1, c.compare(i23t3, i23t4));

        Assert.assertEquals("I23T5 < I23T1", -1, c.compare(i23t5, i23t1));
        Assert.assertEquals("I23T1 > I23T5", +1, c.compare(i23t1, i23t5));

        TreeSet<Turnout> set = new TreeSet<>(c);
        set.addAll(InstanceManager.getDefault(TurnoutManager.class).getNamedBeanSet());
        Assert.assertArrayEquals(
                new Turnout[]{i23t5, i23t4, i23t3, i23t1, i23t2, i23t6, i23t7, i23t8, i23t9, i23t10},
                set.toArray(new Turnout[set.size()]));
    }

    boolean hit = false;

    @Test
    public void testSystemSpecificCase() {
        NamedBeanUserNameComparator<Turnout> t = new NamedBeanUserNameComparator<>();

        // this just checks that the local sort is called
        Turnout it1 = InstanceManager.getDefault(TurnoutManager.class).provideTurnout("IT1");
        Turnout it2 = new jmri.implementation.AbstractTurnout("IT2") {

            @Override
            protected void forwardCommandChangeToLayout(int s) {
            }

            @Override
            protected void turnoutPushbuttonLockout(boolean b) {
            }

            @Override
            public int compareSystemNameSuffix(String suffix1, String suffix2, jmri.NamedBean n) {
                hit = true;
                return super.compareSystemNameSuffix(suffix1, suffix2, n);
            }
        };

        hit = false;
        Assert.assertEquals("IT1 < IT2", -1, t.compare(it1, it2));
        Assert.assertFalse(hit);

        hit = false;
        Assert.assertEquals("IT2 < IT1", +1, t.compare(it2, it1));
        Assert.assertTrue(hit);
    }

    @BeforeEach
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.initInternalTurnoutManager();
    }

    @AfterEach
    public void tearDown() {
        JUnitUtil.tearDown();
    }

    // private final static Logger log = LoggerFactory.getLogger(NamedBeanUserNameComparatorTest.class);
}
