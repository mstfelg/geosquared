package org.geogebra.common.euclidian.draw;

import org.geogebra.common.awt.GDimension;
import org.geogebra.common.awt.GFont;
import org.geogebra.common.awt.GGraphics2D;
import org.geogebra.common.awt.GRectangle;
import org.geogebra.common.awt.GRectangle2D;
import org.geogebra.common.factories.AwtFactory;
import org.geogebra.common.kernel.geos.GeoInputBox;

import com.google.j2objc.annotations.Weak;

/**
 * Renders LaTeX as text for the editor.
 */
public class LaTeXTextRenderer implements TextRenderer {

	// This margin is to match the height of the editor
	private static final int BOTTOM_OFFSET = 10;
	public static final int MARGIN = 4;
	private static final int CLIP_PADDING = 8;

	@Weak
	private DrawInputBox drawInputBox;

	LaTeXTextRenderer(DrawInputBox drawInputBox) {
		this.drawInputBox = drawInputBox;
	}

	@Override
	public void drawText(GeoInputBox geo, GGraphics2D graphics,
						 GFont font, String text,
						 double xPos, double yPos) {
		int textLeft = (int) Math.round(xPos);

		GDimension textDimension = drawInputBox.measureLatex(graphics, geo, font, text);
		int inputBoxHeight = calculateInputBoxHeight(textDimension);
		double diffToCenter = (inputBoxHeight - textDimension.getHeight()) / 2.0;
		int textTop = (int) Math.round(yPos + diffToCenter);
		GRectangle2D rect = AwtFactory.getPrototype().newRectangle2D();
		int clipWidth = drawInputBox.boxWidth - CLIP_PADDING;
		if (textDimension.getWidth() > clipWidth) {
			// if the text does not fit, reduce the margin a little
			clipWidth = drawInputBox.boxWidth - DrawInputBox.TF_PADDING_HORIZONTAL;
			textLeft -= DrawInputBox.TF_PADDING_HORIZONTAL;
		}
		rect.setRect(textLeft, 0, clipWidth, drawInputBox.getView().getHeight());
		graphics.setClip(rect);
		drawInputBox.drawLatex(graphics, geo, font, text, textLeft, textTop, true);
		graphics.resetClip();
	}

	private int calculateInputBoxHeight(GDimension textDimension) {
		int textHeightWithMargin = textDimension.getHeight() + BOTTOM_OFFSET + MARGIN;
		return Math.max(textHeightWithMargin, DrawInputBox.SYMBOLIC_MIN_HEIGHT);
	}

	@Override
	public GRectangle measureBounds(GGraphics2D graphics, GeoInputBox geo, GFont font,
									String labelDescription) {
		GDimension textDimension =
				drawInputBox.measureLatex(graphics, geo, font, geo.getDisplayText());

		int inputBoxHeight = calculateInputBoxHeight(textDimension);
		double labelHeight = drawInputBox.getHeightForLabel(labelDescription);
		double inputBoxTop = drawInputBox.getLabelTop() + ((labelHeight - inputBoxHeight) / 2);

		return AwtFactory.getPrototype().newRectangle(
				drawInputBox.boxLeft,
				(int) Math.round(inputBoxTop),
				drawInputBox.boxWidth,
				inputBoxHeight);
	}
}
