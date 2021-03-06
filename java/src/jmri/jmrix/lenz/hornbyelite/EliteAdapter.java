package jmri.jmrix.lenz.hornbyelite;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import jmri.jmrix.lenz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import purejavacomm.CommPortIdentifier;
import purejavacomm.NoSuchPortException;
import purejavacomm.PortInUseException;
import purejavacomm.SerialPort;
import purejavacomm.UnsupportedCommOperationException;

/**
 * Provide access to XpressNet via the Hornby Elite's built in USB port.
 * Normally controlled by the lenz.hornbyelite.EliteFrame class.
 *
 * @author Bob Jacobsen Copyright (C) 2002
 * @author Paul Bender, Copyright (C) 2003,2008-2010
 */
public class EliteAdapter extends XNetSerialPortController {

    public EliteAdapter() {
        super(new EliteXNetSystemConnectionMemo());
        option1Name = "FlowControl"; // NOI18N
        options.put(option1Name, new Option(Bundle.getMessage("HornbyEliteConnectionLabel"), validOption1));
        this.manufacturerName = EliteConnectionTypeList.HORNBY;
    }

    @Override
    public String openPort(String portName, String appName) {
        // open the port in XpressNet mode, check ability to set moderators
        try {
            // get and open the primary port
            CommPortIdentifier portID = CommPortIdentifier.getPortIdentifier(portName);
            try {
                activeSerialPort = (SerialPort) portID.open(appName, 2000);  // name of program, msec to wait
            } catch (PortInUseException p) {
                return handlePortBusy(p, portName, log);
            }
            // try to set it for XNet
            try {
                setSerialPort();
            } catch (UnsupportedCommOperationException e) {
                log.error("Cannot set serial parameters on port {}: {}",portName,e.getMessage());
                return "Cannot set serial parameters on port " + portName + ": " + e.getMessage(); // NOI18N
            }

            // set timeout
            try {
                activeSerialPort.enableReceiveTimeout(10);
                log.debug("Serial timeout was observed as: {} {}", activeSerialPort.getReceiveTimeout(),
                        activeSerialPort.isReceiveTimeoutEnabled());
            } catch (Exception et) {
                log.info("failed to set serial timeout",et);
            }

            // get and save stream
            serialStream = activeSerialPort.getInputStream();

            // purge contents, if any
            purgeStream(serialStream);

            // report status?
            if (log.isInfoEnabled()) {
                // report now
                log.info("{} port opened at {} baud with DTR: {} RTS: {} DSR: {} CTS: {}  CD: {}", portName, activeSerialPort.getBaudRate(), activeSerialPort.isDTR(), activeSerialPort.isRTS(), activeSerialPort.isDSR(), activeSerialPort.isCTS(), activeSerialPort.isCD());
            }
            if (log.isDebugEnabled()) {
                // report additional status
                log.debug(" port flow control shows {}", activeSerialPort.getFlowControlMode() == SerialPort.FLOWCONTROL_RTSCTS_OUT ? "hardware flow control" : "no flow control"); // NOI18N

                // log events
                setPortEventLogging(activeSerialPort);
            }

            opened = true;

        } catch (NoSuchPortException p) {
            return handlePortNotFound(p, portName, log);
        } catch (IOException ex) {
            log.error("Unexpected exception while opening port {}", portName, ex);
            return "Unexpected error while opening port " + portName + ": " + ex;
        }

        return null; // normal operation
    }

    /**
     * Set up all of the other objects to operate with the Hornby Elite
     * connected to this port.
     */
    @Override
    public void configure() {
        // connect to a packetizing traffic controller
        XNetTrafficController packets = new XNetPacketizer(new HornbyEliteCommandStation());
        packets.connectPort(this);

        // start operation
        this.getSystemConnectionMemo().setXNetTrafficController(packets);
        new XNetInitializationManager()
                .memo(this.getSystemConnectionMemo())
                .powerManager(XNetPowerManager.class)
                .throttleManager(EliteXNetThrottleManager.class)
                .programmer(EliteXNetProgrammer.class)
                .programmerManager(XNetProgrammerManager.class)
                .turnoutManager(EliteXNetTurnoutManager.class)
                .lightManager(XNetLightManager.class)
                .init();
    }

    // base class methods for the XNetSerialPortController interface

    @Override
    public DataInputStream getInputStream() {
        if (!opened) {
            log.error("getInputStream called before load(), stream not available");
            return null;
        }
        return new DataInputStream(serialStream);
    }

    @Override
    public DataOutputStream getOutputStream() {
        if (!opened) {
            log.error("getOutputStream called before load(), stream not available");
        }
        try {
            return new DataOutputStream(activeSerialPort.getOutputStream());
        } catch (IOException e) {
            log.error("getOutputStream exception: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public boolean status() {
        return opened;
    }

    /**
     * Local method to do specific configuration.
     * @throws UnsupportedCommOperationException if port can't do as asked
     */
    protected void setSerialPort() throws UnsupportedCommOperationException {
        // find the baud rate value, configure comm options
        int baud = currentBaudNumber(mBaudRate);
        activeSerialPort.setSerialPortParams(baud,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

        // find and configure flow control
        int flow = 0;  // no flow control is first in the elite setup,
        // since it doesn't seem to work with flow
        // control enabled.
        if (!getOptionState(option1Name).equals(validOption1[0])) {
            flow = SerialPort.FLOWCONTROL_RTSCTS_OUT;
        }
        configureLeadsAndFlowControl(activeSerialPort, flow);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] validBaudRates() {
        return Arrays.copyOf(validSpeeds, validSpeeds.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] validBaudNumbers() {
        return Arrays.copyOf(validSpeedValues, validSpeedValues.length);
    }

    /**
     * validOption1 controls flow control option.
     */
    protected final String[] validSpeeds = new String[]{Bundle.getMessage("Baud9600"),
            Bundle.getMessage("Baud19200"), Bundle.getMessage("Baud38400"),
            Bundle.getMessage("Baud57600"), Bundle.getMessage("Baud115200")};
    protected final int[] validSpeedValues = new int[]{9600, 19200, 38400, 57600, 115200};

    @Override
    public int defaultBaudIndex() {
        return 0;
    }

    // meanings are assigned to these above, so make sure the order is consistent
    protected final String[] validOption1 = new String[]{Bundle.getMessage("FlowOptionNo"), Bundle.getMessage("FlowOptionHw")};

    InputStream serialStream = null;

    private static final Logger log = LoggerFactory.getLogger(EliteAdapter.class);

}
