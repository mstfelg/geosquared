package org.geogebra.desktop.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import org.geogebra.common.gui.InputHandler;
import org.geogebra.common.gui.dialog.handler.NumberInputHandler;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.desktop.gui.GuiManagerD;
import org.geogebra.desktop.main.AppD;

/**
 * abstract class for input radius for any circle
 * 
 * @author mathieu
 * 
 */
public abstract class InputDialogRadiusD extends InputDialogD {

	/** current kernel */
	protected Kernel kernel;

	/**
	 * @param app application
	 * @param title title
	 * @param handler input handler
	 * @param kernel kernel
	 */
	public InputDialogRadiusD(AppD app, String title, InputHandler handler,
			Kernel kernel) {
		super(app, app.getLocalization().getMenu("Radius"), title, "", false,
				handler);

		this.kernel = kernel;
	}

	/**
	 * Handles button clicks for dialog.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		try {
			if (source == btOK || source == inputPanel.getTextComponent()) {
				processInput();
			} else if (source == btApply) {
				processInput();
			} else if (source == btCancel) {
				setVisibleForTools(false);
			}
		} catch (Exception ex) {
			// do nothing on uninitializedValue
			setVisibleForTools(false);
		}
	}

	private void processInput() {
		getInputHandler().processInput(inputPanel.getText(), this,
				ok -> {
					if (ok) {
						GeoElement circle = createOutput(
								((NumberInputHandler) getInputHandler())
										.getNum());
						GeoElement[] geos = { circle };
						app.storeUndoInfoAndStateForModeStarting();
						kernel.getApplication().getActiveEuclidianView()
								.getEuclidianController()
								.memorizeJustCreatedGeos(geos);
					}
					setVisibleForTools(!ok);
				});

	}

	/**
	 * @param num number
	 * @return the circle
	 */
	abstract protected GeoElement createOutput(GeoNumberValue num);

	@Override
	public void windowGainedFocus(WindowEvent arg0) {
		if (!wrappedDialog.isModal()) {
			app.resetCurrentSelectionListener();
		}
		((GuiManagerD) app.getGuiManager()).setCurrentTextfield(this, true);
	}

	@Override
	public void handleDialogVisibilityChange(boolean isVisible) {
		// nothing to do
	}
}
