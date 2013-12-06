package com.gdn.venice.client.app.finance.presenter;

import java.util.ArrayList;
import java.util.HashMap;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.finance.data.FinanceData;
import com.gdn.venice.client.app.finance.view.handlers.PaymentProcessingUiHandlers;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.PromptStyle;
import com.smartgwt.client.util.SC;

/**
 * Presenter for Payment Processing that includes all payment types etc. <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a> <p>
 * <b>version:</b> 1.0 <p> <b>since:</b> 2011
 *
 */
public class PaymentProcessingPresenter
        extends Presenter<PaymentProcessingPresenter.MyView, PaymentProcessingPresenter.MyProxy>
        implements PaymentProcessingUiHandlers {

    @SuppressWarnings("unused")
    private final DispatchAsync dispatcher;
    /*
     * The servlet that gets called by the payment processing presenter
     */
    public final static String paymentProcessingPresenterServlet = "PaymentProcessingPresenterServlet";

    /**
     * {@link PaymentProcessingPresenter}'s proxy.
     */
    @ProxyCodeSplit
    @NameToken(NameTokens.financePaymentProcessingPage)
    public interface MyProxy extends Proxy<PaymentProcessingPresenter>, Place {
    }

    /**
     * {@link PaymentProcessingPresenter}'s view.
     */
    public interface MyView extends View,
            HasUiHandlers<PaymentProcessingUiHandlers> {

        public void loadMerchantPaymentProcessingData(DataSource dataSource);

        public void loadLogisticsPaymentProcessingData(DataSource dataSource);

        public void loadRefundPaymentProcessingData(DataSource dataSource);

        public void refreshMerchantPaymentProcessingData();

        public void refreshLogisticsPaymentProcessingData();

        public void refreshRefundPaymentProcessingData();

        public void refreshPaymentData();

        public void loadPaymentDataForPaymentProcessing(DataSource dataSource, String paymentData);

        public void loadPaymentData(DataSource dataSource);
    }

    /**
     * Injects the payment processing presenter into the view
     *
     * @param eventBus
     * @param view
     * @param proxy
     * @param dispatcher
     */
    @Inject
    public PaymentProcessingPresenter(EventBus eventBus, MyView view,
            MyProxy proxy, DispatchAsync dispatcher) {
        super(eventBus, view, proxy);
        getView().setUiHandlers(this);
        ((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
        getView().loadMerchantPaymentProcessingData(FinanceData.getMerchantPaymentDetailData());
        getView().loadLogisticsPaymentProcessingData(FinanceData.getLogisticsPaymentDetailData());
        getView().loadRefundPaymentProcessingData(FinanceData.getRefundPaymentDetailData());
        getView().loadPaymentData(FinanceData.getPaymentData());
        this.dispatcher = dispatcher;
    }

    /* (non-Javadoc)
     * @see com.gwtplatform.mvp.client.Presenter#revealInParent()
     */
    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,
                this);
    }

    /* (non-Javadoc)
     * @see com.gdn.venice.client.app.finance.view.handlers.PaymentProcessingUiHandlers#onMakePaymentButtonClicked(java.util.HashMap)
     */
    @Override
    public void onMakePaymentButtonClicked(final HashMap<String, String> paymentDataMap) {
        RPCRequest request = new RPCRequest();

        request.setData(Util.formXMLfromHashMap(paymentDataMap));

        request.setActionURL(GWT.getHostPageBaseURL() + paymentProcessingPresenterServlet + "?method=makePayment&type=RPC");
        request.setHttpMethod("POST");
        request.setUseSimpleHttp(true);
        RPCManager.setPromptStyle(PromptStyle.DIALOG);
        RPCManager.setDefaultPrompt("Making payments...");
        RPCManager.setShowPrompt(true);

        RPCManager.sendRequest(request,
                new RPCCallback() {
                    /* (non-Javadoc)
                     * @see com.smartgwt.client.rpc.RPCCallback#execute(com.smartgwt.client.rpc.RPCResponse, java.lang.Object, com.smartgwt.client.rpc.RPCRequest)
                     */
                    public void execute(RPCResponse response,
                            Object rawData, RPCRequest request) {
                        String rpcResponse = rawData.toString();

                        if (rpcResponse.startsWith("0")) {
                            SC.say(DataMessageTokens.PAYMENT_CREATED);
                            String paymentTo = paymentDataMap.get("PAYMENTTO");

                            if (paymentTo.equals("MERCHANT")) {
                                getView().refreshMerchantPaymentProcessingData();
                            } else if (paymentTo.equals("LOGISTICS")) {
                                getView().refreshLogisticsPaymentProcessingData();
                            } else if (paymentTo.equals("REFUND")) {
                                getView().refreshRefundPaymentProcessingData();
                            }
                        } else {
                            /*
                             * Use the 2nd positional split on ":" as the error message
                             */
                            String[] split = rpcResponse.split(":");
                            if (split.length > 1) {
                                SC.warn(split[1]);
                            } else {
                                SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
                            }
                        }
                        RPCManager.setDefaultPrompt("Retrieving records...");
                        RPCManager.setShowPrompt(false);
                    }
                });
    }

    /* (non-Javadoc)
     * @see com.gdn.venice.client.app.finance.view.handlers.PaymentProcessingUiHandlers#onProcessPaymentButtonClicked(com.smartgwt.client.data.DataSource, java.lang.String)
     */
    @Override
    public void onProcessPaymentButtonClicked(final DataSource dataSource, final String paymentData) {
        RPCRequest request = new RPCRequest();

        request.setActionURL(GWT.getHostPageBaseURL() + paymentProcessingPresenterServlet + "?method=fetchBankAccountComboBoxData&type=RPC");
        request.setHttpMethod("POST");
        request.setUseSimpleHttp(true);
        RPCManager.setPromptStyle(PromptStyle.DIALOG);
        RPCManager.setDefaultPrompt("Processing payments...");
        RPCManager.setShowPrompt(true);

        RPCManager.sendRequest(request,
                new RPCCallback() {
                    /* (non-Javadoc)
                     * @see com.smartgwt.client.rpc.RPCCallback#execute(com.smartgwt.client.rpc.RPCResponse, java.lang.Object, com.smartgwt.client.rpc.RPCRequest)
                     */
                    public void execute(RPCResponse response,
                            Object rawData, RPCRequest request) {
                        String rpcResponse = rawData.toString();

                        String xmlData = rpcResponse;
                        dataSource.getField(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTID).setValueMap(Util.formComboBoxMap(Util.formHashMapfromXML(xmlData)));

                        getView().loadPaymentDataForPaymentProcessing(dataSource, paymentData);
                        RPCManager.setDefaultPrompt("Retrieving records...");
                        RPCManager.setShowPrompt(false);
                    }
                });
    }

    /* (non-Javadoc)
     * @see com.gdn.venice.client.app.finance.view.handlers.PaymentProcessingUiHandlers#onSubmitForApproval(java.util.ArrayList)
     */
    @Override
    public void onSubmitForApproval(ArrayList<String> apPaymentIdList) {
        /*
         * Use RPC style to make the call to submit the payments for approval
         */
        RPCRequest request = new RPCRequest();

        /*
         * Extract the payment ids from the 
         */
        HashMap<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < apPaymentIdList.size(); i++) {
            map.put(ProcessNameTokens.APPAYMENTID + (i + 1), apPaymentIdList.get(i));
        }
        String apPaymentIds = map.toString();

        request.setData(apPaymentIds);

        request.setActionURL(GWT.getHostPageBaseURL() + paymentProcessingPresenterServlet + "?method=submitApPaymentForApproval&type=RPC");
        request.setHttpMethod("POST");
        request.setUseSimpleHttp(true);
        request.setWillHandleError(true);
        RPCManager.setPromptStyle(PromptStyle.DIALOG);
        RPCManager.setDefaultPrompt("Submitting payments for approval...");
        RPCManager.setShowPrompt(true);

        RPCManager.sendRequest(request,
                new RPCCallback() {
                    /* (non-Javadoc)
                     * @see com.smartgwt.client.rpc.RPCCallback#execute(com.smartgwt.client.rpc.RPCResponse, java.lang.Object, com.smartgwt.client.rpc.RPCRequest)
                     */
                    public void execute(RPCResponse response,
                            Object rawData, RPCRequest request) {
                        String rpcResponse = rawData.toString();
                        if (rpcResponse.startsWith("0")) {
                            getView().refreshPaymentData();
                            SC.say(DataMessageTokens.SUBMITTED_FOR_APPROVAL);
                        } else {
                            /*
                             * Use the 2nd positional split on ":" as the error message
                             */
                            String[] split = rpcResponse.split(":");
                            if (split.length > 1) {
                                SC.warn(split[1]);
                            } else {
                                SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
                            }
                        }
                        RPCManager.setDefaultPrompt("Retrieving records...");
                        RPCManager.setShowPrompt(false);
                    }
                });

    }

    @Override
    public void onDoneButtonClicked(ArrayList<String> apPaymentIdList) {
        /*
         * Use RPC style to make the call to finish payment
         */
        RPCRequest request = new RPCRequest();

        /*
         * Extract the payment ids from the 
         */

        String apPaymentIds = "";
        for (int i = 0; i < apPaymentIdList.size(); i++) {
            apPaymentIds += apPaymentIdList.get(i);
            if (i < apPaymentIdList.size() - 1) {
                apPaymentIds += ",";
            }
        }

        request.setData(apPaymentIds);

        request.setActionURL(GWT.getHostPageBaseURL() + paymentProcessingPresenterServlet + "?method=finishPaymentProcessing&type=RPC");
        request.setHttpMethod("POST");
        request.setUseSimpleHttp(true);
        request.setWillHandleError(true);
        RPCManager.setPromptStyle(PromptStyle.DIALOG);
        RPCManager.setDefaultPrompt("Finishing payments and creating journal...");
        RPCManager.setShowPrompt(true);

        RPCManager.sendRequest(request,
                new RPCCallback() {
                    /* (non-Javadoc)
                     * @see com.smartgwt.client.rpc.RPCCallback#execute(com.smartgwt.client.rpc.RPCResponse, java.lang.Object, com.smartgwt.client.rpc.RPCRequest)
                     */
                    public void execute(RPCResponse response,
                            Object rawData, RPCRequest request) {
                        String rpcResponse = rawData.toString();
                        if (rpcResponse.startsWith("0")) {
                            getView().refreshPaymentData();
                            SC.say(DataMessageTokens.PAYMENT_FINISHED);
                        } else {
                            /*
                             * Use the 2nd positional split on ":" as the error message
                             */
                            String[] split = rpcResponse.split(":");
                            if (split.length > 1) {
                                SC.warn(split[1]);
                            } else {
                                SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
                            }
                        }
                        RPCManager.setDefaultPrompt("Retrieving records...");
                        RPCManager.setShowPrompt(false);
                    }
                });
    }

    @Override
    public void onExportButtonClicked(ArrayList<String> apPaymentIdList) {
        String apPaymentIds = "";
        for (int i = 0; i < apPaymentIdList.size(); i++) {
            apPaymentIds += apPaymentIdList.get(i);
            if (i < apPaymentIdList.size() - 1) {
                apPaymentIds += ",";
            }
        }

        String host = GWT.getHostPageBaseURL();

        //If in debug mode then change the host URL to the servlet in the server side
        if (host.contains("8889")) {
            host = "http://localhost:8090/";
        }

        /* 
         * Somehow when the app is deployed in Geronimo the getHostPageBaseURL call
         * adds the context root of "Venice/" as it is the web application.
         * This does not happen in development mode as it is running in the root
         * of the Jetty servlet container.
         * 
         * Consequently the context root needs to be removed because the servlet 
         * being called has its own context root in a different web application.
         */
        if (host.contains("Venice/")) {
            host = host.substring(0, host.indexOf("Venice/"));
        }

        Window.open(host + "Venice/MerchantPaymentReportLauncherServlet?apPaymentIds=" + apPaymentIds, "_blank", null);
    }
}
