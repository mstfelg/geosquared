package org.geogebra.desktop.move.ggtapi.models;

import javax.swing.SwingUtilities;

import org.geogebra.common.GeoGebraConstants;
import org.geogebra.common.main.App;
import org.geogebra.common.main.Feature;
import org.geogebra.common.move.events.GenericEvent;
import org.geogebra.common.move.ggtapi.models.AuthenticationModel;
import org.geogebra.common.move.ggtapi.models.ClientInfo;
import org.geogebra.common.move.ggtapi.models.GeoGebraTubeUser;
import org.geogebra.common.move.ggtapi.operations.LogInOperation;
import org.geogebra.common.move.views.EventRenderable;
import org.geogebra.desktop.util.URLEncoderD;

/**
 * The desktop version of the login operation. uses an own AuthenticationModel
 * and an own implementation of the API
 * 
 * @author stefan
 */
public class LoginOperationD extends LogInOperation {

	private App app;

	/**
	 * Initializes the LoginOperation for Desktop by creating the corresponding
	 * model and view classes
	 * 
	 * @param app
	 *            application
	 */
	public LoginOperationD(App app) {
		super();
		this.app = app;
		setModel(new AuthenticationModelD());
	}

	@Override
	public void performTokenLogin(String token, boolean automatic) {

		// Perform the API call in a background process
		doPerformTokenLogin(new GeoGebraTubeUser(token), automatic);
	}

	private GeoGebraTubeAPID api;

	@Override
	public GeoGebraTubeAPID getGeoGebraTubeAPI() {
		if (api == null) {
			ClientInfo client = new ClientInfo();
			client.setModel((AuthenticationModel) this.model);
			client.setType("desktop");
			client.setWidth(1024);
			client.setWidth(768);
			api = new GeoGebraTubeAPID(app.has(Feature.TUBE_BETA), client);
		}
		return api;
	}

	@Override
	protected String getURLLoginCaller() {
		return "desktop";
	}

	@Override
	protected String getURLClientInfo() {
		URLEncoderD enc = new URLEncoderD();
		return enc.encode("GeoGebra Desktop Application V"
				+ GeoGebraConstants.VERSION_STRING);
	}

	@Override
	public void dispatchEvent(final GenericEvent<EventRenderable> event) {

		// call the gui event on the Event dispatch thread.
		SwingUtilities.invokeLater(() -> LoginOperationD.super.dispatchEvent(event));
	}

	/**
	 * @return boolean if the tube API is available
	 */
	public boolean isTubeAvailable() {
		return getGeoGebraTubeAPI().isAvailable();
	}

	/**
	 * @return boolean if the check for the availability of tube is finished
	 */
	public boolean isTubeCheckDone() {
		return getGeoGebraTubeAPI().isCheckDone();
	}

}
