package com.semenov.web.gwt.country.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class CountryEditor implements EntryPoint {

	@Override
	public void onModuleLoad() {
		RootPanel.get().add(new Label("GWT Hello World"));
	}

}
