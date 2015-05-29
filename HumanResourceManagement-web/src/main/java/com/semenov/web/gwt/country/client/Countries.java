package com.semenov.web.gwt.country.client;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import java.util.ArrayList;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.TextInputCell.ViewData;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
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
import com.semenov.web.gwt.region.client.Regions;

/**
 * <code>countries.html</code> entry point
 * 
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
public class Countries {	
	
	private final CellTable<CountryGWT> countries = new CellTable<CountryGWT>();
	private final HeaderTable headerTable = new HeaderTable();
	private final RootPanel rootPanel = RootPanel.get("countries");
	private Regions regionsModule;
	private int pageLength = 10;
	private int pageNumber = 0;
	
	private class HeaderTable extends FlexTable
	{
		private class SaveEditButton extends Button
		{
			private boolean isEditing = false;
			
			public SaveEditButton()
			{
				super("Edit");
				addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {					
						try {
							if(isEditing)
							{
								for(CountryGWT object : countries.getVisibleItems())
									if(object.isEditing())
									{
										CountryFacadeGWT.edit(pageLength, pageNumber, object, countries);
										object.setEditing(false);
									}
								
								setText("Edit");								
							}
							else
							{
								for(CountryGWT object : countries.getVisibleItems())
								{
									object.setEditing(object.isChecked());
									object.setChecked(false);
								}
								
								setText("Save");
							}
							
							isEditing = !isEditing;
							countries.redraw();
							
						} catch (RequestException e) {
							rootPanel.add(new Label("AddRequestError: " + e.getMessage()));
						}
					}
				});
			}			
		}
		
		private final Button addButton = new Button("Add Country");
		private final ListBox pageNumberListBox = new ListBox();
		private final ListBox pageLengthListBox = new ListBox();
		private final SaveEditButton saveEditButton = new SaveEditButton();
		private final Button deleteButton = new Button("Delete");
		
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
			CountryFacadeGWT.updatePageList(pageLength, pageNumberListBox);
			
			deleteButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {					
					try {
						for(CountryGWT object : countries.getVisibleItems())
							if(object.isChecked())
								CountryFacadeGWT.delete(pageLength, pageNumber, object, countries);
						
					} catch (RequestException e) {
						rootPanel.add(new Label("AddRequestError: " + e.getMessage()));
					}
				}
			});
			
			setWidget(0, 0, addButton);
			setWidget(0, 1, pageLengthListBox);
			setWidget(0, 2, pageNumberListBox);
			setWidget(0, 3, saveEditButton);
			setWidget(0, 4, deleteButton);
			
			this.setStylePrimaryName("headerTable");
		}
	}
	
	private class LabelInputCell extends AbstractCell<String>
	{
		private TextInputCell inputCell;
		private ClickableTextCell textCell;
		
		public LabelInputCell()
		{
			super(BrowserEvents.CHANGE, BrowserEvents.KEYUP, BrowserEvents.CLICK, BrowserEvents.KEYDOWN);
			
			inputCell = new TextInputCell();
			textCell = new ClickableTextCell();
		}
		
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,	String value, SafeHtmlBuilder sb) {
			if(countries.getVisibleItem(context.getIndex()).isEditing())
				inputCell.render(context, value, sb);
			else
				textCell.render(context, value, sb);
		}
		
		@Override
		public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {			
			//super.onBrowserEvent(context, parent, value, event, valueUpdater);			
			if(countries.getVisibleItem(context.getIndex()).isEditing())
				inputCell.onBrowserEvent(context, parent, value, event, valueUpdater);
			else
				textCell.onBrowserEvent(context, parent, value, event, valueUpdater);
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
		LabelInputCell nameCell = new LabelInputCell();
		Column<CountryGWT, String> name = new Column<CountryGWT, String>(nameCell) {
			@Override
			public String getValue(CountryGWT object) {
				return object.getName();
			}
	    };
	    name.setFieldUpdater(new FieldUpdater<CountryGWT, String>() {
			@Override
			public void update(int index, CountryGWT object, String value) {
				if(object.isEditing())
					object.setName(value);
				else
					regionsModule.show(object.getID());
			}
	    });
	    this.countries.addColumn(name, "Country Name");
		
		//"Choose Country" row
		CheckboxCell checkboxCell = new CheckboxCell();
		Column<CountryGWT, Boolean> check = new Column<CountryGWT, Boolean>(checkboxCell) {
			@Override
			public Boolean getValue(CountryGWT object) {
				return object.isChecked();
			}					
	    };
	    check.setFieldUpdater(new FieldUpdater<CountryGWT, Boolean>() {
			@Override
			public void update(int index, CountryGWT object, Boolean value) {
				object.setChecked(value);
			}
	    });
	    this.countries.addColumn(check, "Choose Country");
	}
	
	public Countries(Regions regionsModule)
	{
		try {
			this.regionsModule = regionsModule;
			
			buildTable();
			CountryFacadeGWT.load(pageLength, pageNumber, this.countries);
			
			rootPanel.add(this.headerTable);
			rootPanel.add(this.countries);
			
		} catch (RequestException e) {
			
		}
	}
	
}
