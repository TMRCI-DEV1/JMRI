package jmri.jmrix.can.cbus;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import jmri.beans.PreferencesBean;
import jmri.jmrix.can.cbus.node.CbusNode;
import jmri.jmrix.can.cbus.swing.modeswitcher.SprogCbusSprog3PlusModeSwitcherFrame;
import jmri.profile.ProfileManager;
import jmri.profile.ProfileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Preferences for the MERG CBUS connections.
 *
 * @author Steve Young (c) 2019
 */
public class CbusPreferences extends PreferencesBean {

    // defaults
    private boolean _addCommandStations = false;
    private boolean _addNodes = false;
    private boolean _allocateNnListener = true;
    private long _nodeBackgroundFetchDelay = 100L; // needs to match an option in NodeConfigToolPane
    private boolean _startupSearchForCs = false;
    private boolean _startupSearchForNodes = false;
    private boolean searchForNodesBackupXmlOnStartup = false;
    private boolean _saveRestoreEventTable = true;
    private int minimumNumBackupsToKeep = 10;
    private int bootWriteDelay = CbusNode.BOOT_PROG_TIMEOUT_FAST;
    // Default to no programmers available. The p[rogrammer manager will validate
    // the preferences for the hardware connection in use.
    private boolean _isGlobalProgrammerAvailable = true;
    private boolean _isAddressedModePossible = true;
    private int _progTrackMode = SprogCbusSprog3PlusModeSwitcherFrame.PROG_OFF_MODE;
    private int _nodeTableSplit = 100;
    
    public CbusPreferences() {
        super(ProfileManager.getDefault().getActiveProfile());
        Preferences sharedPreferences = ProfileUtils.getPreferences(super.getProfile(), this.getClass(), true);
        this.readPreferences(sharedPreferences);
    }

    private void readPreferences(Preferences sharedPreferences) {

        this._addCommandStations = sharedPreferences.getBoolean("_addCommandStations",this.getAddCommandStations());
        this._addNodes = sharedPreferences.getBoolean("_addNodes",this.getAddNodes());
        this._allocateNnListener = sharedPreferences.getBoolean("_allocateNnListener",this.getAllocateNNListener());
        this._nodeBackgroundFetchDelay = sharedPreferences.getLong("_nodeBgFetchDelay",this.getNodeBackgroundFetchDelay());
        
        this._startupSearchForCs = sharedPreferences.getBoolean("_startupSearchForCs",this.getStartupSearchForCs());
        this._startupSearchForNodes = sharedPreferences.getBoolean("_startupSearchForNodes",this.getStartupSearchForNodes());
        
        this.searchForNodesBackupXmlOnStartup = sharedPreferences.getBoolean(
            "searchForNodesBackupXmlOnStartup",this.getSearchForNodesBackupXmlOnStartup() );
        this.minimumNumBackupsToKeep = sharedPreferences.getInt(
            "minimumNumBackupsToKeep",this.getMinimumNumBackupsToKeep() );
        this.bootWriteDelay = sharedPreferences.getInt(
            "bootWriteDelay",this.getBootWriteDelay() );
        this._saveRestoreEventTable = sharedPreferences.getBoolean(
            "saveRestoreEventTable",this.getSaveRestoreEventTable() );

        this._isGlobalProgrammerAvailable = sharedPreferences.getBoolean(
            "globalprogrammer", this.isGlobalProgrammerAvailable() );
        this._isAddressedModePossible = sharedPreferences.getBoolean(
            "addressedprogrammer", this.isAddressedModePossible() );

        this._progTrackMode = sharedPreferences.getInt("progtrackmode", this.getProgTrackMode() );
        
        this._nodeTableSplit = sharedPreferences.getInt("nodetablesplit", this.getNodeTableSplit() );
    }

    public void savePreferences() {

        Preferences sharedPreferences = ProfileUtils.getPreferences(this.getProfile(), this.getClass(), true);
        
        sharedPreferences.putBoolean("_addCommandStations", this.getAddCommandStations() );
        sharedPreferences.putBoolean("_addNodes", this.getAddNodes() );
        sharedPreferences.putBoolean("_allocateNnListener", this.getAllocateNNListener() );
        sharedPreferences.putLong("_nodeBgFetchDelay", this.getNodeBackgroundFetchDelay() );
        
        sharedPreferences.putBoolean("_startupSearchForCs", this.getStartupSearchForCs() );
        sharedPreferences.putBoolean("_startupSearchForNodes", this.getStartupSearchForNodes() );
        
        sharedPreferences.putBoolean("searchForNodesBackupXmlOnStartup", this.getSearchForNodesBackupXmlOnStartup() );
        sharedPreferences.putInt("minimumNumBackupsToKeep", this.getMinimumNumBackupsToKeep() );
        sharedPreferences.putInt("bootWriteDelay", this.getBootWriteDelay() );
        sharedPreferences.putBoolean("saveRestoreEventTable", this.getSaveRestoreEventTable() );

        sharedPreferences.putBoolean("globalprogrammer", this.isGlobalProgrammerAvailable() );
        sharedPreferences.putBoolean("addressedprogrammer", this.isAddressedModePossible() );
        
        sharedPreferences.putInt("progtrackmode", this.getProgTrackMode() );
        
        sharedPreferences.putInt("nodetablesplit", this.getNodeTableSplit() );
        
        try {
            sharedPreferences.sync();
            log.debug("Updated Cbus Preferences");
          //  setIsDirty(false);  //  Resets only when stored
        } catch (BackingStoreException ex) {
            log.error("Exception while saving preferences", ex);
        }
    }

    boolean isPreferencesValid() {
        return true;
    }
    
    /**
     * Get if should add new command stations heard on network to CBUS Node Manager table
     * @return true if adding command stations, else false
     */
    public boolean getAddCommandStations() {
        return _addCommandStations;
    }

    /**
     * Set if should add new command stations heard on network to CBUS Node Manager table
     * @param newVal true if adding command stations, else false
     */
    public void setAddCommandStations( boolean newVal ) {
        _addCommandStations = newVal;
        savePreferences();
    }
    
    /**
     * Get if should add new nodes heard on network to CBUS Node Manager table
     * @return true if adding nodes, else false
     */
    public boolean getAddNodes() {
        return _addNodes;
    }
    
    /**
     * Set if should add new nodes heard on network to CBUS Node Manager table
     * @param newVal true if adding nodes, else false
     */
    public void setAddNodes( boolean newVal ) {
        _addNodes = newVal;
        savePreferences();
    }
    
    /**
     * Get if should listen on network for new node number requests
     * @return true if should listen, else false
     */
    public boolean getAllocateNNListener(){
        return _allocateNnListener;
    }
    
    /**
     * Set if should listen on network for new node number requests
     * @param newVal true if should listen, else false
     */
    public void setAllocateNNListener( boolean newVal ){
        _allocateNnListener = newVal;
        savePreferences();
    }
    
    /**
     * Get Background delay between CBUS Node Manager data fetch from nodes
     * @return the delay
     */
    public long getNodeBackgroundFetchDelay() {
        return _nodeBackgroundFetchDelay;
    }
    
    /**
     * Set Background delay between CBUS Node Manager data fetch from nodes
     * @param newVal in ms can be 0 but defaults to 100ms
     */
    public void setNodeBackgroundFetchDelay( long newVal ) {
        _nodeBackgroundFetchDelay = newVal;
        savePreferences();
    }
    
    /**
     * Get Search for Command stations on CBUS Node Table Startup
     * @return true to send CBUS search for CS, else false
     */
    public boolean getStartupSearchForCs(){
        return _startupSearchForCs;
    }
    
    /**
     * Set Search for Command stations on CBUS Node Table Startup
     * @param newVal true to send CBUS search for CS, else false
     */
    public void setStartupSearchForCs( boolean newVal ){
        _startupSearchForCs = newVal;
        savePreferences();
    }    
    
    /**
     * Get Search for Nodes on CBUS Node Table Startup
     * @return true to send CBUS search for nodes, else false
     */
    public boolean getStartupSearchForNodes(){
        return _startupSearchForNodes;
    }
    
    /**
     * Set Search for Nodes on CBUS Node Table Startup
     * @param newVal true to send CBUS search for nodes, else false
     */
    public void setStartupSearchForNodes( boolean newVal ){
        _startupSearchForNodes = newVal;
        savePreferences();
    }
    
    /**
     * Get Save Restore CBUS Event Table
     * <p>
     * If enabled loads CBUS event table data from xml on table startup,
     * and saves data to xml on shutdown.
     *
     * @return true to save and restore, else false
     */
    public boolean getSaveRestoreEventTable(){
        return _saveRestoreEventTable;
    }
    
    /**
     * Set Save Restore CBUS Event Table
     * @param newVal true to save and restore, else false
     */
    public void setSaveRestoreEventTable( boolean newVal ){
        _saveRestoreEventTable = newVal;
        savePreferences();
    }

    /**
     * Get search CBUS node backup directory on startup for node xml files
     * @return true to search, else false
     */
    public boolean getSearchForNodesBackupXmlOnStartup(){
        return searchForNodesBackupXmlOnStartup;
    }
    
    /**
     * Set search CBUS node backup directory on startup for node xml files
     * @param newVal true to lookup node xml files, else false
     */
    public void setSearchForNodesBackupXmlOnStartup( boolean newVal ){
        searchForNodesBackupXmlOnStartup = newVal;
        savePreferences();
    }
    
    /**
     * Get minimum number of CbusNode XML backups to retain
     * @return number of backups, defaults to 10
     */
    public int getMinimumNumBackupsToKeep(){
        return minimumNumBackupsToKeep;
    }
    
    /**
     * Set minimum number of CbusNode XML backups to retain
     * @param newVal the new number of backups
     */
    public void setMinimumNumBackupsToKeep( int newVal ){
        minimumNumBackupsToKeep = newVal;
        savePreferences();
    }
    
    /**
     * Get delay between bootloader data writes
     * @return delay, in ms, defaults to CbusNode.BOOT_PROG_TIMEOUT_FAST
     */
    public int getBootWriteDelay(){
        return bootWriteDelay;
    }
    
    /**
     * Set delay between bootloader data writes
     * @param newVal the delay in ms
     */
    public void setBootWriteDelay( int newVal ){
        bootWriteDelay = newVal;
        savePreferences();
    }
    
    /**
     * Get the global programmer state
     * @return global programmer state
     */
    public boolean isGlobalProgrammerAvailable() {
        return _isGlobalProgrammerAvailable;
    }
    
    /**
     * Get the addressed programmer state
     * @return addressed programmer state
     */
    public boolean isAddressedModePossible() {
        return _isAddressedModePossible;
    }
    
    /**
     * Set global (service mode) programmer availability
     * @param state true if available
     */
    public void setGlobalProgrammerAvailable(boolean state) {
        _isGlobalProgrammerAvailable = state;
        savePreferences();
    }
    
    /**
     * Set global (service mode) programmer availability
     * @param state true if available
     */
    public void setAddressedModePossible(boolean state) {
        _isAddressedModePossible = state;
        savePreferences();
    }
    
    /**
     * Set the programmer type
     * @param global true if global (service mode) programmer is available
     * @param addressed thru if addressed (ops mode) programmer is available
     */
    public void setProgrammersAvailable(boolean global, boolean addressed) {
        setGlobalProgrammerAvailable(global);
        setAddressedModePossible(addressed);
    }
    
    /**
     * Get the programming track mode
     * @return the mode
     */
    public int getProgTrackMode() {
        return _progTrackMode;
    }
    
    /**
     * Set programming track mode
     * @param mode to be set
     */
    public void setProgTrackMode(int mode) {
        _progTrackMode = mode;
        savePreferences();
    }
    
    /**
     * Get the position of the node table split
     * 
     * @return position in pixels
     */
    public int getNodeTableSplit() {
        return _nodeTableSplit;
    }
    
    /**
     * Set the position of the node table split from the top of thw window
     * 
     * @param pixels new position
     */
    public void setNodeTableSplit(int pixels) {
        _nodeTableSplit = pixels;
        savePreferences();
    }
    
    private final static Logger log = LoggerFactory.getLogger(CbusPreferences.class);

}
