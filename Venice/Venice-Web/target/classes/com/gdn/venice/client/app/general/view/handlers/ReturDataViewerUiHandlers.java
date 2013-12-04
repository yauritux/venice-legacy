package com.gdn.venice.client.app.general.view.handlers;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;
import com.smartgwt.client.data.DataSource;

public interface ReturDataViewerUiHandlers extends UiHandlers {
	public List<DataSource> onShowReturDetailData(String returId);
	public void onFetchComboBoxData(int startRow);
	public void onCountTotalData();
}
