package org.geogebra.common.gui.menu.impl;

import org.geogebra.common.GeoGebraConstants;
import org.geogebra.common.gui.menu.Action;
import org.geogebra.common.gui.menu.ActionableItem;
import org.geogebra.common.gui.menu.DrawerMenu;
import org.geogebra.common.gui.menu.Icon;
import org.geogebra.common.gui.menu.MenuItem;
import org.geogebra.common.gui.menu.MenuItemGroup;
import org.geogebra.common.move.ggtapi.models.GeoGebraTubeUser;
import org.geogebra.common.move.ggtapi.operations.LogInOperation;

/**
 * Menu factory creating the default menus for all app versions and platforms.
 */
public class DefaultDrawerMenuFactory extends AbstractDrawerMenuFactory {

	private final GeoGebraConstants.Platform platform;
	private final LogInOperation logInOperation;
	private final boolean createExamEntry;
	private final boolean enableFileFeatures;
	private final String versionNumber;

	/**
	 * Create a new DrawerMenuFactory.
	 * @param platform platform
	 * @param version version
	 */
	public DefaultDrawerMenuFactory(GeoGebraConstants.Platform platform,
			GeoGebraConstants.Version version) {
		this(platform, version, null);
	}

	/**
	 * Create a new DrawerMenuFactory.
	 * @param platform platform
	 * @param version version
	 * @param logInOperation if loginOperation is not null, it creates menu options that require
	 * login based on the {@link LogInOperation#isLoggedIn()} method.
	 */
	public DefaultDrawerMenuFactory(GeoGebraConstants.Platform platform,
			GeoGebraConstants.Version version,
			LogInOperation logInOperation) {
		this(platform, version, logInOperation, false);
	}

	/**
	 * Create a new DrawerMenuFactory.
	 * @param platform platform
	 * @param version version
	 * @param logInOperation if loginOperation is not null, it creates menu options that require
	 * login based on the {@link LogInOperation#isLoggedIn()} method.
	 * @param createExamEntry whether the factory should create the start exam button
	 */
	public DefaultDrawerMenuFactory(GeoGebraConstants.Platform platform,
			GeoGebraConstants.Version version,
			LogInOperation logInOperation,
			boolean createExamEntry) {
		this(platform, version, null, logInOperation, createExamEntry, true);
	}

	/**
	 * Create a new DrawerMenuFactory.
	 * @param platform platform
	 * @param version version
	 * @param logInOperation if loginOperation is not null, it creates menu options that require
	 * login based on the {@link LogInOperation#isLoggedIn()} method.
	 * @param enableFileFeatures whether to show sign-in related file features
	 * @param createExamEntry whether the factory should create the start exam button
	 */
	public DefaultDrawerMenuFactory(GeoGebraConstants.Platform platform,
			GeoGebraConstants.Version version,
			String versionNumber,
			LogInOperation logInOperation,
			boolean createExamEntry,
			boolean enableFileFeatures) {
		this(platform, version, versionNumber, logInOperation, createExamEntry,
				enableFileFeatures, false);
	}

	/**
	 * Create a new DrawerMenuFactory.
	 * @param platform platform
	 * @param version version
	 * @param versionNumber version number
	 * @param logInOperation if loginOperation is not null, it creates menu options that require
	 * login based on the {@link LogInOperation#isLoggedIn()} method.
	 * @param createExamEntry whether the factory should create the start exam button
	 * @param enableFileFeatures whether to show sign-in related file features
	 * @param isSuiteApp whether it is the Suite app
	 */
	public DefaultDrawerMenuFactory(GeoGebraConstants.Platform platform,
			GeoGebraConstants.Version version,
			String versionNumber,
			LogInOperation logInOperation,
			boolean createExamEntry,
			boolean enableFileFeatures,
			boolean isSuiteApp) {
		super(version, isSuiteApp);
		this.platform = platform;
		this.versionNumber = versionNumber;
		this.logInOperation = logInOperation;
		this.createExamEntry = createExamEntry;
		this.enableFileFeatures = enableFileFeatures;
	}

	@Override
	public DrawerMenu createDrawerMenu() {
		MenuItemGroup main = createMainMenuItemGroup();
		MenuItemGroup secondary = createSecondaryMenuItemGroup();
		MenuItemGroup userGroup = createUserGroup();
		String title = getMenuTitle();
		return new DrawerMenuImpl(title, removeNulls(main, secondary, userGroup));
	}

	private MenuItemGroup createMainMenuItemGroup() {
		MenuItem clearConstruction = enableFileFeatures ? clearConstruction() : null;
		MenuItem openFile = enableFileFeatures ? openFile() : null;
		MenuItem save = enableFileFeatures && logInOperation != null ? saveFile() : null;
		MenuItem share = enableFileFeatures ? share() : null;
		MenuItem downloadAs = isDesktop() ? showDownloadAs() : null;
		MenuItem printPreview = hasPrintPreview() ? previewPrint() : null;
		MenuItem startExamMode = createExamEntry ? startExamMode() : null;
		if (version == GeoGebraConstants.Version.SCIENTIFIC) {
			return new MenuItemGroupImpl(removeNulls(clearConstruction, startExamMode));
		}
		return new MenuItemGroupImpl(removeNulls(clearConstruction, openFile, save, share,
				exportImage(), downloadAs, printPreview, startExamMode));
	}

	private boolean hasPrintPreview() {
		return isDesktop() && version != GeoGebraConstants.Version.PROBABILITY;
	}

	private MenuItemGroup createSecondaryMenuItemGroup() {
		return new MenuItemGroupImpl(removeNulls(showSwitchCalculator(),
				showSettings()));
	}

	private MenuItemGroup createUserGroup() {
		if (enableFileFeatures && logInOperation != null) {
			return createUserGroup(logInOperation);
		}
		return null;
	}

	private static MenuItemGroup createUserGroup(LogInOperation logInOperation) {
		if (logInOperation.isLoggedIn()) {
			GeoGebraTubeUser user = logInOperation.getModel().getLoggedInUser();
			MenuItem userItem = new ActionableItemImpl(Icon.USER_ICON,
					user.getUserName(), Action.OPEN_PROFILE_PAGE);
			MenuItem signOut = new ActionableItemImpl(Icon.SIGN_OUT,
					"SignOut", Action.SIGN_OUT);
			return new MenuItemGroupImpl(userItem, signOut);
		} else {
			MenuItem signIn = new ActionableItemImpl(Icon.SIGN_IN, "SignIn", Action.SIGN_IN);
			return new MenuItemGroupImpl(signIn);
		}
	}

	private boolean isMobile() {
		return platform == GeoGebraConstants.Platform.ANDROID
				|| platform == GeoGebraConstants.Platform.IOS;
	}

	private boolean isDesktop() {
		return !isMobile();
	}

	public boolean isOffline() {
		return platform == GeoGebraConstants.Platform.OFFLINE;
	}

	public boolean enableFileFeatures() {
		return enableFileFeatures;
	}

	private static MenuItem startExamMode() {
		return new ActionableItemImpl(Icon.HOURGLASS_EMPTY,
				"exam_menu_entry", Action.START_EXAM_MODE);
	}

	protected static MenuItem share() {
		return new ActionableItemImpl(Icon.EXPORT_FILE, "Share", Action.SHARE_FILE);
	}

	protected static MenuItem exportImage() {
		return new ActionableItemImpl(Icon.EXPORT_IMAGE, "exportImage", Action.EXPORT_IMAGE);
	}

	protected static MenuItem showSettings() {
		return new ActionableItemImpl(Icon.SETTINGS, "Settings", Action.SHOW_SETTINGS);
	}

	protected static MenuItem previewPrint() {
		return new ActionableItemImpl(Icon.PRINT, "PrintPreview", Action.PREVIEW_PRINT);
	}

	protected MenuItem showDownloadAs() {
		ActionableItem png = new ActionableItemImpl(null, "Download.PNGImage", Action.DOWNLOAD_PNG);
		ActionableItem svg = new ActionableItemImpl(null, "Download.SVGImage", Action.DOWNLOAD_SVG);
		ActionableItem pdf = new ActionableItemImpl(null,
				"Download.PDFDocument", Action.DOWNLOAD_PDF);
		switch (version) {
		case PROBABILITY:
			return buildDownloadAs(createDownloadGgb(), png);
		case NOTES:
			return buildDownloadAs(createDownloadSlides(), png, svg, pdf);
		case GRAPHING_3D:
			ActionableItem dae = new ActionableItemImpl(
					"Download.ColladaDae", Action.DOWNLOAD_COLLADA_DAE);
			ActionableItem html = new ActionableItemImpl(
					"Download.ColladaHtml", Action.DOWNLOAD_COLLADA_HTML);
			return buildDownloadAs(createDownloadGgb(), png, createDownloadStl(), dae, html);
		default:
			return buildDownloadAs(createDownloadGgb(), png, svg, pdf, createDownloadStl());
		}
	}

	private MenuItem buildDownloadAs(ActionableItem... items) {
		return new SubmenuItemImpl(Icon.DOWNLOAD, "DownloadAs", null, items);
	}

	private static ActionableItem createDownloadGgb() {
		return new ActionableItemImpl("Download.GeoGebraFile", Action.DOWNLOAD_GGB);
	}

	private static ActionableItem createDownloadSlides() {
		return new ActionableItemImpl("Download.SlidesGgs", Action.DOWNLOAD_GGS);
	}

	private static ActionableItem createDownloadStl() {
		return new ActionableItemImpl("Download.3DPrint", Action.DOWNLOAD_STL);
	}

	public LogInOperation getLogInOperation() {
		return logInOperation;
	}
}
