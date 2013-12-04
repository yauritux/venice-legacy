package com.gdn.venice.client.app.finance.view.handlers;

import java.util.HashMap;

import com.gwtplatform.mvp.client.UiHandlers;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public interface ManualJournalUiHandlers extends UiHandlers {

	void onManualJournalDetail(DataSource dataSource, ListGridRecord record);

	void onNewManualJournal(DataSource dataSource);

	void onSaveManualJournalClicked(HashMap<String, String> manualJournalDataMap);

	void onDeleteManualJournalClicked(HashMap<String, String> manualJournalMap);
}

