package com.semenov.web.gwt.staff.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.semenov.web.gwt.country.client.CountryFacadeGWT;
import com.semenov.web.gwt.office.client.OfficeFacadeGWT;
import com.semenov.web.gwt.region.client.RegionFacadeGWT;

/**
 * <code>StaffGWT</code> JSON facade
 * 
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
public class StaffFacadeGWT {
	
	private static final String JSON_URL = "json";
	private static final String EDIT_URL = "edit";
	private static final String DELETE_URL = "delete";
	private static final String ADD_URL = "add";
	
	/**
	 * Staff representation for JSON
	 */
	private static class StaffRecord extends JavaScriptObject 
	{
		protected StaffRecord(){}
		
		/**
		 * @return Staff ID number
		 */
		public final native String getID() /*-{ return this.id }-*/;
		/**
		 * @return Staff firstName
		 */
		public final native String getFirstName() /*-{ return this.firstName }-*/;
		/**
		 * @return Staff secondName
		 */
		public final native String getSecondName() /*-{ return this.secondName }-*/;
	};
	
	/**
	 * Extended Staff representation for JSON
	 */
	private static class StaffRecordExtended extends JavaScriptObject 
	{
		protected StaffRecordExtended(){}
		
		/**
		 * @return Staff ID number
		 */
		public final native String getID() /*-{ return this.id }-*/;
		/**
		 * @return Staff firstName
		 */
		public final native String getFirstName() /*-{ return this.firstName }-*/;
		/**
		 * @return Staff secondName
		 */
		public final native String getSecondName() /*-{ return this.secondName }-*/;
		/**
		 * @return Staff countryId
		 */
		public final native String getCountryId() /*-{ return this.countryId }-*/;
		/**
		 * @return Staff regionId
		 */
		public final native String getRegionId() /*-{ return this.regionId }-*/;
		/**
		 * @return Staff officeId
		 */
		public final native String getOfficeId() /*-{ return this.officeId }-*/;
	};
	
	
	private static class PagesRecord extends JavaScriptObject 
	{
		protected PagesRecord(){}
		
		public final native String getPages() /*-{ return this.pages }-*/;
	}
	
	/**
	 * Loads page of employees
	 * @param pageLength - page length
	 * @param pageNumber - page number
	 * @param target - target table
	 */
	public static void load(final String office_id, int pageLength, int pageNumber, final CellTable<StaffGWT> target)
	{
		String url = StaffFacadeGWT.JSON_URL + "?office_id=" + office_id + "&length=" + String.valueOf(pageLength) + "&page=" + String.valueOf(pageNumber);
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new RequestCallback() 
				{
					@Override
					public void onResponseReceived(Request request,	Response response) {						
						final List<StaffGWT> result = new ArrayList<StaffGWT>();
						
						JsArray<StaffRecord> source = JsonUtils.<JsArray<StaffRecord>>safeEval(response.getText());
						
						for(int i = 0; i < source.length(); ++i)
							result.add(new StaffGWT(String.valueOf(source.get(i).getID()),
									                String.valueOf(source.get(i).getFirstName()),
									                String.valueOf(source.get(i).getSecondName()),
									                office_id));
						
						target.setRowCount(result.size());
						target.setRowData(result);
					}

					@Override
					public void onError(Request request, Throwable exception) {
						RootPanel.get().add(new Label("RequestError: " + exception.getMessage()));
					}
				});
		} catch (RequestException e) {
			RootPanel.get().add(new Label("RequestError: " + e.getMessage()));
		}	
	}
	
	/**
	 * Adds employee and reloads data
	 * @param pageLength - page length
	 * @param pageNumber - page number
	 * @param target - target table
	 * @throws RequestException
	 */
	public static void add(final String office_id, final int pageLength, final int pageNumber, final CellTable<StaffGWT> target) throws RequestException
	{		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, StaffFacadeGWT.ADD_URL + "?region_id=" + office_id);
		
		builder.sendRequest(null, new RequestCallback() 
			{
				@Override
				public void onResponseReceived(Request request,	Response response) {
					StaffFacadeGWT.load(office_id, pageLength, pageNumber, target);
				}

				@Override
				public void onError(Request request, Throwable exception) {
					RootPanel.get().add(new Label("RequestError: " + exception.getMessage()));
				}
			});
	}
	
	/**
	 * Deletes employee and reloads data
	 * @param office_id - office ID
	 * @param pageLength - page length
	 * @param pageNumber - page number
	 * @param object - employee to delete
	 * @param target - target table
	 * @throws RequestException
	 */
	public static void delete(final String office_id, final int pageLength, final int pageNumber, StaffGWT object, final CellTable<StaffGWT> target) throws RequestException
	{		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, StaffFacadeGWT.DELETE_URL);
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");
		
		builder.sendRequest(object.getDeleteData(), new RequestCallback() {

			public void onResponseReceived(Request request, Response response) {
				StaffFacadeGWT.load(office_id, pageLength, pageNumber, target);
            }
			
			public void onError(Request request, Throwable exception) {
            	RootPanel.get().add(new Label("RequestError: " + exception.getMessage()));
            }           
        });
	}
	
	/**
	 * Edits employee and reloads data
	 * @param pageLength - page length
	 * @param pageNumber - page number
	 * @param object - employee to edit
	 * @param target - target table
	 * @throws RequestException
	 */
	public static void edit(final String office_id, final int pageLength, final int pageNumber, StaffGWT object, final CellTable<StaffGWT> target) throws RequestException
	{		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, StaffFacadeGWT.EDIT_URL);
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");
		
		builder.sendRequest(object.getEditData(), new RequestCallback() {

			public void onResponseReceived(Request request, Response response) {
				StaffFacadeGWT.load(office_id, pageLength, pageNumber, target);
            }
			
			public void onError(Request request, Throwable exception) {
            	RootPanel.get().add(new Label("RequestError: " + exception.getMessage()));
            }           
        });
	}
	
	/**
	 * Updates page number ListBox
	 * @param length - page length
	 * @param target - target ListBox
	 */
	public static void updatePageList(String office_id, int length, final ListBox target)
	{		
		String url = StaffFacadeGWT.JSON_URL + "?office_id=" + office_id + "&length=" + String.valueOf(length);
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new RequestCallback() 
				{
					@Override
					public void onResponseReceived(Request request,	Response response) {
						
						int pageNumber = Integer.parseInt(JsonUtils.<PagesRecord>safeEval(response.getText()).getPages());
						
						target.clear();											
						for(int i = 0; i < pageNumber; ++i)
						{
							target.addItem(String.valueOf(i + 1), String.valueOf(i));
						}
					}

					@Override
					public void onError(Request request, Throwable exception) {
						RootPanel.get().add(new Label("RequestError: " + exception.getMessage()));
					}
				});
		} catch (RequestException e) {
			RootPanel.get().add(new Label("UpdatePageListRequestError: " + e.getMessage()));
		}
	}
	
	public static void fillEditorLists(String id_extended, final ListBox countries, final ListBox regions, final ListBox offices)
	{
		String url = StaffFacadeGWT.JSON_URL + "?id_extended=" + id_extended;
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new RequestCallback() 
				{
					@Override
					public void onResponseReceived(Request request,	Response response) {						
						StaffRecordExtended source = JsonUtils.<StaffRecordExtended>safeEval(response.getText());
						
						CountryFacadeGWT.fillCountriesList(countries, String.valueOf(source.getCountryId()));
						RegionFacadeGWT.fillRegionsList(String.valueOf(source.getCountryId()), regions, String.valueOf(source.getRegionId()));
						OfficeFacadeGWT.fillOfficesList(String.valueOf(source.getRegionId()), offices, String.valueOf(source.getOfficeId()));
					}

					@Override
					public void onError(Request request, Throwable exception) {
						RootPanel.get().add(new Label("RequestError: " + exception.getMessage()));
					}
				});
		} catch (RequestException e) {
			RootPanel.get().add(new Label("UpdatePageListRequestError: " + e.getMessage()));
		}
	}
}
