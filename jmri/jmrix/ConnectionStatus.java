// ConnectionStatus.java

package jmri.jmrix;

import java.util.ArrayList;

/**
 * Interface for classes that wish to get notification when the
 * connection to the layout changes.
 *
 * @author     Daniel Boudreau   Copyright (C) 2007
 * @version    $Revision: 1.2 $
 */
public class ConnectionStatus {

	public static final String CONNECTION_UNKNOWN = "Unknown";
	public static final String CONNECTION_UP = "Connected";
	public static final String CONNECTION_DOWN = "Not Connected";

	// simple database of port names and their status
	static ArrayList portNames= new ArrayList(); 
	static ArrayList portStatus = new ArrayList(); 

	/** record the single instance **/
	private static ConnectionStatus _instance = null;

	public static synchronized ConnectionStatus instance() {
		if (_instance == null) {
			if (log.isDebugEnabled()) log.debug("ConnectionStatus creating instance");
			// create and load
			_instance = new ConnectionStatus();
		}
		if (log.isDebugEnabled()) log.debug("ConnectionStatus returns instance "+_instance);
		return _instance;
	}

	public synchronized void addConnection (String systemName, String portName){
		log.debug ("add connection to monitor " + systemName + " " + portName);
		if (portNames.contains(portName))
			return;
		portNames.add(portName);
		portStatus.add(CONNECTION_UNKNOWN);
		firePropertyChange("add", null, portName);
	}
	/**
	 * sets the connection state of a communication port
	 * @param portName = communication port name
	 * @param state  
	 */
	public synchronized void setConnectionState(String portName, String state){
		log.debug ("set " + portName + " connection status: " + state);
		if (!portNames.contains(portName)){
			portNames.add(portName);
			portStatus.add(state);
			firePropertyChange("add", null, portName);
		} else {
			for (int i=0; i<portNames.size(); i++){
				if (portName.equals(portNames.get(i))){
					if (!state.equals(portStatus.get(i))){ 
					portStatus.set(i, state);
					firePropertyChange("change", null, portName);
					break;
					}
				}
			}
		}
	}
	
	/**
	 * get the status of a communication port
	 * @param portName
	 * @return status string
	 */
	public synchronized String getConnectionState(String portName){
		String stateText = CONNECTION_UNKNOWN;
		if (portNames.contains(portName)){
			for (int i=0; i<portNames.size(); i++){
				if (portName.equals(portNames.get(i))){
					stateText = (String) portStatus.get(i);
					break;
				}
			}
		}
		log.debug ("get connection status: " + portName + " " + stateText);
		return stateText;
	}

	/**
	 * Returns status of a communication port
	 * @param portName
	 * @return true if port connection is operatonal or unknown, false if not
	 */
	public synchronized boolean isConnectionOk(String portName){
		String stateText = getConnectionState(portName);
		if (stateText.equals(CONNECTION_DOWN))
			return false;
		else
			return true;
	}
	
    java.beans.PropertyChangeSupport pcs = new java.beans.PropertyChangeSupport(this);

    public synchronized void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    protected void firePropertyChange(String p, Object old, Object n) {
        log.debug ("firePropertyChange " + p + " old: " +old + " new: "+ n);
    	pcs.firePropertyChange(p,old,n);
    }

    public synchronized void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

	static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(ConnectionStatus.class.getName());
}