package com.gdn.venice.client.view;

import com.gdn.venice.client.presenter.LoginPresenter;
import com.gdn.venice.client.view.handlers.LoginUiHandlers;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 
 * @author Henry Chandra
 *
 */
public class LoginView extends ViewWithUiHandlers<LoginUiHandlers> implements LoginPresenter.MyView {

	VLayout loginLayout;
	
	ImgButton applicationImage;
	
	TextItem userItem;
	PasswordItem passwordItem;

	@Inject
	public LoginView() {

		loginLayout = new VLayout();
		
//		loginLayout.setBackgroundImage("loginBackgroundOverall.jpg");
//		loginLayout.setBackgroundRepeat(BkgndRepeat.NO_REPEAT);
//		loginLayout.setBackgroundPosition("center");

		loginLayout.setWidth100();
		loginLayout.setHeight100();

		final Window loginWindow = new Window();
		

		loginWindow.setWidth(380);
		loginWindow.setHeight(130);
		loginWindow.setTitle("Login to Application");
		loginWindow.setShowMinimizeButton(false);
		loginWindow.setShowMaximizeButton(false);
		loginWindow.setShowCloseButton(false);
		loginWindow.setIsModal(true);
		loginWindow.setShowModalMask(true);
		loginWindow.centerInPage();

		HLayout loginHLayout = new HLayout();
		loginHLayout.setMembersMargin(10);

		loginHLayout.setAlign(VerticalAlignment.BOTTOM);

		DynamicForm loginForm = new DynamicForm();
		loginForm.setAction("/MainPagePresenterServlet");

		userItem = new TextItem();
		userItem.setTitle("User");

		passwordItem = new PasswordItem();
		passwordItem.setTitle("Password");
		
		SelectItem applicationSelectItem = new SelectItem();
		applicationSelectItem.setValueMap("Venice");
		applicationSelectItem.setTitle("Application");
		
		applicationSelectItem.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				applicationImage.setVisible(true);
				applicationImage.setSrc("VeniceSmall.png");
			}
		});
		
		loginForm.setItems(userItem, passwordItem, applicationSelectItem);

		VLayout applicationImageLayout = new VLayout();
		applicationImageLayout.setWidth(103);
		applicationImageLayout.setHeight(58);
		
		applicationImage = new ImgButton();
		applicationImage.setVisible(false);
		applicationImage.setSrc("VeniceSmall.png");
		applicationImage.setWidth(103);
		applicationImage.setHeight(58);
		
		applicationImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().onLogin(userItem.getValueAsString(), passwordItem.getValueAsString());
				loginWindow.destroy();
			}
		});
		
		applicationImageLayout.setMembers(applicationImage);
		
		loginHLayout.setPadding(10);		
		loginHLayout.setMembers(loginForm, applicationImageLayout);

		loginWindow.addItem(loginHLayout);

		loginWindow.show();



		bindCustomUiHandlers();
	}



	@Override
	public Widget asWidget() {
		return loginLayout;
	}

	protected void bindCustomUiHandlers() {

	}



}

