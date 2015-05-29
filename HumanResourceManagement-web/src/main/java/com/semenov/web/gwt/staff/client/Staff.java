package com.semenov.web.gwt.staff.client;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.RequestException;
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
import com.semenov.web.gwt.country.client.CountryFacadeGWT;
import com.semenov.web.gwt.office.client.OfficeFacadeGWT;
import com.semenov.web.gwt.region.client.RegionFacadeGWT;

/**
 * <code>staff.html</code> entry point
 * 
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
public class Staff {

	private final CellTable<StaffGWT> offices = new CellTable<StaffGWT>();
	private final HeaderTable headerTable = new HeaderTable();
	private final RootPanel rootPanel = RootPanel.get("staff");
	private int pageLength = 10;
	private int pageNumber = 0;
	
	private String office_id;
	
	/**
	 * Data editor popup panel
	 */
	private class StaffEditPanel extends PopupPanel
	{
		/**
		 * Table with editor components
		 */
		private class DataTable extends FlexTable
		{
			private Button save;
			private Button cancel;
			
			private TextBox firstName;
			private TextBox secondName;
			
			private ListBox countriesList;
			private ListBox regionsList;
			private ListBox officesList;			
			
			public DataTable(final StaffGWT staff)
			{
				firstName = new TextBox();
				firstName.setValue(staff.getFirstName());
				
				secondName = new TextBox();
				secondName.setValue(staff.getSecondName());
				
				countriesList = new ListBox();
				countriesList.addChangeHandler(new ChangeHandler(){
					@Override
					public void onChange(ChangeEvent event) {
						RegionFacadeGWT.fillRegionsList(countriesList.getSelectedValue(), regionsList, null);
					}					
				});
				
				regionsList = new ListBox();
				regionsList.addChangeHandler(new ChangeHandler(){
					@Override
					public void onChange(ChangeEvent event) {
						OfficeFacadeGWT.fillOfficesList(regionsList.getSelectedValue(), officesList, null);
					}					
				});
				
				officesList = new ListBox();
				officesList.addChangeHandler(new ChangeHandler(){
					@Override
					public void onChange(ChangeEvent event) {
						staff.setOfficeId(officesList.getSelectedValue());
					}
				});
				
				save = new Button("Save");
				save.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						try {
							staff.setFirstName(firstName.getValue());
							staff.setSecondName(secondName.getValue());
							StaffFacadeGWT.edit(office_id, pageLength, pageNumber, staff, offices);
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
					{new Label("Staff ID"), new Label(staff.getID())},
					{new Label("Staff First Name"), firstName},
					{new Label("Staff Second Name"), secondName},
					{new Label("Staff Country"), countriesList},
					{new Label("Staff Region"), regionsList},
					{new Label("Staff Office"), officesList},
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
				
				StaffFacadeGWT.fillEditorLists(staff.getID(), countriesList, regionsList, officesList);
			}
		}
		
		public StaffEditPanel(final StaffGWT office)
		{
			super(false);
			this.center();
			this.add(new DataTable(office));
			this.setStylePrimaryName("popupDiv");
		}
	}
	
	private class HeaderTable extends FlexTable
	{
		private final Button addButton = new Button("Add Staff");
		private final ListBox pageNumberListBox = new ListBox();
		private final ListBox pageLengthListBox = new ListBox();
		
		private void pageNumberListBoxChange()
		{
			pageNumber = Integer.parseInt(pageNumberListBox.getSelectedValue());
			StaffFacadeGWT.load(office_id, pageLength, pageNumber, offices);
		}
		
		private void pageLengthListBoxChange()
		{
			pageLength = Integer.parseInt(pageLengthListBox.getSelectedValue());
			StaffFacadeGWT.updatePageList(office_id, pageLength, pageNumberListBox);
			
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
						StaffFacadeGWT.add(office_id, pageLength, pageNumber, offices);
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
			StaffFacadeGWT.updatePageList(office_id, pageLength, pageNumberListBox);
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
		//"Staff ID" row
		TextColumn<StaffGWT> id = new TextColumn<StaffGWT>() {
		    @Override
			    public String getValue(StaffGWT row) {
			    	return row.getID();
			    }
			};
		this.offices.addColumn(id, "Staff ID");
		
		//"Staff First Name" row
		TextCell firstNameCell = new TextCell();
		Column<StaffGWT, String> firstName = new Column<StaffGWT, String>(firstNameCell) {
			@Override
			public String getValue(StaffGWT object) {
				return object.getFirstName();
			}			
	    };
	    this.offices.addColumn(firstName, "Staff First Name");
	    
	    //"Staff Second Name" row
  		TextCell secondNameCell = new TextCell();
  		Column<StaffGWT, String> secondName = new Column<StaffGWT, String>(secondNameCell) {
  			@Override
  			public String getValue(StaffGWT object) {
  				return object.getSecondName();
  			}			
  	    };
  	    this.offices.addColumn(secondName, "Staff Second Name");
		
		//"Edit Staff" row
		ButtonCell editCell = new ButtonCell();
		Column<StaffGWT, String> edit = new Column<StaffGWT, String>(editCell) {
			@Override
			public String getValue(StaffGWT object) {
				return "Edit " + object.getFirstName() + " " + object.getSecondName();
			}			
	    };
	    edit.setFieldUpdater(new FieldUpdater<StaffGWT, String>() {
			@Override
			public void update(int index, StaffGWT object, String value) {					
				new StaffEditPanel(object).show();
			}
	    });
	    this.offices.addColumn(edit, "Edit Staff");
		
		//"Delete Staff" row
		ButtonCell deleteCell = new ButtonCell();
		Column<StaffGWT, String> delete = new Column<StaffGWT, String>(deleteCell) {
			@Override
			public String getValue(StaffGWT object) {
				return "Delete " + object.getFirstName() + " " + object.getSecondName();
			}			
	    };
	    delete.setFieldUpdater(new FieldUpdater<StaffGWT, String>() {
			@Override
			public void update(int index, StaffGWT object, String value) {					
				try {
					StaffFacadeGWT.delete(office_id, pageLength, pageNumber, object, offices);
				} catch (RequestException e) {
					rootPanel.add(new Label("DeleteRequestError: " + e.getMessage()));
				}
			}
	    });
	    this.offices.addColumn(delete, "Delete Staff");
	}
	
	public Staff() {
		try {			
			buildTable();			
			
			rootPanel.add(new HeaderTable());
			rootPanel.add(this.offices);
			
			hide();
			
		} catch (RequestException e) {
			
		}
	}
	
	public void show(String office_id)
	{
		this.office_id = office_id;
		this.pageNumber = 0;
		this.pageLength = 10;		
		
		this.headerTable.show();
		
		StaffFacadeGWT.load(office_id, pageLength, pageNumber, this.offices);
		
		rootPanel.setVisible(true);
	}
	
	public void hide()
	{
		rootPanel.setVisible(false);
	}
}
