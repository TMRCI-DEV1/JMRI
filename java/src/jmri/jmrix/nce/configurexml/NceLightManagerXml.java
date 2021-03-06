package jmri.jmrix.nce.configurexml;

import org.jdom2.Element;

/**
 * Provides load and store functionality for configuring NceLightManagers.
 * <p>
 * Uses the store method from the abstract base class, but provides a load
 * method here.
 *
 * @author Dave Duchamp Copyright (c) 2010
 */
public class NceLightManagerXml extends jmri.managers.configurexml.AbstractLightManagerConfigXML {

    public NceLightManagerXml() {
        super();
    }

    @Override
    public void setStoreElementClass(Element lights) {
        lights.setAttribute("class", "jmri.jmrix.nce.configurexml.NceLightManagerXml");
    }

    @Override
    public boolean load(Element shared, Element perNode) {
        // load individual lights
        return loadLights(shared);
    }

//    private final static Logger log = LoggerFactory.getLogger(NceLightManagerXml.class);
}
