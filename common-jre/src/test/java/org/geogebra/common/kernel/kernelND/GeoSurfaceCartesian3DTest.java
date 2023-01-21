package org.geogebra.common.kernel.kernelND;

import org.geogebra.common.AppCommonFactory;
import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.geogebra3D.kernel3D.geos.GeoSurfaceCartesian3D;
import org.geogebra.common.jre.headless.AppCommon;
import org.junit.Before;
import org.junit.Test;

public class GeoSurfaceCartesian3DTest extends BaseUnitTest {

	@Before
	public void setUp() {
		getApp().set3dConfig();
	}

	@Override
	public AppCommon createAppCommon() {
		return AppCommonFactory.create3D();
	}

	@Test
	public void testGeoSurfaceCartesianNDHasTwoFunctions() {
		getApp().set3dConfig();
		addAvInput("f(a,b)=(a+b,a-b)");
		GeoSurfaceCartesian3D g = addAvInput("g(u,v)=f(u,v)+(0,0,1)");
		g.setDerivatives();
	}
}
