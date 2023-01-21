package org.geogebra.common.gui.dialog.options.model;

import java.util.ArrayList;
import java.util.List;

import org.geogebra.common.main.App;

public class GroupModel extends OptionsModel {
	private List<OptionsModel> models = new ArrayList<>();
	private PropertyListener listener;

	public GroupModel(App app) {
		super(app);
		listener = new PropertyListener() {

			@Override
			public Object updatePanel(Object[] geos2) {
				boolean enabled = false;
				for (OptionsModel model : models) {
					enabled = model.updateMPanel(geos2) || enabled;
				}
				return enabled ? this : null;
			}
		};
	}

	@Override
	protected boolean isValidAt(int index) {
		for (OptionsModel model : models) {
			if (model.isValidAt(index)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void updateProperties() {
		for (OptionsModel model : models) {
			model.updateProperties();
		}

	}

	public void add(OptionsModel model) {
		models.add(model);
	}

	@Override
	public PropertyListener getListener() {
		return listener;
	}

}
