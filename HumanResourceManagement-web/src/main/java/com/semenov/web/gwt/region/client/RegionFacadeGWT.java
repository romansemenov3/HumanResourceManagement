package com.semenov.web.gwt.region.client;

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
 * <code>RegionGWT</code> JSON facade
 * 
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
public class RegionFacadeGWT {
	
	private static final String JSON_URL = "region/json";
	
	private static final String EDIT_URL = "region/edit";
	private static final String DELETE_URL = "region/delete";
	private static final String ADD_URL = "region/add";
	
	/**
	 * Region representation for JSON
	 */
	private static class RegionRecord extends JavaScriptObject 
	{
		protected RegionRecord(){}
		
		/**
		 * @return Region ID number
		 */
		public final native String getID() /*-{ return this.id }-*/;
		/**
		 * @return Region name
		 */
		public final native String getName() /*-{ return this.name }-*/;
	};
	
	
	private static class PagesRecord extends JavaScriptObject 
	{
		protected PagesRecord(){}
		
		public final native String getPages() /*-{ return this.pages }-*/;
	}
	
	/**
	 * Loads page of regions
	 * @param pageLength - page length
	 * @param pageNumber - page number
	 * @param target - target table
	 */
	public static void load(String country_id, int pageLength, int pageNumber, final CellTable<RegionGWT> target)
	{
		String url = RegionFacadeGWT.JSON_URL + "?country_id=" + country_id + "&length=" + String.valueOf(pageLength) + "&page=" + String.valueOf(pageNumber);
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new RequestCallback() 
				{
					@Override
					public void onResponseReceived(Request request,	Response response) {						
						final List<RegionGWT> result = new ArrayList<RegionGWT>();
						
						JsArray<RegionRecord> source = JsonUtils.<JsArray<RegionRecord>>safeEval(response.getText());
						
						for(int i = 0; i < source.length(); ++i)
							result.add(new RegionGWT(String.valueOf(source.get(i).getID()), String.valueOf(source.get(i).getName())));
						
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
	public static void add(final String country_id, final int pageLength, final int pageNumber, final CellTable<RegionGWT> target) throws RequestException
	{		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, RegionFacadeGWT.ADD_URL + "?country_id=" + country_id);
		
		builder.sendRequest(null, new RequestCallback() 
			{
				@Override
				public void onResponseReceived(Request request,	Response response) {
					RegionFacadeGWT.load(country_id, pageLength, pageNumber, target);
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
	 * @param object - region to delete
	 * @param target - target table
	 * @throws RequestException
	 */
	public static void delete(final String country_id, final int pageLength, final int pageNumber, RegionGWT object, final CellTable<RegionGWT> target) throws RequestException
	{		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, RegionFacadeGWT.DELETE_URL);
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");
		
		builder.sendRequest(object.getDeleteData(), new RequestCallback() {

			public void onResponseReceived(Request request, Response response) {
				RegionFacadeGWT.load(country_id, pageLength, pageNumber, target);
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
	 * @param object - region to edit
	 * @param target - target table
	 * @throws RequestException
	 */
	public static void edit(final String country_id, final int pageLength, final int pageNumber, RegionGWT object, final CellTable<RegionGWT> target) throws RequestException
	{		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, RegionFacadeGWT.EDIT_URL);
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");
		
		builder.sendRequest(object.getEditData(), new RequestCallback() {

			public void onResponseReceived(Request request, Response response) {
				RegionFacadeGWT.load(country_id, pageLength, pageNumber, target);
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
	public static void updatePageList(String country_id, int length, final ListBox target)
	{		
		String url = RegionFacadeGWT.JSON_URL + "?country_id=" + country_id + "&length=" + String.valueOf(length);
		
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
	
	public static void fillRegionsList(final String country_id, final ListBox target, final String choose)
	{
		String url = RegionFacadeGWT.JSON_URL + "?country_id=" + country_id;
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new RequestCallback() 
				{
					@Override
					public void onResponseReceived(Request request,	Response response) {
						
						JsArray<RegionRecord> source = JsonUtils.<JsArray<RegionRecord>>safeEval(response.getText());
						
						target.clear();
						target.addItem("Choose region", "-1");
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
