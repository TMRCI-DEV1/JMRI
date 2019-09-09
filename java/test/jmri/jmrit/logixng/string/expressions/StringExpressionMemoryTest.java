package jmri.jmrit.logixng.string.expressions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import jmri.InstanceManager;
import jmri.Memory;
import jmri.MemoryManager;
import jmri.NamedBeanHandle;
import jmri.NamedBeanHandleManager;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.DigitalActionBean;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.LogixNG_Manager;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.StringActionManager;
import jmri.jmrit.logixng.StringExpressionManager;
import jmri.jmrit.logixng.digital.actions.DoStringAction;
import jmri.jmrit.logixng.implementation.DefaultConditionalNG;
import jmri.jmrit.logixng.string.actions.StringActionMemory;
import jmri.util.JUnitAppender;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test StringExpressionMemory
 * 
 * @author Daniel Bergqvist 2018
 */
public class StringExpressionMemoryTest extends AbstractStringExpressionTestBase {

    protected Memory _memory;
    
    @Override
    public ConditionalNG getConditionalNG() {
        return null;
    }
    
    @Override
    public LogixNG getLogixNG() {
        return null;
    }
    
    @Override
    public String getExpectedPrintedTree() {
        return String.format("Get memory IM1%n");
    }
    
    @Override
    public String getExpectedPrintedTreeFromRoot() {
        return String.format("Get memory IM1%n");
    }
    
    @Test
    public void testCtor() {
        Assert.assertTrue("object exists", _base != null);
        
        StringExpressionMemory expression2;
        Assert.assertNotNull("memory is not null", _memory);
        _memory.setValue(10.2);
        
        expression2 = new StringExpressionMemory("IQSE11");
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", null == expression2.getUserName());
        Assert.assertTrue("String matches", "Get memory none".equals(expression2.getLongDescription()));
        
        expression2 = new StringExpressionMemory("IQSE11", "My memory");
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", "My memory".equals(expression2.getUserName()));
        Assert.assertTrue("String matches", "Get memory none".equals(expression2.getLongDescription()));
        
        expression2 = new StringExpressionMemory("IQSE11");
        expression2.setMemory(_memory);
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", null == expression2.getUserName());
        Assert.assertTrue("String matches", "Get memory IM1".equals(expression2.getLongDescription()));
        
        expression2 = new StringExpressionMemory("IQSE11", "My memory");
        expression2.setMemory(_memory);
        Assert.assertNotNull("object exists", expression2);
        Assert.assertTrue("Username matches", "My memory".equals(expression2.getUserName()));
        Assert.assertTrue("String matches", "Get memory IM1".equals(expression2.getLongDescription()));
        
        // Test template
        expression2 = (StringExpressionMemory)_base.getNewObjectBasedOnTemplate("IQSE12");
        Assert.assertNotNull("object exists", expression2);
        Assert.assertNull("Username is null", expression2.getUserName());
//        Assert.assertTrue("Username matches", "My memory".equals(expression2.getUserName()));
        Assert.assertTrue("String matches", "Get memory IM1".equals(expression2.getLongDescription()));
        
        boolean thrown = false;
        try {
            // Illegal system name
            new StringExpressionMemory("IQA55:12:XY11");
        } catch (IllegalArgumentException ex) {
            thrown = true;
        }
        Assert.assertTrue("Expected exception thrown", thrown);
        
        thrown = false;
        try {
            // Illegal system name
            new StringExpressionMemory("IQA55:12:XY11", "A name");
        } catch (IllegalArgumentException ex) {
            thrown = true;
        }
        Assert.assertTrue("Expected exception thrown", thrown);
    }
    
    @Test
    public void testEvaluate() throws SocketAlreadyConnectedException, SocketAlreadyConnectedException {
        StringExpressionMemory expression = (StringExpressionMemory)_base;
        _memory.setValue("");
        Assert.assertEquals("Evaluate matches", "", expression.evaluate());
        _memory.setValue("Other");
        Assert.assertEquals("Evaluate matches", "Other", expression.evaluate());
        expression.setMemory((Memory)null);
        Assert.assertEquals("Evaluate matches", "", expression.evaluate());
        expression.reset();
    }
    
    @Test
    public void testEvaluateAndAction() throws SocketAlreadyConnectedException, SocketAlreadyConnectedException {
        
        StringExpressionMemory expression = (StringExpressionMemory)_base;
        
        LogixNG logixNG = InstanceManager.getDefault(LogixNG_Manager.class).createLogixNG("A logixNG");
        ConditionalNG conditionalNG = new DefaultConditionalNG(logixNG.getSystemName()+":1");
        
        logixNG.addConditionalNG(conditionalNG);
        logixNG.activateLogixNG();
        
        DigitalActionBean actionDoString = new DoStringAction();
        MaleSocket socketDoString = InstanceManager.getDefault(DigitalActionManager.class).registerAction(actionDoString);
        conditionalNG.getChild(0).connect(socketDoString);
        
        MaleSocket socketExpression = InstanceManager.getDefault(StringExpressionManager.class).registerExpression(expression);
        socketDoString.getChild(0).connect(socketExpression);
        
        Memory _memoryOut = InstanceManager.getDefault(MemoryManager.class).provide("IM2");
        _memoryOut.setValue("");
        StringActionMemory actionMemory = new StringActionMemory("IQSA1");
        actionMemory.setMemory(_memoryOut);
        MaleSocket socketAction = InstanceManager.getDefault(StringActionManager.class).registerAction(actionMemory);
        socketDoString.getChild(1).connect(socketAction);
        
        // The action is not yet executed so the double should be 0.0
        Assert.assertEquals("memory is \"\"", "", _memoryOut.getValue());
        // Set the value of the memory. This should not execute the conditional.
        _memory.setValue("Test");
        // The conditionalNG is not yet enabled so it shouldn't be executed.
        // So the memory should be 0.0
        Assert.assertEquals("memory is \"\"", "", _memoryOut.getValue());
        // Set the value of the memory. This should not execute the conditional.
        _memory.setValue("Other test");
        // Enable the conditionalNG and all its children.
        conditionalNG.setEnabled(true);
        // The action is not yet executed so the memory should be 0.0
        Assert.assertEquals("memory is \"\"", "", _memoryOut.getValue());
        // Set the value of the memory. This should execute the conditional.
        _memory.setValue("Something else");
        // The action should now be executed so the memory should be 3.0
        Assert.assertEquals("memory is \"Something else\"", "Something else", _memoryOut.getValue());
        // Disable the conditionalNG and all its children.
        conditionalNG.setEnabled(false);
        // The action is not yet executed so the memory should be 0.0
        Assert.assertEquals("memory is \"something else\"", "Something else", _memoryOut.getValue());
        // Set the value of the memory. This should not execute the conditional.
        _memory.setValue("Something new");
        // The action should not be executed so the memory should still be 3.0
        Assert.assertEquals("memory is \"something else\"", "Something else", _memoryOut.getValue());
        // Unregister listeners. This should do nothing since the listeners are
        // already unregistered.
        expression.unregisterListeners();
        // The action is not yet executed so the memory should be 0.0
        Assert.assertEquals("memory is \"something else\"", "Something else", _memoryOut.getValue());
        // Set the value of the memory. This should not execute the conditional.
        _memory.setValue("Something different");
        // The action should not be executed so the memory should still be 3.0
        Assert.assertEquals("memory is \"something else\"", "Something else", _memoryOut.getValue());
        
        // Test register listeners when there is no memory.
        expression.setMemory((Memory)null);
        expression.registerListeners();
    }
    
    @Test
    public void testMemory() {
        StringExpressionMemory expression = (StringExpressionMemory)_base;
        expression.setMemory((Memory)null);
        Assert.assertNull("Memory is null", expression.getMemory());
        expression.setMemory(_memory);
        Assert.assertTrue("Memory matches", _memory == expression.getMemory().getBean());
        
        expression.setMemory((NamedBeanHandle<Memory>)null);
        Assert.assertNull("Memory is null", expression.getMemory());
        Memory otherMemory = InstanceManager.getDefault(MemoryManager.class).provide("IM99");
        Assert.assertNotNull("memory is not null", otherMemory);
        NamedBeanHandle<Memory> memoryHandle = InstanceManager.getDefault(NamedBeanHandleManager.class)
                .getNamedBeanHandle(otherMemory.getDisplayName(), otherMemory);
        expression.setMemory(memoryHandle);
        Assert.assertTrue("Memory matches", memoryHandle == expression.getMemory());
        Assert.assertTrue("Memory matches", otherMemory == expression.getMemory().getBean());
        
        expression.setMemory((String)null);
        Assert.assertNull("Memory is null", expression.getMemory());
        expression.setMemory(memoryHandle.getName());
        Assert.assertTrue("Memory matches", memoryHandle == expression.getMemory());
        
        // Test setMemory with a memory name that doesn't exists
        expression.setMemory("Non existent memory");
        Assert.assertTrue("Memory matches", memoryHandle == expression.getMemory());
        JUnitAppender.assertWarnMessage("memory 'Non existent memory' does not exists");
        
        // Test setMemory() when listeners are registered
        Assert.assertNotNull("Memory is not null", expression.getMemory());
        expression.registerListeners();
        boolean thrown = false;
        try {
            expression.setMemory((String)null);
        } catch (RuntimeException ex) {
            thrown = true;
        }
        Assert.assertTrue("Expected exception thrown", thrown);
        JUnitAppender.assertErrorMessage("setMemory must not be called when listeners are registered");
        
        thrown = false;
        try {
            expression.setMemory((NamedBeanHandle<Memory>)null);
        } catch (RuntimeException ex) {
            thrown = true;
        }
        Assert.assertTrue("Expected exception thrown", thrown);
        JUnitAppender.assertErrorMessage("setMemory must not be called when listeners are registered");
        
        thrown = false;
        try {
            expression.setMemory((Memory)null);
        } catch (RuntimeException ex) {
            thrown = true;
        }
        Assert.assertTrue("Expected exception thrown", thrown);
        JUnitAppender.assertErrorMessage("setMemory must not be called when listeners are registered");
    }
    
    @Test
    public void testVetoableChange() throws PropertyVetoException {
        // Get some other memory for later use
        Memory otherMemory = InstanceManager.getDefault(MemoryManager.class).provide("IM99");
        Assert.assertNotNull("Memory is not null", otherMemory);
        Assert.assertNotEquals("Memory is not equal", _memory, otherMemory);
        
        // Get the expression and set the memory
        StringExpressionMemory expression = (StringExpressionMemory)_base;
        expression.setMemory(_memory);
        Assert.assertEquals("Memory matches", _memory, expression.getMemory().getBean());
        
        // Test vetoableChange() for some other propery
        expression.vetoableChange(new PropertyChangeEvent(this, "CanSomething", "test", null));
        Assert.assertEquals("Memory matches", _memory, expression.getMemory().getBean());
        
        // Test vetoableChange() for a string
        expression.vetoableChange(new PropertyChangeEvent(this, "CanDelete", "test", null));
        Assert.assertEquals("Memory matches", _memory, expression.getMemory().getBean());
        expression.vetoableChange(new PropertyChangeEvent(this, "DoDelete", "test", null));
        Assert.assertEquals("Memory matches", _memory, expression.getMemory().getBean());
        
        // Test vetoableChange() for another memory
        expression.vetoableChange(new PropertyChangeEvent(this, "CanDelete", otherMemory, null));
        Assert.assertEquals("Memory matches", _memory, expression.getMemory().getBean());
        expression.vetoableChange(new PropertyChangeEvent(this, "DoDelete", otherMemory, null));
        Assert.assertEquals("Memory matches", _memory, expression.getMemory().getBean());
        
        // Test vetoableChange() for its own memory
        boolean thrown = false;
        try {
            expression.vetoableChange(new PropertyChangeEvent(this, "CanDelete", _memory, null));
        } catch (PropertyVetoException ex) {
            thrown = true;
        }
        Assert.assertTrue("Expected exception thrown", thrown);
        
        Assert.assertEquals("Memory matches", _memory, expression.getMemory().getBean());
        expression.vetoableChange(new PropertyChangeEvent(this, "DoDelete", _memory, null));
        Assert.assertNull("Memory is null", expression.getMemory());
    }
    
    @Test
    public void testCategory() {
        Assert.assertTrue("Category matches", Category.ITEM == _base.getCategory());
    }
    
    @Test
    public void testIsExternal() {
        Assert.assertTrue("is external", _base.isExternal());
    }
    
    @Test
    public void testShortDescription() {
        Assert.assertTrue("String matches", "Get memory IM1".equals(_base.getShortDescription()));
    }
    
    @Test
    public void testLongDescription() {
        Assert.assertTrue("String matches", "Get memory IM1".equals(_base.getLongDescription()));
    }
    
    @Test
    public void testChild() {
        Assert.assertTrue("Num children is zero", 0 == _base.getChildCount());
        boolean hasThrown = false;
        try {
            _base.getChild(0);
        } catch (UnsupportedOperationException ex) {
            hasThrown = true;
            Assert.assertTrue("Error message is correct", "Not supported.".equals(ex.getMessage()));
        }
        Assert.assertTrue("Exception is thrown", hasThrown);
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initMemoryManager();
        _memory = InstanceManager.getDefault(MemoryManager.class).provide("IM1");
        Assert.assertNotNull("memory is not null", _memory);
        _memory.setValue(10.2);
        _base = new StringExpressionMemory("IQSE321", "StringIO_Memory");
        ((StringExpressionMemory)_base).setMemory(_memory);
    }

    @After
    public void tearDown() {
        _base.dispose();
        JUnitUtil.tearDown();
    }
    
}
