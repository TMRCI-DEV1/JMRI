package jmri.jmrix.ipocs;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.*;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

import jmri.SensorManager;
import jmri.jmrix.SystemConnectionMemoManager;
import jmri.jmrix.SystemConnectionMemoTestBase;

import jmri.util.JUnitUtil;

public class IpocsSystemConnectionMemoTest extends SystemConnectionMemoTestBase<IpocsSystemConnectionMemo> {

  @Test
  public void constructorTest() {
    try (MockedStatic<jmri.InstanceManager> imMock = Mockito.mockStatic(jmri.InstanceManager.class)) {
      SystemConnectionMemoManager scmm = mock(SystemConnectionMemoManager.class);
      when(scmm.isSystemPrefixAvailable("P")).thenReturn(true);
      when(scmm.isUserNameAvailable("IPOCS")).thenReturn(true);
      imMock.when(() -> jmri.InstanceManager.getDefault(SystemConnectionMemoManager.class)).thenReturn(scmm);

      IpocsSystemConnectionMemo memo = new IpocsSystemConnectionMemo();
      assertNotNull(memo);

      memo.dispose();
    }
  }

  @Test
  public void configureManagersEnabledTest() {
    try (MockedStatic<jmri.InstanceManager> imMock = Mockito.mockStatic(jmri.InstanceManager.class)) {
      SystemConnectionMemoManager scmm = mock(SystemConnectionMemoManager.class);
      SensorManager sm = mock(SensorManager.class);
      when(scmm.isSystemPrefixAvailable("P")).thenReturn(true);
      when(scmm.isUserNameAvailable("IPOCS")).thenReturn(true);
      imMock.when(() -> jmri.InstanceManager.getDefault(SystemConnectionMemoManager.class)).thenReturn(scmm);
      imMock.when(() -> jmri.InstanceManager.sensorManagerInstance()).thenReturn(sm);
      imMock.when(() -> jmri.InstanceManager.getDefault(SensorManager.class)).thenReturn(sm);

      IpocsSystemConnectionMemo memo = new IpocsSystemConnectionMemo();
      assertNotNull(memo);
      memo.configureManagers();

      memo.dispose();
    }
  }

  @Test
  public void configureManagersDisabledTest() {
    try (MockedStatic<jmri.InstanceManager> imMock = Mockito.mockStatic(jmri.InstanceManager.class)) {
      SystemConnectionMemoManager scmm = mock(SystemConnectionMemoManager.class);
      SensorManager sm = mock(SensorManager.class);
      when(scmm.isSystemPrefixAvailable("P")).thenReturn(true);
      when(scmm.isUserNameAvailable("IPOCS")).thenReturn(true);
      imMock.when(() -> jmri.InstanceManager.getDefault(SystemConnectionMemoManager.class)).thenReturn(scmm);
      imMock.when(() -> jmri.InstanceManager.sensorManagerInstance()).thenReturn(sm);

      IpocsSystemConnectionMemo memo = new IpocsSystemConnectionMemo();
      memo.setDisabled(true);
      assertNotNull(memo);
      memo.configureManagers();

      memo.dispose();
    }
  }

  @Test
  public void getActionModelResourceBundleTest() {
    try (MockedStatic<jmri.InstanceManager> imMock = Mockito.mockStatic(jmri.InstanceManager.class)) {
      SystemConnectionMemoManager scmm = mock(SystemConnectionMemoManager.class);
      when(scmm.isSystemPrefixAvailable("P")).thenReturn(true);
      when(scmm.isUserNameAvailable("IPOCS")).thenReturn(true);
      imMock.when(() -> jmri.InstanceManager.getDefault(SystemConnectionMemoManager.class)).thenReturn(scmm);

      IpocsSystemConnectionMemo memo = new IpocsSystemConnectionMemo();
      assertNotNull(memo);
      assertNull(memo.getActionModelResourceBundle());

      memo.dispose();
    }
  }

  @Test
  public void portControllerTest() {
    try (MockedStatic<jmri.InstanceManager> imMock = Mockito.mockStatic(jmri.InstanceManager.class)) {
      SystemConnectionMemoManager scmm = mock(SystemConnectionMemoManager.class);
      when(scmm.isSystemPrefixAvailable("P")).thenReturn(true);
      when(scmm.isUserNameAvailable("IPOCS")).thenReturn(true);
      imMock.when(() -> jmri.InstanceManager.getDefault(SystemConnectionMemoManager.class)).thenReturn(scmm);

      IpocsSystemConnectionMemo memo = new IpocsSystemConnectionMemo();
      assertNotNull(memo);
      assertNull(memo.getPortController());
      memo.setPortController(mock(IpocsPortController.class));
      assertNotNull(memo.getPortController());

      memo.dispose();
    }
  }

    @BeforeEach
    @Override
    public void setUp() {
        JUnitUtil.setUp();
        scm = new IpocsSystemConnectionMemo();
    }

    @AfterEach
    @Override
    public void tearDown() {
        scm.dispose();
        scm = null;
        JUnitUtil.tearDown();
    }

}
