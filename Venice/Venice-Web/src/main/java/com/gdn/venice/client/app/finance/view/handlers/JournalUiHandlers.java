package com.gdn.venice.client.app.finance.view.handlers;

import java.util.ArrayList;

import com.gwtplatform.mvp.client.UiHandlers;

public interface JournalUiHandlers extends UiHandlers {
	/**
	 * Submit the selected journals for approval to the BPM system
	 * @param journalGroupIdList
	 */
	public void onSubmitForApproval(ArrayList<String> journalGroupIdList);
}

