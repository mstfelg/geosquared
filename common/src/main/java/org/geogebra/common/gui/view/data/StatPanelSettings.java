package org.geogebra.common.gui.view.data;

import org.geogebra.common.gui.view.data.DataVariable.GroupType;

/**
 * @author G. Sturr
 * 
 *         Settings for DataAnalysisView displays
 * 
 */
public class StatPanelSettings extends PlotSettings {

	public DataSource dataSource;

	// histogram types
	public static final int TYPE_COUNT = 0;
	public static final int TYPE_RELATIVE = 1;
	public static final int TYPE_NORMALIZED = 2;
	private int frequencyType = TYPE_COUNT;

	// histogram options
	private boolean isCumulative = false;
	private boolean useManualClasses = false;
	private boolean hasOverlayNormal = false;
	private boolean hasOverlayPolygon = false;
	private boolean showFrequencyTable = false;
	private boolean showHistogram = true;
	private boolean showScatterplotLine = false;
	private boolean showOutliers = true;

	private double classStart = 0;
	private double classWidth = 5;
	private int numClasses = 5;

	private boolean isLeftRule = true;

	// bar chart options
	private double barWidth = 0.5;
	private boolean isAutomaticBarWidth = true;

	// graph options
	private boolean isAutomaticWindow = true;

	private CoordMode coordMode = CoordMode.STANDTOSTAND;

	public enum CoordMode {
		STANDTOSTAND(0), LOGTOSTAND(1), STANDTOLOG(2), LOGTOLOG(3);
		private int mode;

		private CoordMode(int mode) {
			this.mode = mode;
		}

		public int mode() {
			return mode;
		}
	}

	// stemplot options
	private int stemAdjust = 0;

	// ==================================================
	// Getters/Setters
	// ==================================================

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public GroupType groupType() {
		return dataSource.getGroupType();
	}

	public boolean isNumericData() {
		return dataSource.isNumericData();
	}

	public boolean isPointList() {
		return dataSource.isPointData();
	}

	public boolean isUseManualClasses() {
		return useManualClasses;
	}

	public void setUseManualClasses(boolean useManualClasses) {
		this.useManualClasses = useManualClasses;
	}

	public boolean isCumulative() {
		return isCumulative;
	}

	public void setCumulative(boolean isCumulative) {
		this.isCumulative = isCumulative;
	}

	public int getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(int frequencyType) {
		this.frequencyType = frequencyType;
	}

	public boolean isHasOverlayNormal() {
		return hasOverlayNormal;
	}

	public void setHasOverlayNormal(boolean hasOverlayNormal) {
		this.hasOverlayNormal = hasOverlayNormal;
	}

	public boolean isHasOverlayPolygon() {
		return hasOverlayPolygon;
	}

	public void setHasOverlayPolygon(boolean hasOverlayPolygon) {
		this.hasOverlayPolygon = hasOverlayPolygon;
	}

	public boolean isAutomaticWindow() {
		return isAutomaticWindow;
	}

	public void setAutomaticWindow(boolean isAutomaticWindow) {
		this.isAutomaticWindow = isAutomaticWindow;
	}

	public boolean isShowFrequencyTable() {
		return showFrequencyTable;
	}

	public void setShowFrequencyTable(boolean showFrequencyTable) {
		this.showFrequencyTable = showFrequencyTable;
	}

	public boolean isShowHistogram() {
		return showHistogram;
	}

	public void setShowHistogram(boolean showHistogram) {
		this.showHistogram = showHistogram;
	}

	public boolean isLeftRule() {
		return isLeftRule;
	}

	public void setLeftRule(boolean isLeftRule) {
		this.isLeftRule = isLeftRule;
	}

	public boolean isShowScatterplotLine() {
		return showScatterplotLine;
	}

	public void setShowScatterplotLine(boolean showScatterplotLine) {
		this.showScatterplotLine = showScatterplotLine;
	}

	public boolean isShowOutliers() {
		return showOutliers;
	}

	public void setShowOutliers(boolean showOutliers) {
		this.showOutliers = showOutliers;
	}

	public boolean isAutomaticBarWidth() {
		return isAutomaticBarWidth;
	}

	public void setAutomaticBarWidth(boolean isAutomaticBarWidth) {
		this.isAutomaticBarWidth = isAutomaticBarWidth;
	}

	public double getBarWidth() {
		return barWidth;
	}

	public void setBarWidth(double barWidth) {
		this.barWidth = barWidth;
	}

	public int getNumClasses() {
		return numClasses;
	}

	public void setNumClasses(int numClasses) {
		this.numClasses = numClasses;
	}

	public double getClassStart() {
		return classStart;
	}

	public void setClassStart(double classStart) {
		this.classStart = classStart;
	}

	public double getClassWidth() {
		return classWidth;
	}

	public void setClassWidth(double classWidth) {
		this.classWidth = classWidth;
	}

	public int getStemAdjust() {
		return stemAdjust;
	}

	public void setStemAdjust(int stemAdjust) {
		this.stemAdjust = stemAdjust;
	}

	/**
	 * @param coordMode
	 *            coordinates mode (log / standard)
	 */
	public void setCoordMode(CoordMode coordMode) {
		this.coordMode = coordMode;
		switch (coordMode) {
		case STANDTOSTAND:
			this.logXAxis = false;
			this.logYAxis = false;
			break;
		case LOGTOSTAND:
			this.logXAxis = true;
			this.logYAxis = false;
			break;
		case STANDTOLOG:
			this.logXAxis = false;
			this.logYAxis = true;
			break;
		case LOGTOLOG:
			this.logXAxis = true;
			this.logYAxis = true;
			break;
		}
	}

	public CoordMode getCoordMode() {
		return coordMode;
	}

}
