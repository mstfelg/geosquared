package org.geogebra.common.move.ggtapi.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.geogebra.common.main.Localization;
import org.geogebra.common.move.ggtapi.GroupIdentifier;
import org.geogebra.common.move.ggtapi.events.LoginEvent;
import org.geogebra.common.move.ggtapi.models.Material.MaterialType;
import org.geogebra.common.move.ggtapi.models.MaterialRequest.Order;
import org.geogebra.common.move.ggtapi.models.json.JSONArray;
import org.geogebra.common.move.ggtapi.models.json.JSONException;
import org.geogebra.common.move.ggtapi.models.json.JSONObject;
import org.geogebra.common.move.ggtapi.models.json.JSONTokener;
import org.geogebra.common.move.ggtapi.operations.BackendAPI;
import org.geogebra.common.move.ggtapi.operations.LogInOperation;
import org.geogebra.common.move.ggtapi.requests.MaterialCallbackI;
import org.geogebra.common.util.AsyncOperation;
import org.geogebra.common.util.HttpRequest;
import org.geogebra.common.util.StringUtil;
import org.geogebra.common.util.debug.Log;

/**
 * API connector for the MARVL restful API
 */
public class MaterialRestAPI implements BackendAPI {
	/** whether API is available */
	protected boolean available = true;
	/** whether availability check request was sent */
	private boolean availabilityCheckDone = false;
	private final String baseURL;
	private AuthenticationModel model;

	private final Service service;

	public static final String marvlUrl = "https://api.geogebra.org/v1.0";

	/**
	 * @param baseURL
	 *            URL of the API; endpoints append eg. "/materials" to it
	 */
	public MaterialRestAPI(String baseURL, Service service) {
		this.baseURL = baseURL;
		this.service = service;
	}

	@Override
	public void getItem(String id, MaterialCallbackI callback) {
		performRequest("GET", "/materials/" + id, null, callback);
	}

	@Override
	public boolean checkAvailable(LogInOperation logInOperation) {
		if (!availabilityCheckDone) {
			performCookieLogin(logInOperation);
		}
		return available;
	}

	@Override
	public String getLoginUrl() {
		return null;
	}

	@Override
	public boolean parseUserDataFromResponse(GeoGebraTubeUser guser, String response) {
		try {
			JSONTokener tokener = new JSONTokener(response);
			JSONObject user = new JSONObject(tokener).getJSONObject("user");
			guser.setUserName(user.getString("displayname"));
			guser.setUserId(user.getInt("id"));
			guser.setIdentifier("");
			guser.setStudent(!"1".equals(user.getString("isTeacher")));
			guser.setLanguage(user.optString("langUi"));
			ArrayList<GroupIdentifier> allGroups = new ArrayList<>();
			addGroups(user, "allClasses", allGroups, GroupIdentifier.GroupCategory.CLASS);
			addGroups(user, "allCourses", allGroups, GroupIdentifier.GroupCategory.COURSE);
			guser.setGroups(allGroups);
			return true;
		} catch (Exception e) {
			Log.warn(e.getMessage());
		}
		return false;
	}

	private void addGroups(JSONObject user, String allClasses,
			ArrayList<GroupIdentifier> allGroups, GroupIdentifier.GroupCategory cat)
			throws JSONException {
		if (user.has(allClasses)) {
			JSONArray classList = user.getJSONArray(allClasses);
			allGroups.addAll(stringList(classList, cat));
		}
	}

	/**
	 * @param classList
	 *            JSON array
	 * @return Java array
	 * @throws JSONException
	 *             if array contains objects other than strings
	 */
	private static ArrayList<GroupIdentifier> stringList(JSONArray classList,
			GroupIdentifier.GroupCategory category) throws JSONException {
		ArrayList<GroupIdentifier> groups = new ArrayList<>();
		for (int i = 0; i < classList.length(); i++) {
			groups.add(new GroupIdentifier(classList.getString(i), category));
		}
		return groups;
	}

	@Override
	public void deleteMaterial(final Material mat, final MaterialCallbackI callback) {

		HttpRequest request = service.createRequest(model);
		request.sendRequestPost("DELETE", baseURL + "/materials/" + mat.getSharingKeyOrId(), null,
				new AjaxCallback() {
					@Override
					public void onSuccess(String responseStr) {
						// we don't parse the response here
						ArrayList<Material> mats = new ArrayList<>();
						mats.add(mat);
						callback.onLoaded(mats, null);
					}

					@Override
					public void onError(String error) {
						callback.onError(new Exception(error));
					}
				});

	}

	@Override
	public final void authorizeUser(final GeoGebraTubeUser user, final LogInOperation op,
			final boolean automatic) {

		HttpRequest request = service.createRequest(model);
		request.sendRequestPost("GET", baseURL + "/auth", null, new AjaxCallback() {
			@Override
			public void onSuccess(String responseStr) {
				try {
					MaterialRestAPI.this.availabilityCheckDone = true;
					MaterialRestAPI.this.available = true;

					// Parse the userdata from the response
					if (!parseUserDataFromResponse(user, responseStr)) {
						op.onEvent(new LoginEvent(user, false, automatic, responseStr));
						return;
					}
					String auth = request.getResponseHeader("Authorization");
					user.setJWTToken(auth.replace("Bearer ", ""));
					op.onEvent(new LoginEvent(user, true, automatic, responseStr));
				} catch (Exception e) {
					Log.error(e.getMessage());
				}
			}

			@Override
			public void onError(String error) {
				Log.error(error);
				MaterialRestAPI.this.availabilityCheckDone = true;
				MaterialRestAPI.this.available = false;

				op.onEvent(new LoginEvent(user, false, automatic, null));
			}
		});
	}

	@Override
	public void setClient(ClientInfo client) {
		this.model = client.getModel();
	}

	@Override
	public boolean isCheckDone() {
		return this.availabilityCheckDone;
	}

	@Override
	public void setUserLanguage(String fontStr, String loginToken) {
		// not supported
	}

	@Override
	public void favorite(int id, boolean favorite) {
		// not supported
	}

	@Override
	public String getUrl() {
		return this.baseURL;
	}

	@Override
	public void logout(String token) {
		// open platform dependent popup
	}

	@Override
	public void uploadLocalMaterial(Material mat, MaterialCallbackI cb) {
		// offline materials not supported
	}

	@Override
	public boolean performCookieLogin(final LogInOperation op) {
		op.passiveLogin();
		return true;
	}

	@Override
	public void performTokenLogin(LogInOperation op, String token) {
		performCookieLogin(op);
	}

	@Override
	public void getUsersMaterials(MaterialCallbackI userMaterialsCB, MaterialRequest.Order order) {
		getUsersOwnMaterials(userMaterialsCB, order);
	}

	private ArrayList<Chapter> parseMaterialCount(String responseStr) throws JSONException {
		ArrayList<Chapter> ret = new ArrayList<>();
		int[] counts = new int[3];
		JSONTokener jst = new JSONTokener(responseStr);
		Object parsed = jst.nextValue();
		if (parsed instanceof JSONObject && ((JSONObject) parsed).has("from")) {
			counts[0] = ((JSONObject) parsed).getInt("from");
			counts[1] = (((JSONObject) parsed).getInt("to"));
			counts[2] = (((JSONObject) parsed).getInt("total"));
			ret.add(new Chapter(null, counts));
			return ret;
		}
		return null;
	}

	/**
	 * @param responseStr
	 *            JSON encoded material or list of materials
	 * @return list of materials
	 * @throws JSONException
	 *             when structure of JSON is invalid
	 */
	private List<Material> parseMaterials(String responseStr) throws JSONException {
		ArrayList<Material> ret = new ArrayList<>();
		JSONTokener jst = new JSONTokener(responseStr);
		Object parsed = jst.nextValue();
		if (parsed instanceof JSONObject) {
			if (((JSONObject) parsed).has("materials")) {
				JSONArray materials = ((JSONObject) parsed).getJSONArray("materials");
				for (int i = 0; i < materials.length(); i++) {
					Material mat = JSONParserGGT.prototype.toMaterial(materials.getJSONObject(i));
					ret.add(mat);
				}
			} else {
				Material mat = JSONParserGGT.prototype.toMaterial((JSONObject) parsed);
				ret.add(mat);
			}
		}
		return ret;
	}

	@Override
	public void getFeaturedMaterials(MaterialCallbackI userMaterialsCB) {
		// no public materials
		userMaterialsCB.onLoaded(new ArrayList<>(), null);
	}

	@Override
	public void getUsersOwnMaterials(final MaterialCallbackI userMaterialsCB,
			MaterialRequest.Order order) {
		if (model == null) {
			userMaterialsCB.onError(new Exception("No user signed in"));
			return;
		}

		performRequest("GET",
				"/users/" + model.getUserId()
						+ "/materials?limit=50&embed=creator&order="
						+ orderStr(order),
				null, userMaterialsCB);
	}

	@Override
	public void getUsersAndSharedMaterials(MaterialCallbackI allMaterialsCB, Order order,
			int offset) {
		if (model == null) {
			allMaterialsCB.onError(new Exception("No user signed in"));
			return;
		}

		performRequest("GET",
				"/users/" + model.getUserId()
						+ "/materials?format=page&type=all&limit=50&offset=" + offset
						+ "&embed=creator&order="
						+ orderStr(order),
				null, allMaterialsCB);
	}

	private static String orderStr(Order order) {
		switch (order) {
		case timestamp:
			return "-modified";
		case created:
			return "-" + order.name();
		case title:
		case privacy:
			return order.name();
		default:
			return "title";
		}
	}

	private void performRequest(final String method, String endpoint, String json,
			final MaterialCallbackI userMaterialsCB) {
		HttpRequest request = service.createRequest(model);
		request.setContentTypeJson();

		request.sendRequestPost(method, baseURL + endpoint, json, new AjaxCallback() {
			@Override
			public void onSuccess(String responseStr) {
				try {
					userMaterialsCB
							.onLoaded(parseMaterials(responseStr), parseMaterialCount(responseStr));
				} catch (Exception e) {
					userMaterialsCB.onError(e);
				}
			}

			@Override
			public void onError(String error) {
				userMaterialsCB.onError(new Exception(error));
			}
		});
	}

	@Override
	public void uploadMaterial(String tubeID, String visibility, String text, String base64,
			MaterialCallbackI materialCallback, MaterialType type, boolean isMultiuser) {
		JSONObject request = new JSONObject();
		try {
			request.put("visibility", visibility); // per docs "S" is the only
											// supported visibility
			request.put("title", text);
			request.put("file", base64);
			if (StringUtil.emptyOrZero(tubeID)) {
				request.put("type", type.toString());
			} else if (service.hasMultiuser()) {
				request.put("multiuser", isMultiuser);
			}
		} catch (JSONException e) {
			materialCallback.onError(e);
		}
		if (!StringUtil.emptyOrZero(tubeID)) {
			performRequest("PATCH", "/materials/" + tubeID, request.toString(),
					materialCallback);
		} else {
			performRequest("POST", "/materials", request.toString(), materialCallback);
		}
	}

	@Override
	public void uploadRenameMaterial(Material material, MaterialCallbackI materialCallback) {
		JSONObject request = new JSONObject();
		try {
			request.put("title", material.getTitle());
		} catch (JSONException e) {
			materialCallback.onError(e);
		}
		performRequest("PATCH", "/materials/" + material.getSharingKeyOrId(),
				request.toString(), materialCallback);
	}

	@Override
	public void copy(Material material, final String title,
			final MaterialCallbackI materialCallback) {
		performRequest("POST", "/materials/" + material.getSharingKeyOrId(), null,
				new MaterialCallbackI() {

					@Override
					public void onLoaded(List<Material> result, ArrayList<Chapter> meta) {
						if (result.size() == 1) {
							result.get(0).setTitle(title);
							uploadRenameMaterial(result.get(0), materialCallback);
						}
					}

					@Override
					public void onError(Throwable exception) {
						materialCallback.onError(exception);
					}
				});
	}

	/**
	 * @param localization
	 *            localization
	 * @param title
	 *            original title
	 * @return title with "Copy of" prefix or numeric suffix
	 */
	public static String getCopyTitle(Localization localization, String title) {
		if (title.matches(localization.getPlain("CopyOfA", ".*"))) {
			int i = 2;
			String stem = title;
			if (title.endsWith(")")) {
				String numeric = title.substring(title.lastIndexOf('(') + 1, title.length() - 1);
				try {
					i = Integer.parseInt(numeric) + 1;
					stem = title.substring(0, title.lastIndexOf('(') - 1);
				} catch (RuntimeException e) {
					// ignore
				}
			}
			return stem + " (" + i + ")";
		}
		return localization.getPlain("CopyOfA", title);
	}

	@Override
	public void setShared(Material m, GroupIdentifier groupID, boolean shared,
			final AsyncOperation<Boolean> callback) {
		HttpRequest request = service.createRequest(model);
		request.sendRequestPost(shared ? "POST" : "DELETE",
				baseURL + "/materials/" + m.getSharingKeyOrId() + "/groups/"
						+ groupID.name + "?category=" + groupID.getCategory(), null,
				new AjaxCallback() {
					@Override
					public void onSuccess(String responseStr) {
						callback.callback(true);
					}

					@Override
					public void onError(String error) {
						callback.callback(false);
					}
				});
	}

	@Override
	public void getGroups(String materialID, GroupIdentifier.GroupCategory category,
			AsyncOperation<List<GroupIdentifier>> callback) {
		HttpRequest request = service.createRequest(model);
		String path = "/materials/" + materialID + "/groups?type=isShared";
		if (category != null) {
			path += "&category=" + category.name().toLowerCase(Locale.ROOT);
		}
		request.sendRequestPost("GET",
				baseURL + path, null,
				new AjaxCallback() {
					@Override
					public void onSuccess(String responseStr) {
						JSONArray groups;
						try {
							groups = new JSONArray(new JSONTokener(responseStr));
							callback.callback(stringList(groups, category));
						} catch (JSONException e) {
							callback.callback(null);
						}

					}

					@Override
					public void onError(String error) {
						callback.callback(null);
					}
				});
	}

	@Override
	public boolean owns(Material mat) {
		return mat.getCreator().getId() <= 0
				|| mat.getCreator().getId() == model.getUserId();
	}

	@Override
	public boolean canUserShare(boolean student) {
		return !student;
	}

	@Override
	public boolean anonymousOpen() {
		return false;
	}

	@Override
	public void getTemplateMaterials(final MaterialCallbackI templateMaterialsCB) {
		if (model == null || !model.isLoggedIn()) {
			templateMaterialsCB.onLoaded(new ArrayList<>(), null);
			return;
		}

		performRequest("GET",
				"/users/" + model.getUserId()
						+ "/materials?format=page&filter="
						+ "ggs-template",
				null, templateMaterialsCB);
	}

	/**
	 * send the base64 of a h5p file
	 * @param base64 of the file
	 * @param callback to handle api response
	 */
	public void uploadAndUnzipH5P(String base64, AjaxCallback callback) {
		HttpRequest request = service.createRequest(model);
		request.setContentTypeJson();
		String json = "{\"file\":\"" + base64 + "\"}";
		request.sendRequestPost("POST", baseURL + "/media/h5p",
				json, callback);
	}
}
