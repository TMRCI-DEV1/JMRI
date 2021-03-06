package jmri.jmrix.can.adapters.gridconnect.canrs;

import jmri.jmrix.AbstractMRMessage;
import jmri.jmrix.AbstractMRReply;
import jmri.jmrix.can.CanMessage;
import jmri.jmrix.can.CanReply;
import jmri.jmrix.can.adapters.gridconnect.GcTrafficController;
import jmri.jmrix.can.cbus.CbusConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Traffic controller for the MERG variant of the GridConnect protocol.
 * <p>
 * MERG CAN-RS/CAN-USB uses messages transmitted as an ASCII string of up to 24
 * characters of the form: :ShhhhNd0d1d2d3d4d5d6d7; The S indicates a standard
 * CAN frame hhhh is the two byte header (11 useful bits), left justified on
 * send to adapter N or R indicates a normal or remote frame d0 - d7 are the (up
 * to) 8 data bytes
 *
 * @author Andrew Crosland Copyright (C) 2008
 */
public class MergTrafficController extends GcTrafficController {

    public MergTrafficController() {
        super();
        super.setCanId(CbusConstants.DEFAULT_STANDARD_ID);
    }

    // New message for hardware protocol
    @Override
    protected AbstractMRMessage newMessage() {
        log.debug("New MergMessage created");
        return new MergMessage();
    }

    /**
     * Make a CanReply from a MergReply reply.
     * {@inheritDoc}
     */
    @Override
    public CanReply decodeFromHardware(AbstractMRReply m) {
        log.debug("Decoding from hardware");
        MergReply gc = new MergReply();
        try {
            gc = (MergReply) m;
        } catch(java.lang.ClassCastException cce){
            log.error("{} is not a MergReply",m);
        }
        CanReply ret = gc.createReply();
        log.debug("CanReply created {} from MergReply {}", ret, gc);
        return ret;
    }

    /**
     * Encode a CanMessage for the hardware.
     * {@inheritDoc}
     */
    @Override
    public AbstractMRMessage encodeForHardware(CanMessage m) {
        //log.debug("Encoding for hardware");
        return new MergMessage(m);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractMRReply newReply() {
        log.debug("New MergReply created");
        return new MergReply();
    }

    private final static Logger log = LoggerFactory.getLogger(MergTrafficController.class);

}
