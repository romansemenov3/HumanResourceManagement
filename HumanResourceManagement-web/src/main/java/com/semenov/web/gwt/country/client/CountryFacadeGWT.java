package com.semenov.web.gwt.country.client;

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

/**
 * <code>CountryGWT</code> JSON facade
 * 
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
public class CountryFacadeGWT {
	
	private static final String JSON_URL = "json";
	private static final String EXTERNAL_JSON_URL = "../country/json";
	
	private static final String EDIT_URL = "edit";
	private static final String DELETE_URL = "delete";
	private static final String ADD_URL = "add";
	
	/**
	 * Country representation for JSON
	 */
	private static class CountryRecord extends JavaScriptObject 
	{
		protected CountryRecord(){}
		
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
	public static void load(int pageLength, int pageNumber, final CellTable<CountryGWT> target)
	{
		String url = CountryFacadeGWT.JSON_URL + "?length=" + String.valueOf(pageLength) + "&page=" + String.valueOf(pageNumber);
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new RequestCallback() 
				{
					@Override
					public void onResponseReceived(Request request,	Response response) {						
						final List<CountryGWT> result = new ArrayList<CountryGWT>();
						
						JsArray<CountryRecord> source = JsonUtils.<JsArray<CountryRecord>>safeEval(response.getText());
						
						for(int i = 0; i < source.length(); ++i)
							result.add(new CountryGWT(String.valueOf(source.get(i).getID()), String.valueOf(source.get(i).getName())));
						
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
	 * Adds country and reloads data
	 * @param pageLength - page length
	 * @param pageNumber - page number
	 * @param target - target table
	 * @throws RequestException
	 */
	public static void add(final int pageLength, final int pageNumber, final CellTable<CountryGWT> target) throws RequestException
	{		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, CountryFacadeGWT.ADD_URL);
		
		builder.sendRequest(null, new RequestCallback() 
			{
				@Override
				public void onResponseReceived(Request request,	Response response) {
					CountryFacadeGWT.load(pageLength, pageNumber, target);
				}

				@Override
				public void onError(Request request, Throwable exception) {
					RootPanel.get().add(new Label("RequestError: " + exception.getMessage()));
				}
			});
	}
	
	/**
	 * Deletes country and reloads data
	 * @param pageLength - page length
	 * @param pageNumber - page number
	 * @param object - country to delete
	 * @param target - target table
	 * @throws RequestException
	 */
	public static void delete(final int pageLength, final int pageNumber, CountryGWT object, final CellTable<CountryGWT> target) throws RequestException
	{		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, CountryFacadeGWT.DELETE_URL);
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");
		
		builder.sendRequest(object.getDeleteData(), new RequestCallback() {

			public void onResponseReceived(Request request, Response response) {
				CountryFacadeGWT.load(pageLength, pageNumber, target);
            }
			
			public void onError(Request request, Throwable exception) {
            	RootPanel.get().add(new Label("RequestError: " + exception.getMessage()));
            }           
        });
	}
	
	/**
	 * Edits country and reloads data
	 * @param pageLength - page length
	 * @param pageNumber - page number
	 * @param object - country to edit
	 * @param target - target table
	 * @throws RequestException
	 */
	public static void edit(final int pageLength, final int pageNumber, CountryGWT object, final CellTable<CountryGWT> target) throws RequestException
	{		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, CountryFacadeGWT.EDIT_URL);
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");
		
		builder.sendRequest(object.getEditData(), new RequestCallback() {

			public void onResponseReceived(Request request, Response response) {
				CountryFacadeGWT.load(pageLength, pageNumber, target);
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
	public static void updatePageList(int length, final ListBox target)
	{		
		String url = CountryFacadeGWT.JSON_URL + "?length=" + String.valueOf(length);
		
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
	
	public static void fillCountriesList(final ListBox target, final String choose)
	{
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, CountryFacadeGWT.EXTERNAL_JSON_URL);
		try {
			builder.sendRequest(null, new RequestCallback() 
				{
					@Override
					public void onResponseReceived(Request request,	Response response) {
						
						JsArray<CountryRecord> source = JsonUtils.<JsArray<CountryRecord>>safeEval(response.getText());
						
						target.clear();
						target.addItem("Choose country", "-1");
						target.setSelectedIndex(0);
						for(int i = 0; i < source.length(); ++i)
						{
							target.addItem(String.valueOf(source.get(i).getName()), String.valueOf(source.get(i).getID()));
							if(String.valueOf(source.get(i).getID()).equals(choose))
								target.setSelectedIndex(i + 1);
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
}
