package com.gdn.venice.client.app.general.view;

import java.util.List;
import java.util.Map;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.general.presenter.OrderDataViewerPresenter;
import com.gdn.venice.client.app.general.view.handlers.OrderDataViewerUiHandlers;
import com.gdn.venice.client.app.general.widgets.OrderDetailContentLayout;
import com.gdn.venice.client.util.PrintUtility;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for Order Data Viewer
 *
 * @author Henry Chandra
 */
public class OrderDataViewerView extends ViewWithUiHandlers<OrderDataViewerUiHandlers> implements OrderDataViewerPresenter.MyView {

    private static final int LIST_HEIGHT = 200;
    RafViewLayout orderDataViewerLayout;
    VLayout orderListLayout;
    VLayout orderDetailLayout;
    OrderDetailContentLayout orderDetailContentLayout;
    ListGrid orderListGrid;
    ToolStripButton printButton;
    private ToolStripButton firstButton;
    private ToolStripButton nextButton;
    private ToolStripButton previousButton;
    private ToolStripButton lastButton;
    private final Label pageNumber;

    @Inject
    public OrderDataViewerView() {
        ToolStrip orderDataViewerToolStrip = new ToolStrip();
        orderDataViewerToolStrip.setWidth100();
        orderDataViewerToolStrip.setPadding(2);

        printButton = new ToolStripButton();
        printButton.setIcon("[SKIN]/icons/printer.png");
        printButton.setTooltip("Print Order Details");
        printButton.setTitle("Print");

        firstButton = new ToolStripButton("1");
        firstButton.setTooltip("Current page");
        firstButton.setDisabled(true);

        previousButton = new ToolStripButton("Prev");
        previousButton.setTooltip("Go to previous page");
        previousButton.setDisabled(true);

        nextButton = new ToolStripButton("Next");
        nextButton.setTooltip("Go to next page");

        lastButton = new ToolStripButton();
        lastButton.setTooltip("Go to last page");

        pageNumber = new Label(" 1 ");
        pageNumber.setTooltip("Current page");
        pageNumber.setVisible(false);

        orderDataViewerToolStrip.addButton(printButton);

//		orderDataViewerToolStrip.addSeparator();
//		
//		orderDataViewerToolStrip.addButton(previousButton);
//		orderDataViewerToolStrip.addButton(firstButton);
//		orderDataViewerToolStrip.addMember(pageNumber);
//		orderDataViewerToolStrip.addButton(lastButton);
//		orderDataViewerToolStrip.addButton(nextButton);

        orderDataViewerLayout = new RafViewLayout();

        orderListLayout = new VLayout();
        orderListLayout.setHeight(LIST_HEIGHT);
        orderListLayout.setShowResizeBar(true);

        orderDetailLayout = new VLayout();
        orderDetailLayout.setWidth100();

        HTMLFlow orderDetailFlow = new HTMLFlow();
        orderDetailFlow.setAlign(Alignment.CENTER);
        orderDetailFlow.setWidth100();
        orderDetailFlow.setContents("<h2 align=\"center\">Please select an order to show the order detail</h2>");
        orderDetailLayout.setMembers(orderDetailFlow);

        orderDataViewerLayout.setMembers(orderDataViewerToolStrip, orderListLayout, orderDetailLayout);
        buildOrderListGrid();
        bindCustomUiHandlers();
    }

    private ListGrid buildOrderListGrid() {
        orderListGrid = new ListGrid();
        orderListGrid.setWidth100();
        orderListGrid.setHeight100();
        orderListGrid.setShowAllRecords(true);
        orderListGrid.setSortField(0);
        orderListGrid.setCanResizeFields(true);
        orderListGrid.setShowRowNumbers(true);
        orderListGrid.setAutoFetchData(false);
        orderListGrid.setSelectionType(SelectionStyle.SIMPLE);
        orderListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        orderListGrid.setShowFilterEditor(true);

        return orderListGrid;
    }

    @Override
    public Widget asWidget() {
        return orderDataViewerLayout;
    }

    protected void bindCustomUiHandlers() {
        printButton.addClickHandler(new ClickHandler() {
            /* (non-Javadoc)
             * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
             */
            @Override
            public void onClick(ClickEvent event) {
                PrintUtility.printComponent(orderDataViewerLayout);
            }
        });

        orderListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
            @Override
            public void onSelectionChanged(SelectionEvent event) {
                ListGridRecord[] selectedRecords = event.getSelection();
                if (selectedRecords.length == 1) {
                    Record record = event.getRecord();
                    showOrderDetail(record);
                } else {
                    HTMLFlow orderDetailFlow = new HTMLFlow();
                    orderDetailFlow.setAlign(Alignment.CENTER);
                    orderDetailFlow.setWidth100();
                    if (selectedRecords.length == 0) {
                        orderDetailFlow.setContents("<h2 align=\"center\">Please select an order to show the order detail</h2>");
                    } else if (selectedRecords.length > 1) {
                        orderDetailFlow.setContents("<h2 align=\"center\">More than one order selected, please select only one order to show the order detail</h2>");
                    }
                    orderDetailLayout.setMembers(orderDetailFlow);
                }
            }
        });

        orderListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
            @Override
            public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
                refreshOrderData();
            }
        });

        firstButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
                goToPage(1);
            }
        });

        nextButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
                goToPage(Integer.parseInt(pageNumber.getContents().trim()) + 1);
            }
        });

        lastButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
                goToPage(Integer.parseInt(lastButton.getTitle()));
            }
        });

        previousButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
                goToPage(Integer.parseInt(pageNumber.getContents().trim()) - 1);
            }
        });
    }

    private void showOrderDetail(Record record) {
        String orderId = record.getAttributeAsString(DataNameTokens.VENORDER_ORDERID);
        final String orderStatus = record.getAttributeAsString(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSID);
        List<DataSource> dataSources = getUiHandlers().onShowOrderDetailData(orderId);

        orderDetailContentLayout = new OrderDetailContentLayout(
                dataSources.get(0), //Order Detail
                dataSources.get(1), //Order Item
                dataSources.get(2), //Order Customer
                dataSources.get(3), //Order Customer Address
                dataSources.get(4), //Order Customer Contact
                dataSources.get(5), //Order Logistics Airway Bill
                dataSources.get(6), //Order Finance Payment
                dataSources.get(7), //Order Finance Reconciliation
                dataSources.get(8), //Order History Order
                dataSources.get(9) //Order History Order Item
                );

        orderDetailContentLayout.getOrderDataOrderDetailTab().getButtonSuspiciousFraud().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RPCRequest request = new RPCRequest();
                request.setActionURL(GWT.getHostPageBaseURL() + "OrderDataViewerPresenterServlet?method=checkUserRole&roleNeeded=fraud&type=RPC");
                request.setHttpMethod("POST");
                request.setUseSimpleHttp(true);

                RPCManager.sendRequest(request, new RPCCallback() {
                    public void execute(RPCResponse response, Object rawData, RPCRequest request) {
                        String rpcResponse = rawData.toString().trim();
                        String message = "Are you sure you want to set the order status to SF?";
                        if (rpcResponse.equals("1")) {
                            SC.ask(message, new BooleanCallback() {
                                @Override
                                public void execute(Boolean value) {
                                    if (value != null && value == true) {
                                        getUiHandlers().onUpdateOrderStatus(orderDetailContentLayout.getOrderDataOrderDetailTab().getOrderId(), DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_SF.toString());
                                    }
                                }
                            });
                        } else {
                            SC.say("Only user with role fraud can update the order status.");
                            return;
                        }
                    }
                });
            }
        });

        orderDetailContentLayout.getOrderDataOrderDetailTab().getButtonFraudPassed().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RPCRequest request = new RPCRequest();
                request.setActionURL(GWT.getHostPageBaseURL() + "OrderDataViewerPresenterServlet?method=checkUserRole&roleNeeded=fraud&type=RPC");
                request.setHttpMethod("POST");
                request.setUseSimpleHttp(true);

                RPCManager.sendRequest(request, new RPCCallback() {
                    public void execute(RPCResponse response, Object rawData, RPCRequest request) {
                        String rpcResponse = rawData.toString().trim();
                        String message = "Are you sure you want to set the order status to FP?";
                        if (rpcResponse.equals("1")) {
                            SC.ask(message, new BooleanCallback() {
                                @Override
                                public void execute(Boolean value) {
                                    if (value != null && value == true) {
                                        getUiHandlers().onUpdateOrderStatus(orderDetailContentLayout.getOrderDataOrderDetailTab().getOrderId(), DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_FP.toString());
                                    }
                                }
                            });
                        } else {
                            SC.say("Only user with role fraud can update the order status.");
                            return;
                        }
                    }
                });
            }
        });

        orderDetailContentLayout.getOrderDataOrderDetailTab().getButtonFraudConfirmed().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RPCRequest request = new RPCRequest();
                request.setActionURL(GWT.getHostPageBaseURL() + "OrderDataViewerPresenterServlet?method=checkUserRole&roleNeeded=fraud&type=RPC");
                request.setHttpMethod("POST");
                request.setUseSimpleHttp(true);

                RPCManager.sendRequest(request, new RPCCallback() {
                    public void execute(RPCResponse response, Object rawData, RPCRequest request) {
                        String rpcResponse = rawData.toString().trim();
                        String message = "Are you sure you want to set the order status to FC?";
                        if (rpcResponse.equals("1")) {
                            SC.ask(message, new BooleanCallback() {
                                @Override
                                public void execute(Boolean value) {
                                    if (value != null && value == true) {
                                        getUiHandlers().onUpdateOrderStatus(orderDetailContentLayout.getOrderDataOrderDetailTab().getOrderId(), DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_FC.toString());
                                    }
                                }
                            });
                        } else {
                            SC.say("Only user with role fraud can update the order status.");
                            return;
                        }
                    }
                });
            }
        });

        orderDetailContentLayout.getOrderDataOrderDetailTab().getButtonBlock().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RPCRequest request = new RPCRequest();
                request.setActionURL(GWT.getHostPageBaseURL() + "OrderDataViewerPresenterServlet?method=checkUserRole&roleNeeded=fraud,finance&type=RPC");
                request.setHttpMethod("POST");
                request.setUseSimpleHttp(true);

                RPCManager.sendRequest(request, new RPCCallback() {
                    public void execute(RPCResponse response, Object rawData, RPCRequest request) {
                        String rpcResponse = rawData.toString().trim();

                        if (rpcResponse.equals("1")) {
                            final Window blockWindow = orderDetailContentLayout.getOrderDataOrderDetailTab().buildBlockWindow();
                            orderDetailContentLayout.getOrderDataOrderDetailTab().getButtonBlockOK().addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    String flag = orderDetailContentLayout.getOrderDataOrderDetailTab().getBlockFlagItem().getValueAsString().toUpperCase();
                                    String message = "";
                                    if (flag.equalsIgnoreCase("true")) {
                                        message = "Are you sure you want to block this order?";
                                    } else {
                                        message = "Are you sure you want to unblock this order?";
                                    }
                                    SC.ask(message, new BooleanCallback() {
                                        @Override
                                        public void execute(Boolean value) {
                                            if (value != null && value == true) {
                                                String orderId = orderDetailContentLayout.getOrderDataOrderDetailTab().getOrderId();
                                                String flag = orderDetailContentLayout.getOrderDataOrderDetailTab().getBlockFlagItem().getValueAsString().toUpperCase();
                                                String source = orderDetailContentLayout.getOrderDataOrderDetailTab().getBlockSourceItem().getValueAsString();
                                                if (source.equals("Fraud")) {
                                                    source = DataConstantNameTokens.VENORDER_BLOCKINGSOURCEDESC_FRD;
                                                } else if (source.equals("Finance")) {
                                                    source = DataConstantNameTokens.VENORDER_BLOCKINGSOURCEDESC_FIN;
                                                }
                                                String reason = orderDetailContentLayout.getOrderDataOrderDetailTab().getBlockReasonItem().getValueAsString();

                                                getUiHandlers().onBlockOrder(orderId, flag, source, reason, orderStatus);
                                                blockWindow.destroy();
                                            }
                                        }
                                    });
                                }
                            });
                            blockWindow.show();
                        } else {
                            SC.say("Only user with role fraud/finace can block/unblock order.");
                            return;
                        }
                    }
                });
            }
        });

        orderDetailContentLayout.getOrderDataFinanceTab().getOrderFinancePaymentListGrid().addCellClickHandler(new CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                String paymentId = event.getRecord().getAttributeAsString(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID);
                orderDetailContentLayout.getOrderDataFinanceTab().loadFinanceReconciliationData(getUiHandlers().onShowOrderFinanceReconciliationData(paymentId));
            }
        });

        orderDetailContentLayout.getOrderDataFinanceTab().getButtonApproveVA().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RPCRequest request = new RPCRequest();
                request.setActionURL(GWT.getHostPageBaseURL() + "OrderDataViewerPresenterServlet?method=checkUserRole&roleNeeded=finance&type=RPC");
                request.setHttpMethod("POST");
                request.setUseSimpleHttp(true);

                RPCManager.sendRequest(request, new RPCCallback() {
                    public void execute(RPCResponse response, Object rawData, RPCRequest request) {
                        String rpcResponse = rawData.toString().trim();
                        if (rpcResponse.equals("1")) {
                            String message = "Are you sure you want to approve this VA payment?";
                            SC.ask(message, new BooleanCallback() {
                                @Override
                                public void execute(Boolean value) {
                                    if (value != null && value == true) {
                                        String selectedPaymentId = orderDetailContentLayout.getOrderDataFinanceTab().getSelectedPaymentId();
                                        if (selectedPaymentId != null) {
                                            getUiHandlers().onApproveVAPayment(selectedPaymentId);
                                        }
                                    }
                                }
                            });
                        } else {
                            SC.say("Only user with role finance can approve VA.");
                            return;
                        }
                    }
                });
            }
        });

        orderDetailContentLayout.getOrderDataFinanceTab().getButtonApproveCS().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RPCRequest request = new RPCRequest();
                request.setActionURL(GWT.getHostPageBaseURL() + "OrderDataViewerPresenterServlet?method=checkUserRole&roleNeeded=finance&type=RPC");
                request.setHttpMethod("POST");
                request.setUseSimpleHttp(true);

                RPCManager.sendRequest(request, new RPCCallback() {
                    public void execute(RPCResponse response, Object rawData, RPCRequest request) {
                        String rpcResponse = rawData.toString().trim();
                        if (rpcResponse.equals("1")) {
                            String message = "Are you sure you want to approve this CS payment?";
                            SC.ask(message, new BooleanCallback() {
                                @Override
                                public void execute(Boolean value) {
                                    if (value != null && value == true) {
                                        String selectedPaymentId = orderDetailContentLayout.getOrderDataFinanceTab().getSelectedPaymentId();
                                        if (selectedPaymentId != null) {
                                            getUiHandlers().onApproveCSPayment(selectedPaymentId);
                                        }
                                    }
                                }
                            });
                        } else {
                            SC.say("Only user with role finance can approve CS.");
                            return;
                        }
                    }
                });
            }
        });

        orderDetailContentLayout.getOrderDataFinanceTab().getButtonPendingCS().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RPCRequest request = new RPCRequest();
                request.setActionURL(GWT.getHostPageBaseURL() + "OrderDataViewerPresenterServlet?method=checkUserRole&roleNeeded=finance&type=RPC");
                request.setHttpMethod("POST");
                request.setUseSimpleHttp(true);

                RPCManager.sendRequest(request, new RPCCallback() {
                    public void execute(RPCResponse response, Object rawData, RPCRequest request) {
                        String rpcResponse = rawData.toString().trim();
                        if (rpcResponse.equals("1")) {
                            String message = "Are you sure you want to pending this CS payment?";
                            SC.ask(message, new BooleanCallback() {
                                @Override
                                public void execute(Boolean value) {
                                    if (value != null && value == true) {
                                        String selectedPaymentId = orderDetailContentLayout.getOrderDataFinanceTab().getSelectedPaymentId();
                                        if (selectedPaymentId != null) {
                                            getUiHandlers().onPendingCSPayment(selectedPaymentId);
                                        }
                                    }
                                }
                            });
                        } else {
                            SC.say("Only user with role finance can pending CS.");
                            return;
                        }
                    }
                });
            }
        });

        orderDetailContentLayout.getOrderDataFinanceTab().getButtonRejectCS().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RPCRequest request = new RPCRequest();
                request.setActionURL(GWT.getHostPageBaseURL() + "OrderDataViewerPresenterServlet?method=checkUserRole&roleNeeded=finance&type=RPC");
                request.setHttpMethod("POST");
                request.setUseSimpleHttp(true);

                RPCManager.sendRequest(request, new RPCCallback() {
                    public void execute(RPCResponse response, Object rawData, RPCRequest request) {
                        String rpcResponse = rawData.toString().trim();
                        if (rpcResponse.equals("1")) {
                            String message = "Are you sure you want to reject this CS payment?";
                            SC.ask(message, new BooleanCallback() {
                                @Override
                                public void execute(Boolean value) {
                                    if (value != null && value == true) {
                                        String selectedPaymentId = orderDetailContentLayout.getOrderDataFinanceTab().getSelectedPaymentId();
                                        if (selectedPaymentId != null) {
                                            getUiHandlers().onRejectCSPayment(selectedPaymentId);
                                        }
                                    }
                                }
                            });
                        } else {
                            SC.say("Only user with role finance can reject CS.");
                            return;
                        }
                    }
                });
            }
        });

        orderDetailLayout.setMembers(orderDetailContentLayout);
    }

    @Override
    public void loadOrderData(DataSource dataSource, Map<String, String> status) {
        dataSource.getField(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSID).setValueMap(status);

        orderListGrid.setDataSource(dataSource);
        orderListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
        orderListGrid.getField(DataNameTokens.VENORDER_ORDERID).setWidth("10%");
        orderListGrid.getField(DataNameTokens.VENORDER_ORDERID).setHidden(true);
        orderListGrid.getField(DataNameTokens.VENORDER_WCSORDERID).setWidth("20%");
        orderListGrid.getField(DataNameTokens.VENORDER_ORDERDATE).setWidth("20%");
        orderListGrid.getField(DataNameTokens.VENORDER_VENCUSTOMER_CUSTOMERUSERNAME).setWidth("30%");
        orderListGrid.getField(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSID).setWidth("10%");
        orderListGrid.getField(DataNameTokens.VENORDER_FPDATE).setWidth("20%");
        orderListGrid.getField(DataNameTokens.VENORDER_BLOCKEDFLAG).setWidth("10%");
//		orderListGrid.getField(DataNameTokens.VENORDER_ORDERDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
//		orderListGrid.getField(DataNameTokens.VENORDER_FPDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        orderListGrid.getField(DataNameTokens.VENORDER_FPDATE).setCanFilter(false);

        orderListLayout.addMember(orderListGrid);
    }

    @Override
    public void refreshOrderData() {
        DSCallback callBack = new DSCallback() {
            @Override
            public void execute(DSResponse response, Object rawData, DSRequest request) {
                orderListGrid.setData(response.getData());
            }
        };
        orderListGrid.getDataSource().fetchData(orderListGrid.getFilterEditorCriteria(), callBack);
    }

    public void goToPage(int pageNum) {
        if (pageNum < 1) {
            pageNum = 1;
        }
        pageNumber.setContents(" " + pageNum + " ");
        updatePage(pageNum);
    }

    public void updatePage(int pageNum) {
        getUiHandlers().onFetchComboBoxData((pageNum - 1) * 50);

        if (pageNum == 1 && pageNum == Integer.parseInt(lastButton.getTitle())) {
            previousButton.setDisabled(true);
            firstButton.setDisabled(true);
            lastButton.setDisabled(true);
            nextButton.setDisabled(true);
            pageNumber.setVisible(false);
        } else if (pageNum == 1) {
            previousButton.setDisabled(true);
            firstButton.setDisabled(true);
            lastButton.setDisabled(false);
            nextButton.setDisabled(false);
            pageNumber.setVisible(false);
        } else {
            if (pageNum == Integer.parseInt(lastButton.getTitle())) {
                previousButton.setDisabled(false);
                firstButton.setDisabled(false);
                lastButton.setDisabled(true);
                nextButton.setDisabled(true);
                pageNumber.setVisible(false);
            } else {
                previousButton.setDisabled(false);
                firstButton.setDisabled(false);
                lastButton.setDisabled(false);
                nextButton.setDisabled(false);
                pageNumber.setVisible(true);
            }
        }
    }
//	@Override
//	public void setLastPage(int totalRows){
//		lastButton.setTitle(totalRows/50+1+"");
//		if(Integer.parseInt(lastButton.getTitle()) == 1)
//			lastButton.setVisible(false);
//		else 
//			lastButton.setVisible(true);
//	}	
}
