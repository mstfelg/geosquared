package org.geogebra.desktop.util;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;

import org.geogebra.common.cas.view.CASView;
import org.geogebra.common.euclidian.EuclidianConstants;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.geos.GeoCasCell;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoText;
import org.geogebra.common.util.debug.Log;
import org.geogebra.desktop.cas.view.CASTableD;
import org.geogebra.desktop.main.AppD;

/**
 * Handles drop events of CAS window
 * 
 * @author Dominik Kreil
 *
 */
public class CASDropTargetListener implements DropTargetListener {

	private CASTableD table;
	private AppD app;
	private Kernel kernel;
	private CASView view;
	private DropTarget dropTarget;

	/**
	 * creates a new drop target listener
	 * 
	 * @param app
	 *            the current application
	 * @param view
	 *            the cas view
	 * @param table
	 *            the cas table
	 */
	public CASDropTargetListener(AppD app, CASView view, CASTableD table) {
		this.app = app;
		this.table = table;
		this.view = view;
		kernel = app.getKernel();
	}

	/**
	 * enables Drag and Drop for this listener
	 */
	public void enableDnD() {
		if (dropTarget == null) {
			dropTarget = new DropTarget(table, this);
		}
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
	}

	@Override
	public void drop(DropTargetDropEvent dropEvent) {
		Transferable t = dropEvent.getTransferable();
		int row = table.rowAtPoint(dropEvent.getLocation());
		try {

			/**
			 * handle a drop from an other cas cell, use this for substitution
			 */
			if (t.isDataFlavorSupported(CASTransferHandler.casTableFlavor)) {
				int cellnumber = (Integer) t
						.getTransferData(CASTransferHandler.casTableFlavor);
				String tableRef = "$" + (cellnumber + 1);

				GeoCasCell cell = table.getGeoCasCell(row);
				GeoCasCell source = table.getGeoCasCell(cellnumber);
				if (cell.getInputVE() == null || source.getInputVE() == null) {
					return;
				}
				// get output of the source cell, this should be changed for
				// dynamic reference
				String substitution = view.resolveCASrowReferences(tableRef,
						row);

				// dont use the same cell as source and destination
				if (cell.getRowNumber() == source.getRowNumber()) {
					return;
				}

				// view.ensureOneEmptyRow();
				GeoCasCell newcell = new GeoCasCell(cell.getConstruction());
				cell.getConstruction().addToConstructionList(newcell, false);
				view.insertRow(newcell, false);

				String subCmd = "Substitute[$" + (cell.getRowNumber() + 1)
						+ ", Flatten[{" + tableRef + "}]]";
				// the code commented below makes the substitution static
				// newcell.setInput(cell.getInput(StringTemplate.defaultTemplate));
				// the code below makes substitution dynamic
				newcell.setInput("$" + (cell.getRowNumber() + 1));

				newcell.setProcessingInformation(cell.getPrefix(), subCmd,
						cell.getPostfix());
				newcell.setEvalCommand("Substitute");
				if (substitution.startsWith("{")
						&& substitution.endsWith("}")) {
					substitution = substitution.substring(1,
							substitution.length() - 1);
				}
				newcell.setEvalComment(substitution);
				view.processRowThenEdit(newcell.getRowNumber());

				app.storeUndoInfo();
				return;
			}

			/**
			 * handle drops from algebra view
			 * 
			 * creates a new row in the CASTable and fills it up only with the
			 * value of the geo eg: Drop Element g: Line[A,B] -> CAS Cell: a*x +
			 * b*y = c without any assignment
			 */
			if (t.isDataFlavorSupported(
					AlgebraViewTransferHandler.algebraViewFlavor)) {
				String textImport;
				// get list of selected geo labels
				ArrayList<String> list = (ArrayList<String>) t.getTransferData(
						AlgebraViewTransferHandler.algebraViewFlavor);

				// exit if empty list
				if (list.size() == 0) {
					dropEvent.dropComplete(false);
					return;
				}

				// for one or more geos
				for (int i = 0; i < list.size(); i++) {
					GeoElement geo = kernel.lookupLabel(list.get(0));
					if (geo != null) {
						textImport = geo.getCASString(
								StringTemplate.defaultTemplate, false);
						GeoCasCell cell = new GeoCasCell(
								kernel.getConstruction());

						// insert new row and start editing
						view.insertRow(cell, true);

						// change the input accordingly to the drop
						if (geo.isGeoText()) {
							cell.setUseAsText(true);
							cell.setGeoText((GeoText) geo);
						} else {
							cell.setInput(textImport);
						}

						// stop editing and evaluate the new input
						app.setMode(EuclidianConstants.MODE_CAS_EVALUATE);
						table.stopEditing();
					}
				}
				table.updateAllRows();
				table.repaint();
				dropEvent.dropComplete(false);
				return;
			}

		} catch (Exception e) {
			Log.debug("CASDropTargetListener: exception in drop");
			e.printStackTrace();
		}

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
	}

}