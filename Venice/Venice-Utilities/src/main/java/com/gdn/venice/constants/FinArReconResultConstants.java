package com.gdn.venice.constants;

/**
 * 
 * @author yauritux
 *
 */
public enum FinArReconResultConstants {
	
	 FIN_AR_RECON_RESULT_ALL(0),
	 FIN_AR_RECON_RESULT_PARTIAL(1),
	 FIN_AR_RECON_RESULT_OVERPAID(2),
	 FIN_AR_RECON_RESULT_TIMEOUT(3),
	 FIN_AR_RECON_RESULT_NONE(4),
	 FIN_AR_RECON_RESULT_REFUNDED(5),
	 FIN_AR_RECON_RESULT_NOT_RECOGNIZED(6),
	 FIN_AR_RECON_RESULT_ALLOCATE_TO_ORDER(7);
	 
	 private long id;
	 
	 public long id() {
		 return id;
	 }
	 
	 private FinArReconResultConstants(long id) {
		 this.id = id;
	 }
}
