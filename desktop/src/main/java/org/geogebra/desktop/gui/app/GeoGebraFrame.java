/* 
 GeoGebra - Dynamic Mathematics for Everyone
 http://www.geogebra.org

 This file is part of GeoGebra.

 This program is free software; you can redistribute it and/or modify it 
 under the terms of the GNU General Public License as published by 
 the Free Software Foundation.

 */

/**
 * GeoGebra Application
 *
 * @author Markus Hohenwarter
 */

package org.geogebra.desktop.gui.app;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.dnd.DropTarget;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.Macro;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.util.DoubleUtil;
import org.geogebra.common.util.StringUtil;
import org.geogebra.common.util.debug.Log;
import org.geogebra.desktop.CommandLineArguments;
import org.geogebra.desktop.awt.GDimensionD;
import org.geogebra.desktop.euclidianND.EuclidianViewInterfaceD;
import org.geogebra.desktop.export.GraphicExportDialog;
import org.geogebra.desktop.geogebra3D.euclidian3D.EuclidianView3DD;
import org.geogebra.desktop.gui.FileDropTargetListener;
import org.geogebra.desktop.gui.GuiManagerD;
import org.geogebra.desktop.gui.dialog.DialogManagerD;
import org.geogebra.desktop.gui.util.AnimatedGifEncoder;
import org.geogebra.desktop.main.AppD;
import org.geogebra.desktop.main.AppPrefs;
import org.geogebra.desktop.util.FrameCollector;

import com.himamis.retex.editor.share.util.Unicode;

/**
 * GeoGebra's main window.
 */
public class GeoGebraFrame extends JFrame
		implements WindowFocusListener, Printable, ComponentListener {

	private static final long serialVersionUID = 1L;

	private static List<NewInstanceListener> instanceListener = new ArrayList<>();
	private static ArrayList<GeoGebraFrame> instances = new ArrayList<>();
	private static GeoGebraFrame activeInstance;
	private static Object lock = new Object();

	private static FileDropTargetListener dropTargetListener;
	protected AppD app;
	private Timer timer;
	private long born;

	/**
	 * New frame
	 */
	public GeoGebraFrame() {
		instances.add(this);
		setActiveInstance(this);
		born = System.currentTimeMillis();
		this.addComponentListener(this);
	}
	
	public GeoGebraFrame(String[] args) {
		// set Application's size, position and font size

		final AppD app = new AppD(args, null, this);
		app.getGuiManager().initMenubar();

		// init GUI
		this.app = app;
		this.getContentPane().add(app.buildApplicationPanel());
		dropTargetListener = new FileDropTargetListener(app);
		this.setGlassPane(((GuiManagerD) app.getGuiManager()).getLayout()
				.getDockManager().getGlassPane());
		this.setDropTarget(new DropTarget(this, dropTargetListener));
		this.addWindowFocusListener(this);
		updateAllTitles();

		app.updateMenubar();

		this.setVisible(true);

		// init some things in the background
		Thread runner = GeoGebraFrame.createAppThread(app);
		runner.start();

		checkCommandLineExport(app);

		for (NewInstanceListener l : instanceListener) {
			l.newInstance(this);
		}
	}

	/**
	 * Disposes this frame and removes it from the static instance list.
	 */
	@Override
	public void dispose() {
		instances.remove(this);
		if (this == activeInstance) {
			setActiveInstance(null);
		}
	}

	public AppD getApplication() {
		return app;
	}

	public void setApplication(AppD app) {
		this.app = app;
	}

	/**
	 * @return number of instances
	 */
	public int getInstanceNumber() {
		for (int i = 0; i < instances.size(); i++) {
			if (this == instances.get(i)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void windowGainedFocus(WindowEvent arg0) {
		setActiveInstance(this);
		app.updateMenuWindow();
	}

	@Override
	public void windowLostFocus(WindowEvent arg0) {

		// fix for Mac OS bug: close open popups manually
		Window[] w = this.getOwnedWindows();
		for (Window win : w) {
			if (win.getClass().getName()
					.equals("javax.swing.Popup$HeavyWeightWindow")) {
				win.setVisible(false);
			}
		}
	}

	@Override
	public Locale getLocale() {
		Locale defLocale = AppPrefs.getPref().getDefaultLocale();
		if (defLocale == null) {
			return super.getLocale();
		}
		return defLocale;
	}

	@Override
	public void setVisible(boolean flag) {
		if (flag) {
			updateSize();

			// set location
			int instanceID = instances.size() - 1;
			if (instanceID > 0) {
				// move right and down of last instance
				GeoGebraFrame prevInstance = getInstance(instanceID - 1);
				Point loc = prevInstance.getLocation();

				// make sure we stay on screen
				Dimension d1 = getSize();
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				loc.x = Math.min(loc.x + 20, dim.width - d1.width);
				loc.y = Math.min(loc.y + 20, dim.height - d1.height - 25);
				setLocation(loc);
			} else {
				// center
				setLocationRelativeTo(null);
			}

			super.setVisible(true);
			app.getActiveEuclidianView().requestFocusInWindow();
		} else {
			if (!isShowing()) {
				return;
			}

			instances.remove(this);

			if (instances.size() == 0) {
				super.setVisible(false);
				dispose();
				AppD.exit(0);
			} else {
				super.setVisible(false);
				updateAllTitles();
			}
		}
	}

	/**
	 * Provide temporary information about window size (for applet designers)
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		if (System.currentTimeMillis() < born + 5000) {
			return;
		}

		this.setTitle((int) getSize().getWidth() + "" + Unicode.MULTIPLY + ""
				+ (int) getSize().getHeight());

		if (timer == null) {
			timer = new Timer(3000, e1 -> setTitle(getPreferredTitle()));
		}
		timer.setRepeats(false);
		timer.restart();
	}

	String getPreferredTitle() {
		return app.getCurrentFile() == null ? "GeoSqured"
				: app.getCurrentFile().getName();
	}

	/**
	 * Set actual size from preferred size (e.g. loaded from file)
	 */
	public void updateSize() {
		// get frame size from layout manager
		Dimension size = GDimensionD.getAWTDimension(app.getPreferredSize());

		// check if frame fits on screen
		Rectangle screenSize = AppD.getScreenSize();

		if (size.width > screenSize.width || size.height > screenSize.height) {
			size.width = screenSize.width;
			size.height = screenSize.height;
			setLocation(0, 0);
		}

		setSize(size);
	}

	/**
	 * Main method to create initial GeoGebra window.
	 * 
	 * @param args
	 *            file name parameter
	 */
	public static synchronized void main(CommandLineArguments args) {
		createNewWindow(args, new GeoGebraFrame());
	}

	public static synchronized GeoGebraFrame
		createNewWindow(CommandLineArguments args) {
		return createNewWindow(args, new GeoGebraFrame());
	}

	public synchronized GeoGebraFrame
		createNewWindow(CommandLineArguments args, Macro macro) {
		return createNewWindow(args, copy());
	}

	/**
	 * Creates new GeoGebra window
	 * 
	 * @param args
	 *            Command line arguments
	 * @param wnd frame
	 * @return the new window
	 */
	public static synchronized GeoGebraFrame
		createNewWindow(final CommandLineArguments args, GeoGebraFrame wnd) {
		// set Application's size, position and font size

		final AppD app = wnd.createApplication(args, wnd);
		app.getGuiManager().initMenubar();

		// init GUI
		wnd.app = app;
		wnd.getContentPane().add(app.buildApplicationPanel());
		dropTargetListener = new FileDropTargetListener(app);
		wnd.setGlassPane(((GuiManagerD) app.getGuiManager()).getLayout()
				.getDockManager().getGlassPane());
		wnd.setDropTarget(new DropTarget(wnd, dropTargetListener));
		wnd.addWindowFocusListener(wnd);
		updateAllTitles();

		app.updateMenubar();

		wnd.setVisible(true);

		// init some things in the background
		Thread runner = GeoGebraFrame.createAppThread(app);
		runner.start();

		checkCommandLineExport(app);

		for (NewInstanceListener l : instanceListener) {
			l.newInstance(wnd);
		}

		return wnd;
	}

	/**
	 * Returns the active GeoGebra window.
	 * 
	 * @return the active GeoGebra window.
	 */
	public static synchronized GeoGebraFrame getActiveInstance() {
		return activeInstance;
	}

	private static void setActiveInstance(GeoGebraFrame frame) {
		synchronized (lock) {
			activeInstance = frame;
		}
	}

	private static void addNewInstanceListener(NewInstanceListener l) {
		instanceListener.add(l);
	}

	/**
	 * return the application running geogebra
	 * 
	 * @param args command line args
	 * @param frame frame
	 * @return the application running geogebra
	 */
	protected AppD createApplication(CommandLineArguments args, JFrame frame) {
		return new AppD(args, frame, true);
	}
	
	public void init(AppD app) {
		this.app = app;
		getContentPane().add(app.buildApplicationPanel());
		dropTargetListener = new FileDropTargetListener(app);
		setGlassPane(((GuiManagerD) app.getGuiManager()).getLayout()
				.getDockManager().getGlassPane());
		setDropTarget(new DropTarget(this, dropTargetListener));
		addWindowFocusListener(this);
		updateAllTitles();
		// init some things in the background
		Thread runner = GeoGebraFrame.createAppThread(app);
		runner.start();

		for (NewInstanceListener l : instanceListener) {
			l.newInstance(this);
		}
	}

	protected GeoGebraFrame copy() {
		return new GeoGebraFrame();
	}

	private static AppThread createAppThread(AppD app) {
		return new AppThread(app);
	}

	private static class AppThread extends Thread {

		AppD app;

		public AppThread(AppD app) {
			this.app = app;
		}

		@Override
		public void run() {

			// init file chooser
			((DialogManagerD) this.app.getDialogManager())
					.initFileChooser();
		}
	}

	/**
	 * Converts a version string to a long value (e.g. 4.1.2.3 to 4001002003)
	 * 
	 * @param version
	 *            string
	 * @return long value
	 */

	static Long versionToLong(String version) {
		String[] subversions = version.split("\\.");
		Long n = 0L;
		int l = subversions.length;
		for (int i = 0; i < l; ++i) {
			String c = subversions[i];
			n = n * 1000 + Integer.parseInt(c);
		}
		return n;
	}

	public static int getInstanceCount() {
		return instances.size();
	}

	public static ArrayList<GeoGebraFrame> getInstances() {
		return instances;
	}

	static GeoGebraFrame getInstance(int i) {
		return instances.get(i);
	}

	private static void updateAllTitles() {
		for (int i = 0; i < instances.size(); i++) {
			if (instances.get(i) == null)
				continue;
			AppD app = instances.get(i).app;
			if (app != null)
				app.updateTitle();
		}
	}

	/**
	 * Checks all opened GeoGebra instances if their current file is the given
	 * file.
	 * 
	 * @param file ggb file
	 * @return GeoGebra instance with file open or null
	 */
	public static GeoGebraFrame getInstanceWithFile(File file) {
		if (file == null)
			return null;

		String absPath = null;
		try {
			absPath = file.getCanonicalPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (absPath == null)
			return null;

		for (GeoGebraFrame inst : instances) {
			AppD app = inst.app;
			if (app == null)
				continue;
			File currFile = app.getCurrentFile();
			if (currFile == null)
				continue;
			
			String currPath = null;
			try {
				currPath = currFile.getCanonicalPath();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (absPath.equals(currPath))
				return inst;
		}
		return null;
	}

	public boolean isIconified() {
		return getExtendedState() == Frame.ICONIFIED;
	}

	/**
	 * Returns the dropTarget listener for this frame.
	 * 
	 * @return the dropTarget listener for this frame.
	 */
	public FileDropTargetListener getDropTargetListener() {
		return dropTargetListener;
	}

	@Override
	public int print(Graphics g, PageFormat pf, int pageIndex)
			throws PrinterException {

		if (pageIndex > 0) {
			return NO_SUCH_PAGE;
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());

		double xScale = pf.getImageableWidth() / this.getWidth();
		double yScale = pf.getImageableHeight() / this.getHeight();
		double scale = Math.min(xScale, yScale);
		g2d.scale(scale, scale);

		this.printAll(g);

		return PAGE_EXISTS;

	}

	private static void checkCommandLineExport(final AppD app) {

		final CommandLineArguments args = null;

		if (args != null && args.containsArg("exportAnimation")
				&& args.containsArg("slider")) {

			String dpiStr = args.getStringValue("dpi");

			final int dpi = Integer
					.parseInt("".equals(dpiStr) ? "300" : dpiStr);

			final EuclidianView ev = app.getActiveEuclidianView();
			final String filename0 = args.getStringValue("exportAnimation");

			final String extension = StringUtil.getFileExtensionStr(filename0);

			final String filename = StringUtil.removeFileExtension(filename0);

			GeoElement export1 = app.getKernel()
					.lookupLabel(EuclidianView.EXPORT1);
			GeoElement export2 = app.getKernel()
					.lookupLabel(EuclidianView.EXPORT2);

			if ("gif".equals(extension) && export1 != null && export2 != null) {
				// maximize window
				// avoids clipping unless export size is especially large
				// needed for Animated GIF export from GeoGebraWeb
				// which runs this server-side
				Frame frame = app.getFrame();
				frame.setExtendedState(
						frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
			}

			SwingUtilities.invokeLater(() -> {

				String sliderName = args.getStringValue("slider");
				GeoElement slider = app.getKernel().lookupLabel(sliderName);

				if (slider == null || !slider.isGeoNumeric()
						|| !((GeoNumeric) slider).isSlider()) {
					Log.error(sliderName + " is not a slider");
					AppD.exit(0);
				}

				app.getKernel().getAnimatonManager().stopAnimation();
				GeoNumeric num = (GeoNumeric) slider;

				int type = num.getAnimationType();
				double min = num.getIntervalMin();
				double max = num.getIntervalMax();

				double val;

				double step;
				int n;

				switch (type) {
				case GeoElement.ANIMATION_DECREASING:
					step = -num.getAnimationStep();
					n = (int) ((max - min) / -step);
					if (DoubleUtil.isZero(((max - min) / -step) - n)) {
						n++;
					}
					if (n == 0) {
						n = 1;
					}
					val = max;
					break;
				case GeoElement.ANIMATION_OSCILLATING:
					step = num.getAnimationStep();
					n = (int) ((max - min) / step) * 2;
					if (DoubleUtil.isZero(((max - min) / step * 2) - n)) {
						n++;
					}
					if (n == 0) {
						n = 1;
					}
					val = min;
					break;
				default: // GeoElement.ANIMATION_INCREASING:
					// GeoElement.ANIMATION_INCREASING_ONCE:
					step = num.getAnimationStep();
					n = (int) ((max - min) / step);
					if (DoubleUtil.isZero(((max - min) / step) - n)) {
						n++;
					}
					if (n == 0) {
						n = 1;
					}
					val = min;
				}

				if ("gif".equals(extension)) {

					// "true" (default) or "false"
					String loop = args.getStringValue("loop");

					// time between frames in ms
					String delayStr = args.getStringValue("delay");

					final int delay = Integer.parseInt(
							"".equals(delayStr) ? "10" : delayStr);

					final AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
					gifEncoder.setQuality(1);
					gifEncoder.start(new File(filename + ".gif"));

					gifEncoder.setDelay(delay); // miliseconds
					if (!"false".equals(loop)) {
						// repeat forever
						gifEncoder.setRepeat(0);
					}

					FrameCollector collector = new FrameCollector() {

						@Override
						public void addFrame(BufferedImage img) {
							gifEncoder.addFrame(img);

						}

						@Override
						public void finish() {
							gifEncoder.finish();

						}
					};

					app.exportAnimatedGIF(ev, collector, num, n, val, min,
							max, step);

					Log.debug("animated GIF exported successfully");

					AppD.exit(0);
				}

				double printingScale = ev.getPrintingScale();
				double exportScale = (printingScale * dpi) / 2.54
						/ ev.getXscale();
				boolean textAsShapes = true;
				boolean transparent = true;
				boolean useEMFplus = true;

				int pixelWidth = (int) Math
						.floor(ev.getExportWidth() * exportScale);
				int pixelHeight = (int) Math
						.floor(ev.getExportHeight() * exportScale);

				for (int i = 0; i < n; i++) {

					Log.debug("exporting frame " + i + "of " + n);

					// avoid values like 14.399999999999968
					val = DoubleUtil.checkDecimalFraction(val);

					num.setValue(val);
					num.updateRepaint();

					File file = new File(filename + i + "." + extension);

					GraphicExportDialog.export(extension,
							(EuclidianViewInterfaceD) ev, file, transparent,
							dpi, exportScale, textAsShapes, useEMFplus,
							pixelWidth, pixelHeight, app);

					val += step;

					if (val > max + Kernel.STANDARD_PRECISION
							|| val < min - Kernel.STANDARD_PRECISION) {
						val -= 2 * step;
						step *= -1;
					}

				}

				AppD.exit(0);
			});

		}

		if (args != null && args.containsArg("export")) {
			final String filename = args.getStringValue("export");
			final String extension = StringUtil.getFileExtensionStr(filename);
			String dpiStr = args.getStringValue("dpi");

			final int dpi = Integer
					.parseInt("".equals(dpiStr) ? "300" : dpiStr);

			Log.debug("attempting to export: " + filename + " at " + dpiStr
					+ "dpi");

			// wait for EuclidianView etc to initialize before export
			SwingUtilities.invokeLater(() -> {

				EuclidianViewInterfaceD ev = (EuclidianViewInterfaceD) app
						.getActiveEuclidianView();
				try {

					boolean export3D = false;

					// if 3D view exists, assume that we should export
					// that
					// (only PNG supported right now for 3D)
					if (app.isEuclidianView3Dinited()) {

						if ("png".equals(extension)) {
							Log.debug("exporting 3D View");
							ev = (EuclidianView3DD) app
									.getEuclidianView3D();

							export3D = true;
						}
					}

					double printingScale = ev.getPrintingScale();
					double exportScale = (printingScale * dpi) / 2.54
							/ ev.getXscale();
					final boolean transparent = true;
					final boolean textAsShapes = true;
					final boolean useEMFplus = true;
					int pixelWidth = (int) Math
							.floor(ev.getExportWidth() * exportScale);
					int pixelHeight = (int) Math
							.floor(ev.getExportHeight() * exportScale);

					int dpi2 = dpi;

					String maxSizeStr = args.getStringValue("maxSize");

					if (maxSizeStr != null && !"".equals(maxSizeStr)) {

						// ************************
						double maxSize = Integer.parseInt(maxSizeStr);
						Log.debug("desiredSize = " + maxSize);
						double size = Math.max(ev.getExportWidth(),
								ev.getExportHeight());
						Log.debug("size = " + size);

						exportScale = Math.min(
								maxSize / Math.floor(ev.getExportWidth()),
								maxSize / Math.floor(ev.getExportHeight()));
						Log.debug("exportScale = " + exportScale);
						pixelWidth = (int) Math
								.floor(ev.getExportWidth() * exportScale);
						Log.debug("pixelWidth = " + pixelWidth);
						pixelHeight = (int) Math
								.floor(ev.getExportHeight() * exportScale);
						Log.debug("pixelHeight = " + pixelHeight);

						dpi2 = (int) (exportScale * ev.getXscale() * 2.54
								/ printingScale);
						Log.debug("dpi2 = " + dpi2);
					}

					final File file = new File(filename);

					GraphicExportDialog.export(extension, ev, file,
							transparent, dpi2, exportScale, textAsShapes,
							useEMFplus, pixelWidth, pixelHeight, app);

					// HACK
					// do it again for 3D, first call initializes JOGL
					if (export3D) {
						GraphicExportDialog.export(extension, ev, file,
								transparent, dpi2, exportScale,
								textAsShapes, useEMFplus, pixelWidth,
								pixelHeight, app);
					}

					Log.debug("Graphics View exported successfully to "
							+ file.getAbsolutePath());

				} catch (Throwable t) {
					t.printStackTrace();
				}
				AppD.exit(0);
			});

		}
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// we only care about resize
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// we only care about resize
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// we only care about resize
	}

	/**
	 * If app exists, call listener now. Otherwise, wait until app is
	 * created and call listener after.
	 *
	 * @param listener instance listener
	 */
	public static void doWithActiveInstance(NewInstanceListener listener) {
		if (activeInstance == null || activeInstance.getApplication() == null) {
			addNewInstanceListener(listener);
		} else {
			listener.newInstance(activeInstance);
		}
	}

}