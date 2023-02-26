package org.geogebra.desktop.gui.dialog.options;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.geogebra.common.gui.SetLabels;
import org.geogebra.common.main.App.InputPosition;
import org.geogebra.common.main.Localization;
import org.geogebra.common.main.settings.Settings;
import org.geogebra.desktop.gui.util.FullWidthLayout;
import org.geogebra.desktop.gui.util.LayoutUtil;
import org.geogebra.desktop.main.AppD;
import org.geogebra.desktop.util.GuiResourcesD;

/**
 * Layout options for the options dialog.
 */
public class OptionsLayoutD
		implements OptionPanelD, ActionListener, FocusListener, SetLabels {

	private AppD app;
	private Settings settings;

	private JPanel sideBarPanel;
	private JPanel inputBarPanel;
	private JPanel toolbarPanel;
	private JPanel perspectivesPanel;

	private JCheckBox ckShowInputHelp;
	private JCheckBox ckShowTitleBar;
	private JCheckBox ckAllowStyleBar;
	private JCheckBox ckShowInputBar;
	private JCheckBox ckShowToolbar;
	private JCheckBox ckShowToolHelp;
	private JCheckBox ckShowSideBar;

	private JToggleButton rbToolbarNorth;
	private JToggleButton rbToolbarSouth;
	private JToggleButton rbToolbarEast;
	private JToggleButton rbToolbarWest;
	private JToggleButton rbSidebarWest;
	private JToggleButton rbSidebarEast;

	private JRadioButton rbPespectiveSidebar;
	private JRadioButton rbButtonSidebar;

	private JToggleButton rbInputBarSouth;
	private JToggleButton rbInputBarNorth;

	private JLabel lblInputBarPosition;

	private final JPanel wrappedPanel;

	/**
	 * Construct layout option panel.
	 * 
	 * @param app application
	 */
	public OptionsLayoutD(AppD app) {
		this.wrappedPanel = new JPanel(new BorderLayout());

		this.app = app;
		this.settings = app.getSettings();

		initGUI();
		updateGUI();
		setLabels();

	}

	private void reinit() {
		wrappedPanel.removeAll();
		initGUI();
		updateGUI();
		setLabels();
	}

	/**
	 * Initialize the user interface.
	 * 
	 * @remark updateGUI() will be called directly after this method
	 * @remark Do not use translations here, the option dialog will take care of
	 *         calling setLabels()
	 */
	private void initGUI() {

		initInputbarPanel();
		initPerspectivesPanel();
		initToolBarPanel();
		initSideBarPanel();

		JPanel panel = new JPanel();
		panel.setLayout(new FullWidthLayout());
		panel.add(inputBarPanel);
		panel.add(toolbarPanel);
		panel.add(perspectivesPanel);
		panel.add(sideBarPanel);

		panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(4);
		// scrollPane.setBorder(BorderFactory.createEmptyBorder());

		wrappedPanel.add(scrollPane, BorderLayout.CENTER);

	}

	/**
	 * Initialize the perspectives (view) panel.
	 */
	private void initPerspectivesPanel() {

		perspectivesPanel = new JPanel();
		perspectivesPanel
				.setLayout(new BoxLayout(perspectivesPanel, BoxLayout.Y_AXIS));

		ckShowTitleBar = new JCheckBox();
		ckShowTitleBar.addActionListener(this);
		perspectivesPanel.add(LayoutUtil.flowPanel(ckShowTitleBar));

		ckAllowStyleBar = new JCheckBox();
		ckAllowStyleBar.addActionListener(this);
		perspectivesPanel.add(LayoutUtil.flowPanel(ckAllowStyleBar));

	}

	/**
	 * Initialize the input bar panel.
	 */
	private void initInputbarPanel() {

		inputBarPanel = new JPanel();
		inputBarPanel.setLayout(new BoxLayout(inputBarPanel, BoxLayout.Y_AXIS));

		ckShowInputBar = new JCheckBox();
		ckShowInputBar.addActionListener(this);

		ButtonGroup inputbarPosGroup = new ButtonGroup();

		rbInputBarNorth = new JToggleButton(
				app.getScaledIcon(GuiResourcesD.LAYOUT_NORTH));
		rbInputBarNorth.addActionListener(this);
		inputbarPosGroup.add(rbInputBarNorth);

		rbInputBarSouth = new JToggleButton(
				app.getScaledIcon(GuiResourcesD.LAYOUT_SOUTH));
		rbInputBarSouth.addActionListener(this);
		inputbarPosGroup.add(rbInputBarSouth);

		lblInputBarPosition = new JLabel();

		inputBarPanel.add(LayoutUtil.flowPanel(ckShowInputBar,
				Box.createHorizontalStrut(5), rbInputBarNorth,
				rbInputBarSouth));

		ckShowInputHelp = new JCheckBox();
		ckShowInputHelp.addActionListener(this);

		int tab = 20;
		inputBarPanel.add(LayoutUtil.flowPanel(tab, ckShowInputHelp));
	}

	/**
	 * Initialize the sidebar panel.
	 */
	private void initSideBarPanel() {

		sideBarPanel = new JPanel();
		sideBarPanel.setLayout(new BoxLayout(sideBarPanel, BoxLayout.Y_AXIS));

		ckShowSideBar = new JCheckBox();
		ckShowSideBar.addActionListener(this);

		ButtonGroup grp = new ButtonGroup();
		rbSidebarWest = new JToggleButton(
				app.getScaledIcon(GuiResourcesD.LAYOUT_WEST));
		rbSidebarWest.addActionListener(this);
		grp.add(rbSidebarWest);
		rbSidebarEast = new JToggleButton(
				app.getScaledIcon(GuiResourcesD.LAYOUT_EAST));
		rbSidebarEast.setSelected(true);
		rbSidebarEast.addActionListener(this);
		grp.add(rbSidebarEast);

		ButtonGroup grp2 = new ButtonGroup();
		rbPespectiveSidebar = new JRadioButton();
		rbPespectiveSidebar.addActionListener(this);
		rbPespectiveSidebar.setSelected(true);
		grp2.add(rbPespectiveSidebar);
		rbButtonSidebar = new JRadioButton();
		rbButtonSidebar.addActionListener(this);
		grp2.add(rbButtonSidebar);

		sideBarPanel.add(LayoutUtil.flowPanel(ckShowSideBar,
				Box.createHorizontalStrut(5), rbSidebarWest, rbSidebarEast));
	}

	/**
	 * Initialize the tool bar panel.
	 */
	private void initToolBarPanel() {

		toolbarPanel = new JPanel();
		toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.Y_AXIS));

		ckShowToolbar = new JCheckBox();
		ckShowToolbar.addActionListener(this);

		ckShowToolHelp = new JCheckBox();
		ckShowToolHelp.addActionListener(this);

		// TODO need to create a method to set a flag for this in the gui
		// manager
		ckShowToolHelp.setSelected(true);

		ButtonGroup toolBarPosGroup = new ButtonGroup();

		rbToolbarNorth = new JToggleButton(
				app.getScaledIcon(GuiResourcesD.LAYOUT_NORTH));
		rbToolbarNorth.addActionListener(this);
		toolBarPosGroup.add(rbToolbarNorth);

		rbToolbarSouth = new JToggleButton(
				app.getScaledIcon(GuiResourcesD.LAYOUT_SOUTH));
		rbToolbarSouth.addActionListener(this);
		toolBarPosGroup.add(rbToolbarSouth);

		rbToolbarEast = new JToggleButton(
				app.getScaledIcon(GuiResourcesD.LAYOUT_EAST));
		rbToolbarEast.addActionListener(this);
		toolBarPosGroup.add(rbToolbarEast);

		rbToolbarWest = new JToggleButton(
				app.getScaledIcon(GuiResourcesD.LAYOUT_WEST));
		rbToolbarWest.addActionListener(this);
		toolBarPosGroup.add(rbToolbarWest);

		lblInputBarPosition = new JLabel();
		toolbarPanel.add(LayoutUtil.flowPanel(ckShowToolbar,
				Box.createHorizontalStrut(5), rbToolbarNorth,
				Box.createHorizontalStrut(5), rbToolbarSouth,
				Box.createHorizontalStrut(5), rbToolbarWest,
				Box.createHorizontalStrut(5), rbToolbarEast));
		int tab = 20;
		toolbarPanel.add(LayoutUtil.flowPanel(tab, ckShowToolHelp));

	}

	/**
	 * Update the user interface, ie change selected values.
	 * 
	 * @remark Do not call setLabels() here
	 */
	@Override
	public void updateGUI() {

		ckShowInputBar.setSelected(app.showAlgebraInput());

		ckShowInputHelp.setSelected(app.showInputHelpToggle());

		rbToolbarNorth
				.setSelected(app.getToolbarPosition() == SwingConstants.NORTH);
		rbToolbarSouth
				.setSelected(app.getToolbarPosition() == SwingConstants.SOUTH);
		rbToolbarWest
				.setSelected(app.getToolbarPosition() == SwingConstants.WEST);
		rbToolbarEast
				.setSelected(app.getToolbarPosition() == SwingConstants.EAST);
		boolean inputOnTop = app.getInputPosition() == InputPosition.top;
		rbInputBarNorth.setSelected(inputOnTop);
		rbInputBarSouth.setSelected(!inputOnTop);
		ckShowToolbar.setSelected(app.showToolBar());
		ckShowToolHelp.setSelected(app.showToolBarHelp());

		rbToolbarNorth.setEnabled(ckShowToolbar.isSelected());
		rbToolbarSouth.setEnabled(ckShowToolbar.isSelected());
		rbToolbarEast.setEnabled(ckShowToolbar.isSelected());
		rbToolbarWest.setEnabled(ckShowToolbar.isSelected());

		rbInputBarNorth.setEnabled(ckShowInputBar.isSelected());
		rbInputBarSouth.setEnabled(ckShowInputBar.isSelected());

		// ckIgnoreDocumentLayout.setSelected(settings.getLayout()
		// .isIgnoringDocumentLayout());
		ckShowTitleBar.setSelected(settings.getLayout().showTitleBar());
		ckAllowStyleBar.setSelected(settings.getLayout().isAllowingStyleBar());

		ckShowSideBar.removeActionListener(this);
		ckShowSideBar.addActionListener(this);

		rbSidebarEast.removeActionListener(this);
		rbSidebarWest.removeActionListener(this);
		rbButtonSidebar.removeActionListener(this);
		rbPespectiveSidebar.removeActionListener(this);

		rbSidebarEast.addActionListener(this);
		rbSidebarWest.addActionListener(this);
		rbButtonSidebar.addActionListener(this);
		rbPespectiveSidebar.addActionListener(this);
		revalidate();

	}

	/**
	 * Values changed.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		Object source = e.getSource();

		// dock panel (perspective) settings
		if (source == ckShowTitleBar) {
			settings.getLayout().setShowTitleBar(ckShowTitleBar.isSelected());
			// } else if (source == ckIgnoreDocumentLayout) {
			// settings.getLayout().setIgnoreDocumentLayout(
			// ckIgnoreDocumentLayout.isSelected());
		} else if (source == ckAllowStyleBar) {
			settings.getLayout().setAllowStyleBar(ckAllowStyleBar.isSelected());

			// tool bar settings
		} else if (source == ckShowToolbar || source == ckShowToolHelp) {
			app.setShowToolBar(ckShowToolbar.isSelected(),
					ckShowToolHelp.isSelected());
			app.updateApplicationLayout();
			app.updateToolBarLayout();
			app.getGuiManager().updateToolbar();
		} else if (source == rbToolbarNorth) {
			app.setToolbarPosition(SwingConstants.NORTH, true);
		} else if (source == rbToolbarSouth) {
			app.setToolbarPosition(SwingConstants.SOUTH, true);
		} else if (source == rbToolbarEast) {
			app.setToolbarPosition(SwingConstants.EAST, true);
		} else if (source == rbToolbarWest) {
			app.setToolbarPosition(SwingConstants.WEST, true);
		}

		// input bar settings
		else if (source == rbInputBarNorth) {
			app.setInputPosition(InputPosition.top, true);
		} else if (source == rbInputBarSouth) {
			app.setInputPosition(InputPosition.bottom, true);
		} else if (source == ckShowInputBar) {
			app.setShowAlgebraInput(ckShowInputBar.isSelected(), true);
		} else if (source == ckShowInputHelp) {
			app.setShowInputHelpToggle(ckShowInputHelp.isSelected());
		}

		wrappedPanel.requestFocus();
		updateGUI();

	}

	/**
	 * Not implemented.
	 */
	@Override
	public void focusGained(FocusEvent e) {
		// nothing to do
	}

	/**
	 * Apply textfield changes.
	 */
	@Override
	public void focusLost(FocusEvent e) {
		// nothing to do

	}

	/**
	 * Update the language of the user interface.
	 */
	@Override
	public void setLabels() {
		Localization loc = app.getLocalization();
		// input bar panel
		inputBarPanel
				.setBorder(LayoutUtil.titleBorder(loc.getMenu("InputField")));
		ckShowInputBar.setText(loc.getMenu("Show"));
		ckShowInputHelp.setText(loc.getMenu("CmdList"));
		lblInputBarPosition.setText(loc.getMenu("Position"));

		// tool bar panel
		toolbarPanel.setBorder(LayoutUtil.titleBorder(loc.getMenu("Toolbar")));
		ckShowToolbar.setText(loc.getMenu("Show"));
		ckShowToolHelp.setText(loc.getMenu("ShowToolBarHelp"));

		// perspectives panel
		perspectivesPanel
				.setBorder(LayoutUtil.titleBorder(loc.getMenu("View")));
		ckShowTitleBar.setText(loc.getMenu("ShowTitleBar"));
		ckAllowStyleBar.setText(loc.getMenu("AllowStyleBar"));
		// side bar panel
		sideBarPanel.setBorder(LayoutUtil.titleBorder(loc.getMenu("Sidebar")));
		ckShowSideBar.setText(loc.getMenu("ShowSidebar"));
		rbPespectiveSidebar.setText(loc.getMenu("PerspectivePanel"));
		rbButtonSidebar.setText(loc.getMenu("ViewPanel"));
	}

	@Override
	public JPanel getWrappedPanel() {
		return this.wrappedPanel;
	}

	@Override
	public void revalidate() {
		getWrappedPanel().revalidate();

	}

	@Override
	public void setBorder(Border border) {
		this.wrappedPanel.setBorder(border);
	}

	@Override
	public void applyModifications() {
		// override this method to make the properties view apply modifications
		// when panel changes
	}

	@Override
	public void updateFont() {

		Font font = app.getPlainFont();

		// input bar panel
		inputBarPanel.setFont(font);
		ckShowInputBar.setFont(font);
		ckShowInputHelp.setFont(font);
		lblInputBarPosition.setFont(font);

		// tool bar panel
		toolbarPanel.setFont(font);
		ckShowToolbar.setFont(font);
		ckShowToolHelp.setFont(font);

		// perspectives panel
		perspectivesPanel.setFont(font);
		ckShowTitleBar.setFont(font);
		ckAllowStyleBar.setFont(font);

		// sidebar panel
		sideBarPanel.setFont(font);
		ckShowSideBar.setFont(font);

		reinit();
	}

	@Override
	public void setSelected(boolean flag) {
		// see OptionsEuclidianD for possible implementation
	}
}
