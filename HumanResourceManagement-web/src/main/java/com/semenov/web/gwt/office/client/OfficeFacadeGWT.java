package com.semenov.web.gwt.office.client;

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
import com.google.gwt.http.client.URL;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;

public class OfficeFacadeGWT {
	
	private static final String JSON_URL = "json";
	private static final String EDIT_URL = "edit";
	private static final String DELETE_URL = "delete";
	private static final String ADD_URL = "add";
	
	/**
	 * Country representation for JSON
	 */
	private static class RegionRecord extends JavaScriptObject 
	{
		protected RegionRecord(){}
		
		/**
		 * @return Country ID number
		 */
		public final native String getID() /*-{ return this.id }-*/;
		/**
		 * @return Country name
		 */
		public final native String getName() /*-{ return this.name }-*/;
	};
	
	
	private static class PagesRecord extends JavaScriptObject 
	{
		protected PagesRecord(){}
		
		public final native String getPages() /*-{ return this.pages }-*/;
	}
	
	/**
	 * Loads page of countries
	 * @param pageLength - page length
	 * @param pageNumber - page number
	 * @param target - target table
	 */
	public static void load(String region_id, int pageLength, int pageNumber, final CellTable<OfficeGWT> target)
	{
		String url = OfficeFacadeGWT.JSON_URL + "?region_id=" + region_id + "&length=" + String.valueOf(pageLength) + "&page=" + String.valueOf(pageNumber);
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new RequestCallback() 
				{
					@Override
					public void onResponseReceived(Request request,	Response response) {						
						final List<OfficeGWT> result = new ArrayList<OfficeGWT>();
						
						JsArray<RegionRecord> source = JsonUtils.<JsArray<RegionRecord>>safeEval(response.getText());
						
						for(int i = 0; i < source.length(); ++i)
							result.add(new OfficeGWT(String.valueOf(source.get(i).getID()), String.valueOf(source.get(i).getName())));
						
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
	 * Adds region and reloads data
	 * @param pageLength - page length
	 * @param pageNumber - page number
	 * @param target - target table
	 * @throws RequestException
	 */
	public static void add(final String region_id, final int pageLength, final int pageNumber, final CellTable<OfficeGWT> target) throws RequestException
	{		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, OfficeFacadeGWT.ADD_URL + "?region_id=" + region_id);
		
		builder.sendRequest(null, new RequestCallback() 
			{
				@Override
				public void onResponseReceived(Request request,	Response response) {
					OfficeFacadeGWT.load(region_id, pageLength, pageNumber, target);
				}

				@Override
				public void onError(Request request, Throwable exception) {
					RootPanel.get().add(new Label("RequestError: " + exception.getMessage()));
				}
			});
	}
	
	/**
	 * Deletes region and reloads data
	 * @param pageLength - page length
	 * @param pageNumber - page number
	 * @param object - country to delete
	 * @param target - target table
	 * @throws RequestException
	 */
	public static void delete(final String region_id, final int pageLength, final int pageNumber, OfficeGWT object, final CellTable<OfficeGWT> target) throws RequestException
	{		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, OfficeFacadeGWT.DELETE_URL);
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");
		
		builder.sendRequest(object.getDeleteData(), new RequestCallback() {

			public void onResponseReceived(Request request, Response response) {
				OfficeFacadeGWT.load(region_id, pageLength, pageNumber, target);
            }
			
			public void onError(Request request, Throwable exception) {
            	RootPanel.get().add(new Label("RequestError: " + exception.getMessage()));
            }           
        });
	}
	
	/**
	 * Edits region and reloads data
	 * @param pageLength - page length
	 * @param pageNumber - page number
	 * @param object - country to edit
	 * @param target - target table
	 * @throws RequestException
	 */
	public static void edit(final String region_id, final int pageLength, final int pageNumber, OfficeGWT object, final CellTable<OfficeGWT> target) throws RequestException
	{		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, OfficeFacadeGWT.EDIT_URL);
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");
		
		builder.sendRequest(object.getEditData(), new RequestCallback() {

			public void onResponseReceived(Request request, Response response) {
				OfficeFacadeGWT.load(region_id, pageLength, pageNumber, target);
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
	public static int updatePageList(String region_id, int length, final ListBox target)
	{
		final Integer result = new Integer(0);
		
		String url = OfficeFacadeGWT.JSON_URL + "?region_id=" + region_id + "&length=" + String.valueOf(length);
		
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
		
		return result;
	}
}
