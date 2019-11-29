package jmri.jmrit.logixng.digital.actions;

import java.util.Locale;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleDigitalExpressionSocket;
import jmri.jmrit.logixng.DigitalActionWithEnableExecution;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.FemaleDigitalBooleanActionSocket;
import jmri.jmrit.logixng.DigitalBooleanActionManager;

/**
 * Emulates Logix.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class Logix extends AbstractDigitalAction
        implements FemaleSocketListener, DigitalActionWithEnableExecution {

    private boolean _enableExecution;
    private boolean _lastExpressionResult = false;
    private String _expressionSocketSystemName;
    private String _actionSocketSystemName;
    private final FemaleDigitalExpressionSocket _expressionSocket;
    private final FemaleDigitalBooleanActionSocket _actionSocket;
    
    public Logix(String sys, String user) {
        super(sys, user);
        _expressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, "E");
        _actionSocket = InstanceManager.getDefault(DigitalBooleanActionManager.class)
                .createFemaleSocket(this, this, "A");
    }
    
    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.OTHER;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsEnableExecution() {
        return true;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setEnableExecution(boolean b) {
        _enableExecution = b;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isExecutionEnabled() {
        return _enableExecution;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    @Override
    public void evaluateOnly() {
        _lastExpressionResult = _expressionSocket.evaluate();
    }
    
    /** {@inheritDoc} */
    @Override
    public void execute() {
        _lastExpressionResult = _expressionSocket.evaluate();
        _actionSocket.execute(_lastExpressionResult);
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        switch (index) {
            case 0:
                return _expressionSocket;
                
            case 1:
                return _actionSocket;
                
            default:
                throw new IllegalArgumentException(
                        String.format("index has invalid value: %d", index));
        }
    }

    @Override
    public int getChildCount() {
        return 2;
    }

    @Override
    public void connected(FemaleSocket socket) {
        if (socket == _expressionSocket) {
            _expressionSocketSystemName = socket.getConnectedSocket().getSystemName();
        } else if (socket == _actionSocket) {
            _actionSocketSystemName = socket.getConnectedSocket().getSystemName();
        } else {
            throw new IllegalArgumentException("unkown socket");
        }
    }

    @Override
    public void disconnected(FemaleSocket socket) {
        if (socket == _expressionSocket) {
            _expressionSocketSystemName = null;
        } else if (socket == _actionSocket) {
            _actionSocketSystemName = null;
        } else {
            throw new IllegalArgumentException("unkown socket");
        }
    }

    @Override
    public String getShortDescription(Locale locale) {
        return Bundle.getMessage(locale, "IfThenElse_Short");
    }

    @Override
    public String getLongDescription(Locale locale) {
        return Bundle.getMessage(locale, "Logix_Long",
                _expressionSocket.getName(),
                _actionSocket.getName());
    }

    public FemaleDigitalExpressionSocket getIfExpressionSocket() {
        return _expressionSocket;
    }

    public String getExpressionSocketSystemName() {
        return _expressionSocketSystemName;
    }

    public void setExpressionSocketSystemName(String systemName) {
        _expressionSocketSystemName = systemName;
    }

    public FemaleDigitalBooleanActionSocket getActionSocket() {
        return _actionSocket;
    }

    public String getActionSocketSystemName() {
        return _actionSocketSystemName;
    }

    public void setActionSocketSystemName(String systemName) {
        _actionSocketSystemName = systemName;
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        try {
            if ( !_expressionSocket.isConnected()
                    || !_expressionSocket.getConnectedSocket().getSystemName()
                            .equals(_expressionSocketSystemName)) {
                
                String socketSystemName = _expressionSocketSystemName;
                _expressionSocket.disconnect();
                if (socketSystemName != null) {
                    MaleSocket maleSocket =
                            InstanceManager.getDefault(DigitalExpressionManager.class)
                                    .getBeanBySystemName(socketSystemName);
                    if (maleSocket != null) {
                        _expressionSocket.connect(maleSocket);
                        maleSocket.setup();
                    } else {
                        log.error("cannot load digital expression " + socketSystemName);
                    }
                }
            } else {
                _expressionSocket.getConnectedSocket().setup();
            }
            
            if ( !_actionSocket.isConnected()
                    || !_actionSocket.getConnectedSocket().getSystemName()
                            .equals(_actionSocketSystemName)) {
                
                String socketSystemName = _actionSocketSystemName;
                _actionSocket.disconnect();
                if (socketSystemName != null) {
                    MaleSocket maleSocket =
                            InstanceManager.getDefault(DigitalBooleanActionManager.class)
                                    .getBeanBySystemName(socketSystemName);
                    _actionSocket.disconnect();
                    if (maleSocket != null) {
                        _actionSocket.connect(maleSocket);
                        maleSocket.setup();
                    } else {
                        log.error("cannot load digital action " + socketSystemName);
                    }
                }
            } else {
                _actionSocket.getConnectedSocket().setup();
            }
        } catch (SocketAlreadyConnectedException ex) {
            // This shouldn't happen and is a runtime error if it does.
            throw new RuntimeException("socket is already connected");
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void registerListenersForThisClass() {
    }
    
    /** {@inheritDoc} */
    @Override
    public void unregisterListenersForThisClass() {
    }
    
    /** {@inheritDoc} */
    @Override
    public void disposeMe() {
    }

    private final static Logger log = LoggerFactory.getLogger(Logix.class);

}
