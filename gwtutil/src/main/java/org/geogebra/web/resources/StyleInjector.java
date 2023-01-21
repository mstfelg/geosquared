/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.geogebra.web.resources;

import java.util.ArrayList;
import java.util.List;

import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLLinkElement;
import elemental2.dom.HTMLStyleElement;

/**
 * Injects stylesheets to the parent document
 */
public class StyleInjector {

	public static final String CLASSNAME = "ggw_resource";

	private static final List<String> stylesInLoading = new ArrayList<>();
	private static final List<Runnable> onStylesReady = new ArrayList<>();
	private final String moduleBaseUrl;

	public StyleInjector(String moduleBaseURL) {
		this.moduleBaseUrl = devModeFix(moduleBaseURL);
	}

	/**
	 * @param baseUrl (relative or absolute) base url of css file
	 * @param name name of the css file, without extension
	 * @return this for chaining
	 */
	public StyleInjector inject(String baseUrl, String name) {
		// to avoid conflicts with other elements on the page with this id
		String prefixedName = "ggbstyle_" + name;
		if (DomGlobal.document.getElementById(prefixedName) == null) {
			HTMLLinkElement element
					= (HTMLLinkElement) DomGlobal.document.createElement("link");

			stylesInLoading.add(name);
			element.onload = (e) -> {
				stylesInLoading.remove(name);
				checkIfAllStylesLoaded();
			};

			element.className = CLASSNAME;
			element.id = prefixedName;
			element.rel = "stylesheet";
			element.type = "text/css";
			element.href = moduleBaseUrl + "../" + baseUrl + "/" + name + ".css";
			DomGlobal.document.head.appendChild(element);
		}
		return this;
	}

	/**
	 * when localhost:8888/dev is used as codebase, the styles are one level above
	 * @param moduleBaseURL codebase
	 * @return canonical codebase
	 */
	public static String devModeFix(String moduleBaseURL) {
		return moduleBaseURL.replace(":8888/dev", ":8888");
	}

	private static void checkIfAllStylesLoaded() {
		if (stylesInLoading.isEmpty()) {
			for (Runnable r : onStylesReady) {
				r.run();
			}
			onStylesReady.clear();
		}
	}

	/**
	 * @param style stylesheet content
	 * @return HTML style element
	 */
	public static HTMLStyleElement injectStyleSheet(String style) {
		HTMLStyleElement element
				= (HTMLStyleElement) DomGlobal.document.createElement("style");
		element.className = CLASSNAME;
		element.innerHTML = style;
		return element;
	}

	/**
	 * @param runnable callback to run after all styles are loaded
	 */
	public static void onStylesLoaded(Runnable runnable) {
		onStylesReady.add(runnable);
		checkIfAllStylesLoaded();
	}
}
