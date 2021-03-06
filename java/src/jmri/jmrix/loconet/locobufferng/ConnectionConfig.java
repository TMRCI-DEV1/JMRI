package jmri.jmrix.loconet.locobufferng;

import jmri.util.SystemType;

/**
 * Definition of objects to handle configuring a LocoBuffer-NG layout
 * connection via a LocoBufferIIAdapter object.
 *
 * @author Bob Jacobsen Copyright (C) 2001, 2003
 */
public class ConnectionConfig extends jmri.jmrix.AbstractSerialConnectionConfig {

    /**
     * Ctor for an object being created during load process; Swing init is
     * deferred.
     * @param p the SerialPortAdapter to associate with this connection
     */
    public ConnectionConfig(jmri.jmrix.SerialPortAdapter p) {
        super(p);
    }

    /**
     * Ctor for a connection configuration with no preexisting adapter.
     * {@link #setInstance()} will fill the adapter member.
     */
    public ConnectionConfig() {
        super();
    }

    @Override
    public String name() {
        return "LocoNet LocoBuffer-NG"; // NOI18N
    }

    public boolean isOptList2Advanced() {
        return false;
    }

    @Override
    protected void setInstance() {
        if (adapter == null) {
            adapter = new LocoBufferNGAdapter();
        }
    }

    @Override
    protected String[] getPortFriendlyNames() {
        if (SystemType.isWindows()) {
            return new String[]{"LocoBuffer-NG", "LocoBuffer"}; // NOI18N
        }
        return new String[]{};
    }

}
