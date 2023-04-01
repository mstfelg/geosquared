package org.geogebra.desktop.move.ggtapi.models;

import org.geogebra.common.move.ggtapi.models.AuthenticationModel;
import org.geogebra.desktop.main.AppPrefs;

/**
 * @author stefan
 * 
 */
public class AuthenticationModelD extends AuthenticationModel {

	/**
	 * The token value that indicates that the token is not available (login was
	 * not performed yet)
	 */
	public static final String TOKEN_NOT_AVAILABLE = "<n/a>";

	@Override
	public void storeLoginToken(String token) {
	}

	@Override
	public String getLoginToken() {
		return null;
	}

	@Override
	public void clearLoginToken() {
		storeLoginToken(TOKEN_NOT_AVAILABLE);
	}

	@Override
	protected void storeLastUser(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	public String loadLastUser() {
		// TODO Auto-generated method stub
		return null;
	}
}
