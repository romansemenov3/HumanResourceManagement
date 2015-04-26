package com.semenov.web.gwt.country.client;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.http.client.RequestException;

public class Countries implements EntryPoint {	
	
	private final CellTable<CountryGWT> countries = new CellTable<CountryGWT>();
	private int pageLength = 25;
	private int pageNumber = 0;
	
	/**
	 * Data editor popup panel
	 */
	private class CountryEditPanel extends PopupPanel
	{
		/**
		 * Table with editor components
		 */
		private class DataTable extends FlexTable
		{
			private Button save;
			private Button cancel;
			
			private TextBox name;
			
			public DataTable(final CountryGWT country)
			{
				name = new TextBox();
				name.setValue(country.getName());
				
				save = new Button("Save");
				save.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						try {
							country.setName(name.getValue());
							CountryFacadeGWT.edit(pageLength, pageNumber, country, countries);
							hide();
						} catch (RequestException e) {
							RootPanel.get().add(new Label("EditRequestError: " + e.getMessage()));
						}
					}
				});
				
				cancel = new Button("Cancel");
				cancel.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {					
						hide();
					}
				});
				
				Object[][] rowData = { 
					{new Label("Column Name"), new Label("Column Value")},
					{new Label("Country ID"), new Label(country.getID())},
					{new Label("Country Name"), name},
					{save, cancel},
				};
				
				for(int row = 0; row < rowData.length; ++row)
					for (int column = 0; column < 2; ++column)
						setWidget(row, column, (Widget) rowData[row][column]);
				
				this.getCellFormatter().setStylePrimaryName(0, 0, "popupTableHeader");
				this.getCellFormatter().setStylePrimaryName(0, 1, "popupTableHeader");

				int lastRow = rowData.length - 1;
				for(int row = 1; row < lastRow; ++row)
				{
					this.getCellFormatter().setStylePrimaryName(row, 0, "popupColumnName");
					this.getCellFormatter().setStylePrimaryName(row, 1, "popupColumnValue");
				}
						
				this.getCellFormatter().setStylePrimaryName(lastRow, 0, "popupColumnButton");
				this.getCellFormatter().setStylePrimaryName(lastRow, 1, "popupColumnButton");
				
				this.setStylePrimaryName("popupTable");
			}
		}
		
		public CountryEditPanel(final CountryGWT country)
		{
			super(false);
			this.center();
			this.add(new DataTable(country));
			this.setStylePrimaryName("popupDiv");
		}
	}
	
	private class HeaderTable extends FlexTable
	{
		private final Button addButton = new Button("Add Country");
		private final ListBox pageNumberListBox = new ListBox();
		private final ListBox pageLengthListBox = new ListBox();
		
		private void pageNumberListBoxChange()
		{
			pageNumber = Integer.parseInt(pageNumberListBox.getSelectedValue());
			CountryFacadeGWT.load(pageLength, pageNumber, countries);
		}
		
		private void pageLengthListBoxChange()
		{
			pageLength = Integer.parseInt(pageLengthListBox.getSelectedValue());
			CountryFacadeGWT.updatePageList(pageLength, pageNumberListBox);
			
			pageNumberListBox.setSelectedIndex(0);
			pageNumberListBoxChange();
		}
		
		public HeaderTable()
		{
			addButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {					
					try {
						pageLengthListBoxChange();
						pageNumberListBox.setSelectedIndex(pageNumberListBox.getItemCount() - 1);
						pageNumberListBoxChange();
						CountryFacadeGWT.add(pageLength, pageNumber, countries);
					} catch (RequestException e) {
						RootPanel.get().add(new Label("AddRequestError: " + e.getMessage()));
					}
				}
			});
			
			pageLengthListBox.addItem("10", "10");
			pageLengthListBox.addItem("25", "25");
			pageLengthListBox.addItem("50", "50");
			pageLengthListBox.addItem("100", "100");
			pageLengthListBox.addChangeHandler(new ChangeHandler(){
				@Override
				public void onChange(ChangeEvent event) {
					pageLengthListBoxChange();
				}				
			});
			
			pageNumberListBox.addChangeHandler(new ChangeHandler(){
				@Override
				public void onChange(ChangeEvent event) {
					pageNumberListBoxChange();
				}				
			});
			CountryFacadeGWT.updatePageList(pageLength, pageNumberListBox);
			
			setWidget(0, 0, addButton);
			setWidget(0, 1, pageLengthListBox);
			setWidget(0, 2, pageNumberListBox);
			
			this.setStylePrimaryName("headerTable");
		}
	}
	
	/**
	 * Creates table for countries
	 * @return - table for countries
	 * @throws RequestException
	 */
	private void buildTable() throws RequestException
	{		
		//"Country ID" row
		TextColumn<CountryGWT> id = new TextColumn<CountryGWT>() {
		    @Override
			    public String getValue(CountryGWT row) {
			    	return row.getID();
			    }
			};
		this.countries.addColumn(id, "Country ID");
		
		//"Country Name" row
		ClickableTextCell nameCell = new ClickableTextCell();
		Column<CountryGWT, String> name = new Column<CountryGWT, String>(nameCell) {
			@Override
			public String getValue(CountryGWT object) {
				return object.getName();
			}			
	    };
	    name.setFieldUpdater(new FieldUpdater<CountryGWT, String>() {
			@Override
			public void update(int index, CountryGWT object, String value) {
				Window.open("../region/regions.html?country_id=" + object.getID(), "_blank", "");
			}
	    });
	    this.countries.addColumn(name, "Country Name");
		
		//"Edit Country" row
		ButtonCell editCell = new ButtonCell();
		Column<CountryGWT, String> edit = new Column<CountryGWT, String>(editCell) {
			@Override
			public String getValue(CountryGWT object) {
				return "Edit " + object.getName();
			}			
	    };
	    edit.setFieldUpdater(new FieldUpdater<CountryGWT, String>() {
			@Override
			public void update(int index, CountryGWT object, String value) {					
				new CountryEditPanel(object).show();
			}
	    });
	    this.countries.addColumn(edit, "Edit Country");
		
		//"Delete Country" row
		ButtonCell deleteCell = new ButtonCell();
		Column<CountryGWT, String> delete = new Column<CountryGWT, String>(deleteCell) {
			@Override
			public String getValue(CountryGWT object) {
				return "Delete " + object.getName();
			}			
	    };
	    delete.setFieldUpdater(new FieldUpdater<CountryGWT, String>() {
			@Override
			public void update(int index, CountryGWT object, String value) {					
				try {
					CountryFacadeGWT.delete(pageLength, pageNumber, object, countries);
				} catch (RequestException e) {
					RootPanel.get().add(new Label("DeleteRequestError: " + e.getMessage()));
				}
			}
	    });
	    this.countries.addColumn(delete, "Delete Country");
	}	
	
	@Override
	public void onModuleLoad() {
		try {
			buildTable();
			CountryFacadeGWT.load(pageLength, pageNumber, this.countries);
			
			RootPanel.get().add(new HeaderTable());
			RootPanel.get().add(this.countries);
			
		} catch (RequestException e) {
			
		}
	}

}
