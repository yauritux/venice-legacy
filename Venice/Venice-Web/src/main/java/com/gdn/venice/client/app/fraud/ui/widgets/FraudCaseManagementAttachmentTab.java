package com.gdn.venice.client.app.fraud.ui.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.util.Util;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Widget for Attachment
 * 
 * @author Roland
 */

public class FraudCaseManagementAttachmentTab extends Tab {
	ListGrid attachmentListGrid = null;

	public FraudCaseManagementAttachmentTab(String title, final DataSource attachmentData) {
		super(title);		

		ToolStrip attachmentToolStrip = new ToolStrip();
		attachmentToolStrip.setWidth100();

		ToolStripButton addButton = new ToolStripButton();
		addButton.setIcon("[SKIN]/icons/add.png");
		addButton.setTooltip("Add Attachment");
		addButton.setTitle("Add");

		ToolStripButton removeButton = new ToolStripButton();
		removeButton.setIcon("[SKIN]/icons/remove.png");
		removeButton.setTooltip("Remove Attachment");
		removeButton.setTitle("Remove");

		attachmentToolStrip.addButton(addButton);
		attachmentToolStrip.addButton(removeButton);
		
		attachmentListGrid = new ListGrid();
		attachmentListGrid.setWidth100();
		attachmentListGrid.setHeight100();
		attachmentListGrid.setShowAllRecords(true);
		attachmentListGrid.setSortField(0);
		attachmentListGrid.setAutoFetchData(true);
		attachmentListGrid.setDataSource(attachmentData);
		attachmentListGrid.setShowFilterEditor(true);
		attachmentListGrid.setFields(Util.getListGridFieldsFromDataSource(attachmentData));
		attachmentListGrid.setCanSort(true);
		attachmentListGrid.getField(DataNameTokens.FRDFRAUDCASEATTACHMENT_ATTACHMENTID).setHidden(true);
		attachmentListGrid.getField(DataNameTokens.FRDFRAUDCASEATTACHMENT_FRDFRAUDSUSPICIONCASE_SUSPICIONCASEID).setHidden(true);
		attachmentListGrid.setSelectionType(SelectionStyle.SIMPLE);
		attachmentListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		attachmentListGrid.setCanEdit(true);
		attachmentListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		attachmentListGrid.setCanSort(true);
		attachmentListGrid.setCanResizeFields(true);
		attachmentListGrid.setShowRowNumbers(true);
		attachmentListGrid.getField(DataNameTokens.FRDFRAUDCASEATTACHMENT_ATTACHMENTID).setCanEdit(false);
		attachmentListGrid.getField(DataNameTokens.FRDFRAUDCASEATTACHMENT_FILENAME).setCanEdit(false);	
		attachmentListGrid.getField(DataNameTokens.FRDFRAUDCASEATTACHMENT_CREATEDBY).setCanEdit(false);	
		attachmentListGrid.getField(DataNameTokens.FRDFRAUDCASEATTACHMENT_FILELOCATION).setCanEdit(false);	
		attachmentListGrid.getField(DataNameTokens.FRDFRAUDCASEATTACHMENT_FRDFRAUDSUSPICIONCASE_SUSPICIONCASEID).setCanEdit(false);	
		attachmentListGrid.getField(DataNameTokens.FRDFRAUDCASEATTACHMENT_FILELOCATION).setCellFormatter(new CellFormatter() {
			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,
					int colNum) {
				String cellFormat = (String) value;
				cellFormat = cellFormat.substring(cellFormat.lastIndexOf("/")+1, cellFormat.length());
				return "<a href='" + GWT.getHostPageBaseURL() +
					MainPagePresenter.fileDownloadPresenterServlet +
					"?filename=" + value + "' target='_blank'>" + 
					cellFormat + "</a>";
			}
		});
		
		VLayout attachmentLayout = new VLayout();
		attachmentLayout.setMembers(attachmentToolStrip, attachmentListGrid);
		setPane(attachmentLayout);
		
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				DynamicForm formParent = (DynamicForm) attachmentListGrid.getParentElement().getParentElement().getParentElement().getParentElement().getChildren()[1];
				showAddAttachmentDialog(formParent.getItem(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID).getValue().toString()).show();
			}
		});
		
		attachmentListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				attachmentListGrid.saveAllEdits();
				attachmentListGrid.clearCriteria();
				if(event.getDsResponse().getStatus()==0){
					SC.say("Data Added/Edited");
				}					
			}
		});
		
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
							attachmentListGrid.removeSelectedData();
							attachmentListGrid.clearCriteria();
							SC.say("Data Removed");
						}
					}
				});
			}
		});
		
		bindCustomUiHandlers();
	}
	
	protected void bindCustomUiHandlers() {
		attachmentListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshFraudCaseAttachmentData();
			}
		});
	}
	
	private Window showAddAttachmentDialog(String fraudCaseId) {
		final Window uploadWindow = new Window();
		uploadWindow.setWidth(350);
		uploadWindow.setHeight(200);
		uploadWindow.setTitle("Add Attachment");
		uploadWindow.setShowMinimizeButton(false);
		uploadWindow.setIsModal(true);
		uploadWindow.setShowModalMask(true);
		uploadWindow.centerInPage();
		uploadWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				uploadWindow.destroy();
			}
		});
		VLayout uploadLayout = new VLayout();
		uploadLayout.setHeight100();
		uploadLayout.setWidth100();
		
		final DynamicForm uploadForm = new DynamicForm();
		uploadForm.setHeight100();
		uploadForm.setWidth100();
		uploadForm.setPadding(5);
		uploadForm.setEncoding(Encoding.MULTIPART);
		uploadForm.setTarget("upload_frame");

		HiddenItem fraudCaseIdItem = new HiddenItem();
		fraudCaseIdItem.setName(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID);
		fraudCaseIdItem.setDefaultValue(fraudCaseId);

		TextAreaItem descItem = new TextAreaItem();
		descItem.setTitle("Description");
		descItem.setName(DataNameTokens.FRDFRAUDCASEATTACHMENT_DESCRIPTION);
		descItem.setWidth("100%");
		descItem.setHeight(50);
		descItem.setDefaultValue("");
		
		//validation
		//length
		LengthRangeValidator lengthRangeValidator = new LengthRangeValidator();  
		lengthRangeValidator.setMax(1000);  
		descItem.setValidators(lengthRangeValidator);

		final UploadItem attachmentFileItem = new UploadItem();
		attachmentFileItem.setTitle("Attachment");
		
		uploadForm.setItems(fraudCaseIdItem, descItem, attachmentFileItem);
		
		//required
		uploadForm.getField(DataNameTokens.FRDFRAUDCASEATTACHMENT_DESCRIPTION).setRequired(true);

		HLayout btnLayout = new HLayout();
		btnLayout.setMembersMargin(5);
		btnLayout.setPadding(10);
		Button btnSave = new Button("Save");		
		btnSave.addClickHandler(new ClickHandler() {
			@Override	
			public void onClick(ClickEvent event) {
				String host = GWT.getHostPageBaseURL();
				String caseId = uploadForm.getItem(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID).getValue().toString();
				String desc = uploadForm.getItem(DataNameTokens.FRDFRAUDCASEATTACHMENT_DESCRIPTION).getValue().toString();
				
				uploadForm.setAction(host + "FraudCaseAttachmentServlet?caseid=" + caseId + "&desc=" + desc);
				uploadForm.submitForm();
				uploadWindow.destroy();		
				refreshFraudCaseAttachmentData();
			}
		});
		Button btnCancel = new Button("Cancel");
		btnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				uploadWindow.destroy();
			}
		});

		btnLayout.setMembers(btnSave, btnCancel);

		uploadLayout.setMembers(uploadForm, btnLayout);
		uploadWindow.addItem(uploadLayout);
		return uploadWindow;
	}
	
	public void refreshFraudCaseAttachmentData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				attachmentListGrid.setData(response.getData());
			}
		};
		attachmentListGrid.getDataSource().fetchData(attachmentListGrid.getFilterEditorCriteria(), callBack);
	}	
}
