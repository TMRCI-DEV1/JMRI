/**
 * 
 */
package jmri.jmrit.turnoutoperations;

import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import jmri.NoFeedbackTurnoutOperation;
import jmri.TurnoutOperation;
import jmri.jmrit.turnoutoperations.TurnoutOperationConfig;

/**
 * Configuration for NoFeedbackTurnoutOperation class
 * All the work is done by the Common... class
 * @author John Harper	Copyright 2005
 *
 */
public class NoFeedbackTurnoutOperationConfig extends CommonTurnoutOperationConfig {

	/**
	 * Create the config JPanel, if there is one, to configure this operation type
	 */
	public NoFeedbackTurnoutOperationConfig(TurnoutOperation op) {
		super(op);
	}
	
	static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(NoFeedbackTurnoutOperationConfig.class.getName());
}
