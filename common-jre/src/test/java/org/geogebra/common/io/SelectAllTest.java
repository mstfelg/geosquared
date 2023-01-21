package org.geogebra.common.io;

import org.geogebra.common.AppCommonFactory;
import org.geogebra.common.jre.headless.AppCommon;
import org.junit.BeforeClass;
import org.junit.Test;

import com.himamis.retex.editor.share.event.KeyEvent;
import com.himamis.retex.editor.share.util.JavaKeyCodes;
import com.himamis.retex.renderer.share.platform.FactoryProvider;

public class SelectAllTest {
	private static final AppCommon app = AppCommonFactory.create();

	/**
	 * Setup LaTeX
	 */
	@BeforeClass
	public static void prepare() {
		if (FactoryProvider.getInstance() == null) {
			FactoryProvider.setInstance(new FactoryProviderCommon());
		}
	}

	@Test
	public void testPointSimpleCoordinateX() {
		EditorChecker checker = new EditorChecker(app);
		checker.fromParser("(321.45,4)")
				.setModifiers(KeyEvent.CTRL_MASK)
				.protect()
				.left(8)
				.typeKey(JavaKeyCodes.VK_A)
				.type("0")
				.checkAsciiMath("(0,4)");
	}

	@Test
	public void testPointSimpleCoordinateXFromMiddle() {
		EditorChecker checker = new EditorChecker(app);
		checker.fromParser("(321.45,4)")
				.protect()
				.left(4)
				.setModifiers(KeyEvent.CTRL_MASK)
				.typeKey(JavaKeyCodes.VK_A)
				.type("0")
				.checkAsciiMath("(0,4)");
	}

	@Test
	public void listShouldSelectAllElements() {
		EditorChecker checker = new EditorChecker(app);
		checker.fromParser("{1,2,3}")
				.left(1)
				.setModifiers(KeyEvent.CTRL_MASK)
				.typeKey(JavaKeyCodes.VK_A)
				.type(" ")
				.checkAsciiMath(" ");
	}
}
