package com.semenov.web.gwt.region.client;

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

public class Regions implements EntryPoint {	
	
	private final CellTable<RegionGWT> regions = new CellTable<RegionGWT>();
	private int pageLength = 25;
	private int pageNumber = 0;
	
	private String country_id;
	
	/**
	 * Data editor popup panel
	 */
	private class RegionEditPanel extends PopupPanel
	{
		/**
		 * Table with editor components
		 */
		private class DataTable extends FlexTable
		{
			private Button save;
			private Button cancel;
			
			private TextBox name;
			
			public DataTable(final RegionGWT region)
			{
				name = new TextBox();
				name.setValue(region.getName());
				
				save = new Button("Save");
				save.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						try {
							region.setName(name.getValue());
							RegionFacadeGWT.edit(country_id, pageLength, pageNumber, region, regions);
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
					{new Label("Region ID"), new Label(region.getID())},
					{new Label("Region Name"), name},
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
		
		public RegionEditPanel(final RegionGWT region)
		{
			super(false);
			this.center();
			this.add(new DataTable(region));
			this.setStylePrimaryName("popupDiv");
		}
	}
	
	private class HeaderTable extends FlexTable
	{
		private final Button addButton = new Button("Add Region");
		private final ListBox pageNumberListBox = new ListBox();
		private final ListBox pageLengthListBox = new ListBox();
		
		private void pageNumberListBoxChange()
		{
			pageNumber = Integer.parseInt(pageNumberListBox.getSelectedValue());
			RegionFacadeGWT.load(country_id, pageLength, pageNumber, regions);
		}
		
		private void pageLengthListBoxChange()
		{
			pageLength = Integer.parseInt(pageLengthListBox.getSelectedValue());
			RegionFacadeGWT.updatePageList(country_id, pageLength, pageNumberListBox);
			
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
						RegionFacadeGWT.add(country_id, pageLength, pageNumber, regions);
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
			RegionFacadeGWT.updatePageList(country_id, pageLength, pageNumberListBox);
			
			setWidget(0, 0, addButton);
			setWidget(0, 1, pageLengthListBox);
			setWidget(0, 2, pageNumberListBox);
			
			this.setStylePrimaryName("headerTable");
		}
	}
	
	/**
	 * Creates table for regions
	 * @return - table for regions
	 * @throws RequestException
	 */
	private void buildTable() throws RequestException
	{		
		//"Region ID" row
		TextColumn<RegionGWT> id = new TextColumn<RegionGWT>() {
		    @Override
			    public String getValue(RegionGWT row) {
			    	return row.getID();
			    }
			};
		this.regions.addColumn(id, "Region ID");
		
		//"Region Name" row
		ClickableTextCell nameCell = new ClickableTextCell();
		Column<RegionGWT, String> name = new Column<RegionGWT, String>(nameCell) {
			@Override
			public String getValue(RegionGWT object) {
				return object.getName();
			}			
	    };
	    name.setFieldUpdater(new FieldUpdater<RegionGWT, String>() {
			@Override
			public void update(int index, RegionGWT object, String value) {
				Window.open("../office/offices.html?region_id=" + object.getID(), "_blank", "");
			}
	    });
	    this.regions.addColumn(name, "Region Name");
		
		//"Edit Region" row
		ButtonCell editCell = new ButtonCell();
		Column<RegionGWT, String> edit = new Column<RegionGWT, String>(editCell) {
			@Override
			public String getValue(RegionGWT object) {
				return "Edit " + object.getName();
			}			
	    };
	    edit.setFieldUpdater(new FieldUpdater<RegionGWT, String>() {
			@Override
			public void update(int index, RegionGWT object, String value) {					
				new RegionEditPanel(object).show();
			}
	    });
	    this.regions.addColumn(edit, "Edit Region");
		
		//"Delete Region" row
		ButtonCell deleteCell = new ButtonCell();
		Column<RegionGWT, String> delete = new Column<RegionGWT, String>(deleteCell) {
			@Override
			public String getValue(RegionGWT object) {
				return "Delete " + object.getName();
			}			
	    };
	    delete.setFieldUpdater(new FieldUpdater<RegionGWT, String>() {
			@Override
			public void update(int index, RegionGWT object, String value) {					
				try {
					RegionFacadeGWT.delete(country_id, pageLength, pageNumber, object, regions);
				} catch (RequestException e) {
					RootPanel.get().add(new Label("DeleteRequestError: " + e.getMessage()));
				}
			}
	    });
	    this.regions.addColumn(delete, "Delete Region");
	}
	
	@Override
	public void onModuleLoad() {
		try {
			country_id = Window.Location.getParameter("country_id");
			
			buildTable();
			RegionFacadeGWT.load(country_id, pageLength, pageNumber, this.regions);
			
			RootPanel.get().add(new HeaderTable());
			RootPanel.get().add(this.regions);
			
		} catch (RequestException e) {
			
		}
	}

}
