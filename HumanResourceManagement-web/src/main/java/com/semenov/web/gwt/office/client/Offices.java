package com.semenov.web.gwt.office.client;

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
import com.semenov.web.gwt.region.client.RegionFacadeGWT;
import com.semenov.web.gwt.staff.client.Staff;

/**
 * <code>offices.html</code> entry point
 * 
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
public class Offices {	
	
	private final CellTable<OfficeGWT> offices = new CellTable<OfficeGWT>();
	private final HeaderTable headerTable = new HeaderTable();
	private final RootPanel rootPanel = RootPanel.get("offices");
	private Staff staffModule;
	private int pageLength = 10;
	private int pageNumber = 0;
	
	private String region_id;
	
	/**
	 * Data editor popup panel
	 */
	private class OfficeEditPanel extends PopupPanel
	{
		/**
		 * Table with editor components
		 */
		private class DataTable extends FlexTable
		{
			private Button save;
			private Button cancel;
			
			private TextBox name;
			
			public DataTable(final OfficeGWT office)
			{
				name = new TextBox();
				name.setValue(office.getName());
				
				save = new Button("Save");
				save.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						try {
							office.setName(name.getValue());
							OfficeFacadeGWT.edit(region_id, pageLength, pageNumber, office, offices);
							hide();
						} catch (RequestException e) {
							rootPanel.add(new Label("EditRequestError: " + e.getMessage()));
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
					{new Label("Office ID"), new Label(office.getID())},
					{new Label("Office Name"), name},
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
		
		public OfficeEditPanel(final OfficeGWT office)
		{
			super(false);
			this.center();
			this.add(new DataTable(office));
			this.setStylePrimaryName("popupDiv");
		}
	}
	
	private class HeaderTable extends FlexTable
	{
		private final Button addButton = new Button("Add Office");
		private final ListBox pageNumberListBox = new ListBox();
		private final ListBox pageLengthListBox = new ListBox();
		
		private void pageNumberListBoxChange()
		{
			pageNumber = Integer.parseInt(pageNumberListBox.getSelectedValue());
			OfficeFacadeGWT.load(region_id, pageLength, pageNumber, offices);
		}
		
		private void pageLengthListBoxChange()
		{
			pageLength = Integer.parseInt(pageLengthListBox.getSelectedValue());
			OfficeFacadeGWT.updatePageList(region_id, pageLength, pageNumberListBox);
			
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
						OfficeFacadeGWT.add(region_id, pageLength, pageNumber, offices);
					} catch (RequestException e) {
						rootPanel.add(new Label("AddRequestError: " + e.getMessage()));
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
			
			setWidget(0, 0, addButton);
			setWidget(0, 1, pageLengthListBox);
			setWidget(0, 2, pageNumberListBox);
			
			this.setStylePrimaryName("headerTable");
		}
		
		public void show()
		{
			OfficeFacadeGWT.updatePageList(region_id, pageLength, pageNumberListBox);
			pageNumberListBox.setSelectedIndex(0);
		}
	}
	
	/**
	 * Creates table for offices
	 * @return - table for offices
	 * @throws RequestException
	 */
	private void buildTable() throws RequestException
	{		
		//"Office ID" row
		TextColumn<OfficeGWT> id = new TextColumn<OfficeGWT>() {
		    @Override
			    public String getValue(OfficeGWT row) {
			    	return row.getID();
			    }
			};
		this.offices.addColumn(id, "Office ID");
		
		//"Office Name" row
		ClickableTextCell nameCell = new ClickableTextCell();
		Column<OfficeGWT, String> name = new Column<OfficeGWT, String>(nameCell) {
			@Override
			public String getValue(OfficeGWT object) {
				return object.getName();
			}			
	    };
	    name.setFieldUpdater(new FieldUpdater<OfficeGWT, String>() {
			@Override
			public void update(int index, OfficeGWT object, String value) {
				staffModule.show(object.getID());
			}
	    });
	    this.offices.addColumn(name, "Office Name");
		
		//"Edit Office" row
		ButtonCell editCell = new ButtonCell();
		Column<OfficeGWT, String> edit = new Column<OfficeGWT, String>(editCell) {
			@Override
			public String getValue(OfficeGWT object) {
				return "Edit " + object.getName();
			}			
	    };
	    edit.setFieldUpdater(new FieldUpdater<OfficeGWT, String>() {
			@Override
			public void update(int index, OfficeGWT object, String value) {					
				new OfficeEditPanel(object).show();
			}
	    });
	    this.offices.addColumn(edit, "Edit Office");
		
		//"Delete Office" row
		ButtonCell deleteCell = new ButtonCell();
		Column<OfficeGWT, String> delete = new Column<OfficeGWT, String>(deleteCell) {
			@Override
			public String getValue(OfficeGWT object) {
				return "Delete " + object.getName();
			}			
	    };
	    delete.setFieldUpdater(new FieldUpdater<OfficeGWT, String>() {
			@Override
			public void update(int index, OfficeGWT object, String value) {					
				try {
					OfficeFacadeGWT.delete(region_id, pageLength, pageNumber, object, offices);
				} catch (RequestException e) {
					rootPanel.add(new Label("DeleteRequestError: " + e.getMessage()));
				}
			}
	    });
	    this.offices.addColumn(delete, "Delete Office");
	}
	
	public Offices(Staff staffModule) {
		try {
			this.staffModule = staffModule;
			
			buildTable();			
			
			rootPanel.add(new HeaderTable());
			rootPanel.add(this.offices);
			
			hide();
			
		} catch (RequestException e) {
			
		}
	}
	
	public void show(String region_id)
	{
		this.region_id = region_id;
		this.pageNumber = 0;
		this.pageLength = 10;
		
		this.headerTable.show();
		
		OfficeFacadeGWT.load(region_id, pageLength, pageNumber, this.offices);
		
		this.staffModule.hide();
		rootPanel.setVisible(true);
	}
	
	public void hide()
	{
		this.staffModule.hide();
		this.rootPanel.setVisible(false);
	}
}
