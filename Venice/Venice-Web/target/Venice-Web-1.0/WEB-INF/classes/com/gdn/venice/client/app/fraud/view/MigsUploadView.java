package com.gdn.venice.client.app.fraud.view;

import java.util.HashMap;
import java.util.Map;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.presenter.MigsUploadPresenter;
import com.gdn.venice.client.app.fraud.ui.widgets.MigsReportListGrid;
import com.gdn.venice.client.app.fraud.view.handlers.MigsUploadUiHandlers;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.EditorValueMapFunction;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class MigsUploadView extends
ViewWithUiHandlers<MigsUploadUiHandlers> implements
MigsUploadPresenter.MyView {
	RafViewLayout migsUploadLayout;
	ListGrid migsReportListGrid;
	ToolStripButton processButton;
	Window uploadWindow;
	
	@Inject
	public MigsUploadView() {
		migsUploadLayout = new RafViewLayout();
		
		//Toolstrip atas
		ToolStrip migsUploadToolStrip = new ToolStrip();
		migsUploadToolStrip.setWidth100();
		
		ToolStripButton uploadButton = new ToolStripButton();
		uploadButton.setIcon("[SKIN]/icons/pages_add.png");
		uploadButton.setTitle("Upload");
		uploadButton.setTooltip("Upload New MIGS Report");
		uploadButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				buildUploadWindow().show();
			}
		});
		
		processButton = new ToolStripButton();
		processButton.setIcon("[SKIN]/icons/page_next.png");
		processButton.setTitle("Process");
		processButton.setTooltip("Submit MIGS Report");
		processButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to process this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value) {
							RPCRequest request = new RPCRequest();
			        		
			            	request.setActionURL(GWT.getHostPageBaseURL() + MigsUploadPresenter.migsUploadPresenterServlet + "?type=RPC");
			        		request.setHttpMethod("POST");
			        		request.setUseSimpleHttp(true);
			        		request.setShowPrompt(false);
			        		
			        		RPCManager.sendRequest(request, 
			    				new RPCCallback () {
			    					public void execute(RPCResponse response,
			    							Object rawData, RPCRequest request) {
			    						
			    						((MigsReportListGrid) migsReportListGrid).refreshUploadedData();
			    						String rpcResponse = rawData.toString();
			    						SC.say(rpcResponse);
			    					}
			        		});
						}
					}
				});
			}
		});

		migsUploadToolStrip.addButton(uploadButton);
		migsUploadToolStrip.addSeparator();
		migsUploadToolStrip.addButton(processButton);
		
		migsReportListGrid = new MigsReportListGrid() {
			@Override
			protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
				if (!record.getAttributeAsString(DataNameTokens.MIGSUPLOAD_PROBLEMDESCRIPTION).equalsIgnoreCase("")) {
					return "color:#FF0000;";
				}
				else {
					return super.getCellCSSText(record, rowNum, colNum);
				}
			}
		};
		
		//Masukkan semua ke dalam layout
		migsUploadLayout.setMembers(migsUploadToolStrip);
		
		bindCustomUiHandlers();
	}
	
	private Window buildUploadWindow() {
		uploadWindow = new Window();
		uploadWindow.setWidth(320);
		uploadWindow.setHeight(150);
		uploadWindow.setTitle("Upload MIGS Report");
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
		uploadForm.setPadding(5);
		uploadForm.setEncoding(Encoding.MULTIPART);
		uploadForm.setTarget("upload_frame");
		
		final SelectItem reportTypeSelectItem = new SelectItem();
		reportTypeSelectItem.setTitle("Report Type");
		reportTypeSelectItem.setValueMap("MIGS", "Mandiri Installment");
		
		UploadItem reportFileItem = new UploadItem();
		reportFileItem.setTitle("MIGS File");
		uploadForm.setItems(reportTypeSelectItem,reportFileItem);
		
		HLayout buttonLayout = new HLayout(5);
		IButton buttonUpload = new IButton("Upload");
		IButton buttonCancel = new IButton("Cancel");
		

		
		buttonUpload.addClickHandler(new ClickHandler() {
			@Override	
			public void onClick(ClickEvent event) {
				String host = GWT.getHostPageBaseURL();
				
				if (reportTypeSelectItem.getValueAsString().equals("MIGS")) {
					uploadForm.setAction(host + "MigsUploadPresenterServlet");
				} else if (reportTypeSelectItem.getValueAsString().equals("Mandiri Installment")) {
					uploadForm.setAction(host + "MandiriInstallmentUploadPresenterServlet");
				} 
				
				
//				uploadForm.submit(new DSCallback() {
//					
//					@Override
//					public void execute(DSResponse response, Object rawData, DSRequest request) {
//						((MigsReportListGrid) migsReportListGrid).refreshUploadedData();
//					}
//				});
				uploadForm.submitForm();
				
				uploadWindow.destroy();
			}
		});
		
		buttonCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				uploadWindow.destroy();
			}
		});
		
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setMembers(buttonUpload, buttonCancel);
		uploadLayout.setMembers(uploadForm, buttonLayout);
		uploadWindow.addItem(uploadLayout);
		
		return uploadWindow;
	}

	@Override
	public Widget asWidget() {
		return migsUploadLayout;
	}

	protected void bindCustomUiHandlers() {
		migsReportListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				migsReportListGrid.saveAllEdits();
				migsReportListGrid.clearCriteria();		
			}
		});
	}


	@Override
	public void loadMigsUploadData(DataSource dataSource) {
		
		migsReportListGrid.setAutoFetchData(false);
		migsReportListGrid.setDataSource(dataSource);
		migsReportListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		//Rapikan width-width
		migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_MIGSID).setWidth("60px");
		migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_MERCHANTTRANSACTIONREFERENCE).setWidth("75px");
		migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_AMOUNT).setWidth("120px");
		Util.formatListGridFieldAsCurrency(migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_AMOUNT));
		migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_AUTHORISATIONCODE).setWidth("80px");
		migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_CARDNUMBER).setWidth("140px");
		migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_CARDTYPE).setWidth("75px");
		migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_ACTION).setWidth("85px");
		migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_TRANSACTIONTYPE).setWidth("100px");
		migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_RESPONSECODE).setWidth("100px");
		migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_PROBLEMDESCRIPTION).setWidth("240px");
		migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_ECOMMERCEINDICATOR).setWidth("50px");
		
		//Calculate value untuk field WCS Order ID (Merchant Transaction Reference)
		migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_MERCHANTTRANSACTIONREFERENCE).setCellFormatter(new CellFormatter() {
			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
				if (value == null) {  
                    return null;  
                } else {  
                    try {  
                        return value.toString().replaceAll("-.*$", "");  
                    } catch (Exception e) {
                        return value.toString();  
                    }  
                }
			}
		});
		
		//Set editor untuk action (pakai combo box)
		SelectItem departmentSelectItem = new SelectItem();  
        departmentSelectItem.setAddUnknownValues(false); 
		migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_ACTION).setEditorValueMapFunction(new EditorValueMapFunction() {
			@Override
			public Map getEditorValueMap(Map values, ListGridField field, ListGrid grid) {
				Map<String, String> valueMap = new HashMap<String, String>(); 
				if (((String) values.get(DataNameTokens.MIGSUPLOAD_PROBLEMDESCRIPTION)).toLowerCase().contains("not an approved transaction") ||
						((String) values.get(DataNameTokens.MIGSUPLOAD_PROBLEMDESCRIPTION)).toLowerCase().contains("incomplete data")) {
					valueMap.put("KEEP", "KEEP");
					valueMap.put("REMOVE", "REMOVE");
				}
				else if (((String) values.get(DataNameTokens.MIGSUPLOAD_PROBLEMDESCRIPTION)).toLowerCase().contains("duplicate")) {
					valueMap.put("SUBMIT", "SUBMIT");
					valueMap.put("KEEP", "KEEP");
					valueMap.put("REMOVE", "REMOVE");
				}
				else if (((String) values.get(DataNameTokens.MIGSUPLOAD_PROBLEMDESCRIPTION)).toLowerCase().contains("uploaded before")) {
					valueMap.put("REMOVE", "REMOVE");
//					valueMap.put("OVERWRITE", "OVERWRITE");
					valueMap.put("KEEP", "KEEP");
				}
				else if (((String) values.get(DataNameTokens.MIGSUPLOAD_PROBLEMDESCRIPTION)).toLowerCase().contains("not found")) {
					valueMap.put("KEEP", "KEEP");
					valueMap.put("REMOVE", "REMOVE");
				}
				else {
					valueMap.put("SUBMIT", "SUBMIT");
					valueMap.put("KEEP", "KEEP");
					valueMap.put("REMOVE", "REMOVE");
				}
				
				return valueMap;
			}  
        }); 
		
		Map<String, String> valueMap = new HashMap<String, String>(); 
		valueMap.put("SUBMIT", "SUBMIT");
		valueMap.put("KEEP", "KEEP");
		valueMap.put("REMOVE", "REMOVE");
//		valueMap.put("OVERWRITE", "OVERWRITE");
		
		migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_ACTION).setEditorType(departmentSelectItem);
		migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_ACTION).setCanEdit(true);
		migsReportListGrid.setShowRowNumbers(true);
		migsReportListGrid.getField(DataNameTokens.MIGSUPLOAD_ACTION).setFilterEditorValueMap(valueMap);
		
		//Set sort by problem description ascending
		migsReportListGrid.sort(DataNameTokens.MIGSUPLOAD_PROBLEMDESCRIPTION, SortDirection.ASCENDING);
		
		migsUploadLayout.addMember(migsReportListGrid);
	}

	@Override
	public void refreshMigsUploadData() {
		if (migsReportListGrid instanceof MigsReportListGrid) {
			((MigsReportListGrid) migsReportListGrid).refreshUploadedData();
		}
	}
}