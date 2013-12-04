package com.gdn.venice.constants;

/**
 * 
 * @author yauritux
 *
 */
public enum VenOrderStatusConstants {
	
	 VEN_ORDER_STATUS_VA(0),
	 VEN_ORDER_STATUS_C(1),
	 VEN_ORDER_STATUS_SF(2),
	 VEN_ORDER_STATUS_FC(3),
	 VEN_ORDER_STATUS_FP(4),
	 VEN_ORDER_STATUS_D(5),
	 VEN_ORDER_STATUS_X(6),
	 VEN_ORDER_STATUS_RV(7),
	 VEN_ORDER_STATUS_PU(8),
	 VEN_ORDER_STATUS_BP(9),
	 VEN_ORDER_STATUS_ES(10),
	 VEN_ORDER_STATUS_RM(11),
	 VEN_ORDER_STATUS_RL(12),
	 VEN_ORDER_STATUS_PP(13),
	 VEN_ORDER_STATUS_PF(14),
	 VEN_ORDER_STATUS_RT(15),
	 VEN_ORDER_STATUS_CX(16),
	 VEN_ORDER_STATUS_OS(17),
	 VEN_ORDER_STATUS_CS(18),
	 VEN_ORDER_STATUS_B(19),
	 VEN_ORDER_STATUS_R(20),
	 VEN_ORDER_STATUS_RF(21),
	 VEN_ORDER_STATUS_TS(22);
	 
	 private long code;
	 
	 private VenOrderStatusConstants(long code) {
		 this.code = code;
	 }
	 
	 public long code() {
		 return code;
	 }
}
