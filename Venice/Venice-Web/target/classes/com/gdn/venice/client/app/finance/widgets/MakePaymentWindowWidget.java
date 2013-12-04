package com.gdn.venice.client.app.finance.widgets;

import java.util.ArrayList;
import java.util.List;

import com.gdn.venice.client.util.Util;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDropEvent;
import com.smartgwt.client.widgets.grid.events.RecordDropHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class MakePaymentWindowWidget extends Window{
	ListGrid paymentListGrid;
	
	ListGrid bank1ListGrid;
	ListGrid bank2ListGrid;
	
	HTMLFlow bank1Total;
	HTMLFlow bank2Total;
	
	String amountField;
	
	
	public MakePaymentWindowWidget(ListGrid paymentListGrid, ListGridField[] makePaymentFields, String amountField) {
		this.paymentListGrid = paymentListGrid;
		this.amountField=amountField;
		
		setWidth(500);
		setHeight(200);
		setTitle("Make Payment");
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				destroy();
			}
		});
		
		VLayout makePaymentLayout = new VLayout();
		makePaymentLayout.setPadding(5);
		
		HLayout bankListGridLayout = new HLayout();
        bankListGridLayout.setWidth100();
        bankListGridLayout.setHeight100();
        
        VLayout bank1Layout = new VLayout();
        
        HTMLFlow bank1Title = new HTMLFlow();
        bank1Title.setContents("<b>Bank BCA</b>");

        bank1ListGrid = new ListGrid();
        bank1ListGrid.setCanDragRecordsOut(true);
        bank1ListGrid.setCanAcceptDroppedRecords(true);
        bank1ListGrid.setCanReorderFields(true);
        bank1ListGrid.setDragDataAction(DragDataAction.MOVE);
        
        bank1ListGrid.setDefaultFields(makePaymentFields);
        bank1ListGrid.setData(getListOfPayment("BCA"));
        
        bank1Total = new HTMLFlow();
        bank1Total.setContents("<b>Total Bank BCA:</b> " + calculateTotal(bank1ListGrid, null, true));

        
        bank1Layout.setMembers(bank1Title, bank1ListGrid, bank1Total);
        bankListGridLayout.addMember(bank1Layout);
        
        VLayout bank2Layout = new VLayout();
        
        HTMLFlow bank2Title = new HTMLFlow();
        bank2Title.setContents("<b>Bank Mandiri</b>");

        bank2ListGrid = new ListGrid();
        bank2ListGrid.setCanDragRecordsOut(true);
        bank2ListGrid.setCanAcceptDroppedRecords(true);
        bank2ListGrid.setCanReorderRecords(true);
        
        bank2ListGrid.setDefaultFields(makePaymentFields);
        bank2ListGrid.setData(getListOfPayment("Mandiri"));
        
        bank1ListGrid.addRecordDropHandler(new RecordDropHandler() {
			
			@Override
			public void onRecordDrop(RecordDropEvent event) {
				if (event.getSourceWidget()!=bank1ListGrid) {
					transferLeft(event.getDropRecords());
				}
			}
		});
        
        bank2ListGrid.addRecordDropHandler(new RecordDropHandler() {
			
			@Override
			public void onRecordDrop(RecordDropEvent event) {
				if (event.getSourceWidget()!=bank2ListGrid) {
					transferRight(event.getDropRecords());
				}
			}
		});
        
        bank2Total = new HTMLFlow();
        bank2Total.setContents("<b>Total Bank Mandiri:</b> " + calculateTotal(bank2ListGrid, null, true));
        
        bank2Layout.setMembers(bank2Title, bank2ListGrid, bank2Total);

        VLayout transferButtonLayout = new VLayout();
        transferButtonLayout.setWidth(32);
        transferButtonLayout.setHeight(74);
        transferButtonLayout.setLayoutAlign(Alignment.CENTER);

        TransferImgButton rightImg = new TransferImgButton(TransferImgButton.RIGHT);
        rightImg.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
            	transferRight(bank1ListGrid.getSelection());
                bank2ListGrid.transferSelectedData(bank1ListGrid);
            }
        });
        transferButtonLayout.addMember(rightImg);

        TransferImgButton leftImg = new TransferImgButton(TransferImgButton.LEFT);
        leftImg.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
            	transferLeft(bank2ListGrid.getSelection());
                bank1ListGrid.transferSelectedData(bank2ListGrid);
            }
        });
        transferButtonLayout.addMember(leftImg);

        bankListGridLayout.addMember(transferButtonLayout);
        
        bankListGridLayout.addMember(bank2Layout);
        
        makePaymentLayout.addMember(bankListGridLayout);
        
        HLayout buttonLayout = new HLayout(5);
        buttonLayout.setPadding(5);
        
        IButton buttonOK = new IButton("OK");
        IButton buttonCancel = new IButton("Cancel");
        
        buttonOK.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				destroy();
			}
		});
		buttonCancel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				destroy();
			}
		});
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setMembers(buttonOK, buttonCancel);
		
		makePaymentLayout.addMember(buttonLayout);
        
        addItem(makePaymentLayout);
		
	}
	
	private void transferRight(ListGridRecord[] newRecords) {
		for (int i=0;i<newRecords.length;i++) {
			newRecords[i].setAttribute("bank", "Mandiri");
			paymentListGrid.updateData(newRecords[i]);
			bank1Total.setContents("<b>Total Bank BCA:</b> " + calculateTotal(bank1ListGrid, newRecords, false));
			bank2Total.setContents("<b>Total Bank Mandiri:</b> " + calculateTotal(bank2ListGrid, newRecords, true));
		}
	}
	
	private void transferLeft(ListGridRecord[] newRecords) {
		for (int i=0;i<newRecords.length;i++) {
			newRecords[i].setAttribute("bank", "BCA");
			paymentListGrid.updateData(newRecords[i]);
			bank1Total.setContents("<b>Total Bank BCA:</b> " + calculateTotal(bank1ListGrid, newRecords, true));
			bank2Total.setContents("<b>Total Bank Mandiri:</b> " + calculateTotal(bank2ListGrid, newRecords, false));
		}
	}
	
	private String calculateTotal(ListGrid bankListGrid, ListGridRecord[] newRecords, boolean add) {
		ListGridRecord[] listGridRecords = bankListGrid.getRecords();
		
		List<ListGridRecord> recordList = new ArrayList<ListGridRecord>();
		
		long total = 0;
		
		if (add) {
			if (listGridRecords!=null) {
				for (int i=0;i<listGridRecords.length;i++) {
					recordList.add(listGridRecords[i]);
				}
			}
			if (newRecords!=null) {
				for (int i=0;i<newRecords.length;i++) {
					recordList.add(newRecords[i]);
				}
			}
			
			ListGridRecord[] records = recordList.toArray(new ListGridRecord[0]);
			
			for (int i=0;i<records.length;i++) {
				total = total + new Long(records[i].getAttributeAsString(amountField)).longValue();
			}
		} else {
			if (listGridRecords!=null) {
				for (int i=0;i<listGridRecords.length;i++) {
					total = total + new Long(listGridRecords[i].getAttributeAsString(amountField)).longValue();
				}
			}
			
		
			if (newRecords!=null) {
				for (int i=0;i<newRecords.length;i++) {
					total = total - new Long(newRecords[i].getAttributeAsString(amountField)).longValue();
				}
			}
		}
		
		return Util.formatStringAsCurrency(new Long(total).toString());
		
	}
	
	private ListGridRecord[] getListOfPayment(String bank) {
		ListGridRecord[] selectedRecords = paymentListGrid.getSelection();
		
		List<ListGridRecord> bankRecords = new ArrayList<ListGridRecord>();
		
		for (int i=0;i<selectedRecords.length;i++) {
			if (selectedRecords[i].getAttribute("bank").equals(bank)) {
				bankRecords.add(selectedRecords[i]);
			}
		}
		
		return bankRecords.toArray(new ListGridRecord[0]);
	}


}
