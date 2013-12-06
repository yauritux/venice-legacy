package com.gdn.venice.server.app.reservation.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.reservation.data.ReservationDto;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.google.gwt.user.server.Base64Utils;

public class FetchReservationOrderManagementDataCommand implements RafDsCommand {
	private RafDsRequest request;
	private ObjectMapper mapper = new ObjectMapper();
	private Properties properties = null;
	public FetchReservationOrderManagementDataCommand(RafDsRequest request){
		this.request = request;
		
		//initialize configuration
		String veniceHome = System.getenv("VENICE_HOME");
		String propertiesFileName = veniceHome + "/conf/reservation-order-management.properties";
		try {
			properties = new Properties();
			properties.load(new FileInputStream(new File(propertiesFileName)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;

		String[] fieldNames = getConfigValue("fieldNames").split(",");
		Arrays.sort(fieldNames);
		HttpURLConnection conn = null;
		try {
			StringBuilder parameterBuilder = new StringBuilder("?");
			if(request !=null && request.getCriteria()!=null){
				for(JPQLSimpleQueryCriteria param : request.getCriteria().getSimpleCriteria()){
					if(Arrays.binarySearch(fieldNames, param.getFieldName()) >= 0){
						parameterBuilder.append(param.getFieldName() ).append("=").append(param.getValue() ).append("&");
					}
				}
			} 
			String restURL = getConfigValue("restURL");
			if(parameterBuilder.length() > 0 ){
				restURL += parameterBuilder.toString();
			}
			System.out.println("restURL:" + restURL);
			//call rest URL using java.net.URL, simple implementation, if there is performance problem, use commons http clients instead
			URL url = new URL(restURL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			conn.setRequestProperty("Content-Type","text/xml; charset=UTF-8");
			
			conn.connect();
	 
			if (conn.getResponseCode() == 200) {
					BufferedReader br = new BufferedReader(new InputStreamReader(
							(conn.getInputStream())));
					String line;
					StringBuilder output = new StringBuilder();
					System.out.println("Output from Server .... \n");
					while ((line= br.readLine()) != null) {
							output.append(line);
					}
					
					ReservationDto[] reservationDtos = mapper.readValue(output.toString(), ReservationDto[].class);
					//Put result
					for (int i = 0; reservationDtos!=null && i < reservationDtos.length; i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						ReservationDto reservationDto = reservationDtos[i];
		
						map.put(DataNameTokens.RSV_ORDERMANAGEMENT_WCSORDERID, reservationDto.getWcsOrderId()!=null ? reservationDto.getWcsOrderId() : "");
						map.put(DataNameTokens.RSV_ORDERMANAGEMENT_RESERVATIONID, reservationDto.getReservationId()!=null ? reservationDto.getReservationId() : "");
						map.put(DataNameTokens.RSV_ORDERMANAGEMENT_EMAILADDRESS, reservationDto.getEmailAddress()!=null  ? reservationDto.getEmailAddress() : "");
						map.put(DataNameTokens.RSV_ORDERMANAGEMENT_ORDERDATE, reservationDto.getOrderDate()!=null ? reservationDto.getOrderDate() : "");
						map.put(DataNameTokens.RSV_ORDERMANAGEMENT_FIRSTNAME, reservationDto.getFirstName()!=null ? reservationDto.getFirstName() : "");
						map.put(DataNameTokens.RSV_ORDERMANAGEMENT_LASTNAME, reservationDto.getLastName()!=null  ? reservationDto.getLastName() : "");
						map.put(DataNameTokens.RSV_ORDERMANAGEMENT_PAYMENTSTATUS, reservationDto.getPaymentStatus()!=null  ? reservationDto.getPaymentStatus(): "");
						map.put(DataNameTokens.RSV_ORDERMANAGEMENT_MOBILEPHONE, reservationDto.getMobilePhone()!=null  ? reservationDto.getMobilePhone() : "");
						dataList.add(map);
					}
					
					//Set DSResponse's properties
					rafDsResponse.setStatus(0);
					rafDsResponse.setStartRow(request.getStartRow());
					rafDsResponse.setTotalRows(reservationDtos.length);
					rafDsResponse.setEndRow(request.getStartRow() + dataList.size());
					
			}	 else {
				System.err.println("Failed : HTTP error code : " + conn.getResponseCode());
				rafDsResponse.setStatus(-1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if (locator != null) {
					locator.close();
				}
				if(conn != null){
					conn.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//Set data and return
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
	
	private String getConfigValue(String propertyName){
			if(properties ==null){
				return null;
			}
			return properties.getProperty(propertyName);
	}
	
}