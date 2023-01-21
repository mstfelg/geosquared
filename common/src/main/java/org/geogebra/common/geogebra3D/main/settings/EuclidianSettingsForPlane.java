package org.geogebra.common.geogebra3D.main.settings;

import org.geogebra.common.main.App;
import org.geogebra.common.main.settings.EuclidianSettings;

/**
 * Settings for view for plane
 * 
 * @author mathieu
 *
 */
public class EuclidianSettingsForPlane extends EuclidianSettings {
	private boolean mirror = false;
	private int rotate = 0;
	private boolean isFromLoadFile = false;

	/**
	 * constructor
	 * 
	 * @param app
	 *            application
	 */
	public EuclidianSettingsForPlane(App app) {
		super(app);
	}

	/**
	 * set transform for plane
	 * 
	 * @param mirror
	 *            mirrored
	 * @param rotate
	 *            rotated
	 */
	public void setTransformForPlane(boolean mirror, int rotate) {
		this.mirror = mirror;
		this.rotate = rotate;

	}

	/**
	 * 
	 * @return if mirrored
	 */
	public boolean getMirror() {
		return mirror;
	}

	/**
	 * 
	 * @return rotation angle
	 */
	public int getRotate() {
		return rotate;
	}

	@Override
	public boolean isViewForPlane() {
		return true;
	}

	/**
	 * set these settings created from loading file or not
	 * 
	 * @param flag
	 *            flag
	 */
	public void setFromLoadFile(boolean flag) {
		isFromLoadFile = flag;
	}

	/**
	 * 
	 * @return if these settings created from loading file or not
	 */
	public boolean isFromLoadFile() {
		return isFromLoadFile;
	}

}
