package com.gdn.venice.client.app.reservation.data;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.reservation.presenter.ReservationOrderManagementPresenter;
import com.gdn.venice.client.data.RafDataSource;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class ReservationData {

	public static DataSource getReservationOrderManagementData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.RSV_ORDERMANAGEMENT_WCSORDERID, "WCS Order ID"),
				new DataSourceTextField(DataNameTokens.RSV_ORDERMANAGEMENT_RESERVATIONID, "Reservation ID"),
				new DataSourceTextField(DataNameTokens.RSV_ORDERMANAGEMENT_EMAILADDRESS, "Email Address"),
				new DataSourceTextField(DataNameTokens.RSV_ORDERMANAGEMENT_ORDERDATE, "Order Date"),
				new DataSourceTextField(DataNameTokens.RSV_ORDERMANAGEMENT_FIRSTNAME, "First Name"),
				new DataSourceTextField(DataNameTokens.RSV_ORDERMANAGEMENT_LASTNAME, "Last Name"),
				new DataSourceTextField(DataNameTokens.RSV_ORDERMANAGEMENT_PAYMENTSTATUS, "Payment Status"),
				new DataSourceTextField(DataNameTokens.RSV_ORDERMANAGEMENT_MOBILEPHONE, "Mobile Phone")
				};
		dataSourceFields[0].setPrimaryKey(true);
		
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + ReservationOrderManagementPresenter.reservationOrderManagementPresenterServlet + "?method=fetchReservationOrderManagementData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return retVal;
		}

}
