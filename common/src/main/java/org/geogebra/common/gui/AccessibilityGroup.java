package org.geogebra.common.gui;

import org.geogebra.common.main.App;

public enum AccessibilityGroup {
	MENU, ALGEBRA_CLOSE, UNDO, REDO, EV_CONTROLS, EV2_CONTROLS, EV3D_CONTROLS, PAGE_LIST_OPEN,
	NOTES_TOOLBAR_HEADER, NOTES_TOOL_SELECT, NOTES_TOOL_TOOLS, NOTES_TOOL_MEDIA,
	NOTES_COLOR_PANEL, NOTES_COLOR_CUSTOM, NOTES_PEN_THICKNESS_SLIDER,
	ALT_GEOTEXT, GEO_ELEMENT, ALGEBRA_ITEM;

	public enum ViewControlId {
		ALT_GEO,
		RESET_BUTTON,
		SETTINGS_BUTTON, ZOOM_NOTES_DRAG_VIEW,
		ZOOM_NOTES_SPOTLIGHT, ZOOM_NOTES_PLUS, ZOOM_NOTES_MINUS,
		ZOOM_NOTES_HOME, PLAY_BUTTON, ZOOM_PANEL_HOME,
		ZOOM_PANEL_PLUS, ZOOM_PANEL_MINUS, FULL_SCREEN
	}

	/**
	 * @param viewId view ID
	 * @return accessibility group for view controls
	 */
	public static AccessibilityGroup getViewGroup(int viewId) {
		switch (viewId) {
			case App.VIEW_EUCLIDIAN3D:
				return  AccessibilityGroup.EV3D_CONTROLS;
			case App.VIEW_EUCLIDIAN2:
				return  AccessibilityGroup.EV2_CONTROLS;
			default:
				return  AccessibilityGroup.EV_CONTROLS;
		}
	}
}
