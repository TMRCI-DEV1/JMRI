package jmri.util;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple TransferHandler that imports and exports Strings into JTable cells.
 *
 * @author Pete Cressman Copyright 2016
 */
public class DnDTableImportExportHandler extends DnDTableExportHandler {
    
    int[] _skipColumns = new int[]{};

    public DnDTableImportExportHandler() {
        super();
    }
    /**
     * Constructor for import/export of JTable cells
     * @param skipCols array of column indices to refuse importing
     */
    public DnDTableImportExportHandler(int[] skipCols) {
        super();
        if (skipCols!=null) {
            _skipColumns = skipCols;
        }
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        DataFlavor[] flavors =  support.getDataFlavors();
        for (int k = 0; k < flavors.length; k++) {
            if (flavors[k].equals(DataFlavor.stringFlavor)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }
        TransferHandler.DropLocation loc = support.getDropLocation();
        Component comp = support.getComponent();
        if (!support.isDrop() || (!(loc instanceof JTable.DropLocation)) || (!(comp instanceof JTable))   ) {
            return false;            
        }
        JTable.DropLocation location = (JTable.DropLocation)loc;
        log.debug("importData: location= {} row= {} col= {}", loc.getDropPoint(), location.getRow(),location.getColumn());
        JTable table = (JTable)comp;
        int row = table.convertRowIndexToModel(location.getRow());
        int col = table.convertColumnIndexToModel(location.getColumn());
        for (int i = 0; i < _skipColumns.length; i++) {
            if (_skipColumns[i] == col) {
                return false;
            }
        }
        try {
            Transferable trans = support.getTransferable();
            Object obj = trans.getTransferData(DataFlavor.stringFlavor);
            table.getModel().setValueAt(obj, row, col);
            return true;
        } catch (UnsupportedFlavorException | IOException ufe) {
            log.warn("DnDStringImportHandler.importData: {}", ufe.getMessage());
        }
        return false;
    }
    private final static Logger log = LoggerFactory.getLogger(DnDTableImportExportHandler.class);
}
