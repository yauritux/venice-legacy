package com.gdn.venice.client.app.finance.widgets;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * This is the widget that is used to display the screen to handle the task
 * for the user to reconcile cash received vs sales journal in accordance with
 * UR 4.3.3.3
 * 
 * It needs to be linked to the back end (similar to the journal screen).
 * It also needs to be instantiated in a task widget so that the task
 * can call it when the user clicks on the task and it is related to
 * the BPM task.
 * 
 * A BPM task also needs to be created for this to create a human task when
 * an order item goes to CX status.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class CashReceiveAndSalesJournalReconciliationVLayoutWidget extends VLayout {
	
	/**
	 * Constructor that builds the layout
	 */
	public CashReceiveAndSalesJournalReconciliationVLayoutWidget() {
		SectionStack cashReceiveAndSalesJournalReconciliationStack = new SectionStack();
		cashReceiveAndSalesJournalReconciliationStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		
		SectionStackSection orderSection = new SectionStackSection("Order Info");
		orderSection.addItem(showOrderDetail());
		
		SectionStackSection cashReceiveJournalSection = new SectionStackSection("Cash Receive Journal");
		cashReceiveJournalSection.addItem(showCashReceiveJournal());
		
		SectionStackSection salesJournalSection = new SectionStackSection("Sales Journal");
		salesJournalSection.addItem(showSalesJournal());
		
		cashReceiveAndSalesJournalReconciliationStack.addSection(orderSection);
		cashReceiveAndSalesJournalReconciliationStack.addSection(cashReceiveJournalSection);
		cashReceiveAndSalesJournalReconciliationStack.addSection(salesJournalSection);
		cashReceiveAndSalesJournalReconciliationStack.collapseSection(0);
		cashReceiveAndSalesJournalReconciliationStack.expandSection(1);
		cashReceiveAndSalesJournalReconciliationStack.expandSection(2);
		
		ToolStrip cashReceiveAndSalesJournalReconciliationToolStrip = new ToolStrip();
		cashReceiveAndSalesJournalReconciliationToolStrip.setWidth100();
		cashReceiveAndSalesJournalReconciliationToolStrip.setPadding(2);
		
		ToolStripButton refundButton = new ToolStripButton();
		refundButton.setIcon("[SKIN]/icons/refund.png");
		refundButton.setTooltip("Refund");
				
		ToolStripButton allocateExcessButton = new ToolStripButton();
		allocateExcessButton.setIcon("[SKIN]/icons/allocateexcess.png");
		allocateExcessButton.setTooltip("Allocate to Over/Less Payment");
		
		cashReceiveAndSalesJournalReconciliationToolStrip.addButton(refundButton);
		cashReceiveAndSalesJournalReconciliationToolStrip.addButton(allocateExcessButton);
		
		setHeight100();
		
		setMembers(cashReceiveAndSalesJournalReconciliationToolStrip, showSummary(), cashReceiveAndSalesJournalReconciliationStack);
	}
	
	/**
	 * Shows the summary of the remaining balance
	 * @return the label that displays the summary
	 */
	private Label showSummary() {
		String summary = "<span style=\"font-size:20px\">Remaining Balance : (Rp. 5,258,750)</span>";
		
		Label label = new Label(summary);
		label.setWidth100();
		label.setHeight(10);
		label.setMargin(5);
		label.setValign(VerticalAlignment.TOP);
		
		return label;
	}

	/**
	 * Shows the order detail
	 * @return the HTMLFlow that displays the order detail
	 */
	private HTMLFlow showOrderDetail() {
		HTMLFlow orderDetailFlow = new HTMLFlow();
		orderDetailFlow.setPadding(5);
		
		String[] data = new String[] {
				"ABCD", "Rp 6,577,900", "Dec 25, 2010 10:10 AM", "CX", "Djoko Susilo", "Yes", "114.199.97.88"
		};

		//orderDetailFlow.setContents(FraudCaseManagementOrderTab.buildOrderLabel(data));
		orderDetailFlow.setWidth100();
		
		return orderDetailFlow;
	}
	
	/**
	 * Show the cash received journal
	 * @return the HTMLFlow that displays the cash received journal
	 */
	private HTMLFlow showCashReceiveJournal() {

		HTMLFlow journalDetailFlow = new HTMLFlow();
		journalDetailFlow.setPadding(5);
		
		String journalDetail = "<table width=\"100%\">" +
				"<tr>" +
				"<td class=\"journalTitle\">&nbsp;</td>" +
				"<td class=\"journalTitle\">Account No.</td>" +
				"<td class=\"journalTitle\">Account Desc.</td>" +
				"<td class=\"journalTitle\"><b>Debit</b></td>" +
				"<td class=\"journalTitle\"><b>Credit</b></td>" +
				"</tr>" +
				"<tr>" +
				"<td class=\"journalEntry\">1.</td>" +
				"<td colspan=\"4\"><b>Payment Type CC</b></td>" +
				"</tr>" +
				"<tr>" +
				"<td>&nbsp;</td>" +
				"<td>2210001</td>" +
				"<td>Uang Muka Pelanggan</td>" +
				"<td class=\"journalEntry\" >&nbsp;</td>" +
				"<td class=\"journalEntry\" >Rp. 5,976,400</td>" +
				"</tr>" +
				"<tr>" +
				"<td colspan=\"5\">&nbsp;</td>" +
				"</tr>" +
				
				"<tr>" +
				"<td class=\"journalEntry\">2.</td>" +
				"<td colspan=\"4\"><b>Payment Type IB</b></td>" +
				"</tr>" +
				"<tr>" +
				"<td>&nbsp;</td>" +
				"<td>2210001</td>" +
				"<td>Uang Muka Pelanggan</td>" +
				"<td class=\"journalEntry\" >&nbsp;</td>" +
				"<td class=\"journalEntry\" >Rp. 5,258,750</td>" +
				"</tr>" +
				"<tr>" +
				"<td colspan=\"5\">&nbsp;</td>" +
				"</tr>" +
				
				"<tr>" +
				"<td class=\"journalEntry\">3.</td>" +
				"<td colspan=\"4\"><b>Payment Type VA</b></td>" +
				"</tr>" +
				"<tr>" +
				"<td>&nbsp;</td>" +
				"<td>2210001</td>" +
				"<td>Uang Muka Pelanggan</td>" +
				"<td class=\"journalEntry\" width=\"20%\">&nbsp;</td>" +
				"<td class=\"journalEntry\" width=\"20%\">Rp. 601,500</td>" +
				"</tr>" +
				"<tr>" +
				"<td class=\"journalTitle\" colspan=\"5\">Total</td>" +
				"<td class=\"journalTitle\" align=\"right\">Rp. 11,836,650</td>" +
				"</tr>" +
				
				"</table>";
		
		journalDetailFlow.setContents(journalDetail);
		journalDetailFlow.setWidth100();
		journalDetailFlow.setHeight(100);
		journalDetailFlow.setOverflow(Overflow.SCROLL);
		journalDetailFlow.scrollToBottom();
		
		return journalDetailFlow;
	}
	
	/**
	 * Show the sales journal
	 * @return the HTMLFlow that shows the sales journal
	 */
	private HTMLFlow showSalesJournal() {

		HTMLFlow journalDetailFlow = new HTMLFlow();
		journalDetailFlow.setPadding(5);
		
		String journalDetail = "<table width=\"100%\">" +
				"<tr>" +
				"<td class=\"journalTitle\">&nbsp;</td>" +
				"<td class=\"journalTitle\">Account No.</td>" +
				"<td class=\"journalTitle\">Account Desc.</td>" +
				"<td class=\"journalTitle\"><b>Debit</b></td>" +
				"<td class=\"journalTitle\"><b>Credit</b></td>" +
				"</tr>" +
				"<tr>" +
				"<td class=\"journalEntry\">1.</td>" +
				"<td>2210001</td>" +
				"<td>Uang Muka Pelanggan</td>" +
				"<td class=\"journalEntry\" width=\"20%\">Rp. 6,577,900</td>" +
				"<td class=\"journalEntry\" width=\"20%\">&nbsp;</td>" +
				"</tr>" +
				"<tr>" +
				"<td class=\"journalTitle\" colspan=\"5\">Total</td>" +
				"<td class=\"journalTitle\" align=\"right\">Rp. 6,577,900</td>" +
				"</tr>" +
				
				"</table>";
		
		journalDetailFlow.setContents(journalDetail);
		journalDetailFlow.setWidth100();
		journalDetailFlow.setHeight(100);
		journalDetailFlow.setOverflow(Overflow.SCROLL);
		journalDetailFlow.scrollToBottom();
		
		return journalDetailFlow;
	}

}
