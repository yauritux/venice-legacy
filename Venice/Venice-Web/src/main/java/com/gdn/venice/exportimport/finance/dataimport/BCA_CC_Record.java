package com.gdn.venice.exportimport.finance.dataimport;
import com.gdn.venice.hssf.PojoInterface;

/**
 * Pojo class for importing the MIGS report from the Excel spreadsheet.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class BCA_CC_Record implements PojoInterface{
	private String kdGrp;
	private String mercNo;
	private String termId;
	private String transDate;
	private String transTime;
	private String batchPtlf;
	private String seq;
	private String authCd;
	private String cardNo;
	private String grossAmt;
	private String discRate;
	private String discAmt;
	private String nettAmt;
	private String authAmt;
	private String reward;
	private String redeem;
	private String tranCode;
	private String dbaName;
	/**
	 * @return the kdGrp
	 */
	public String getKdGrp() {
		return kdGrp;
	}
	/**
	 * @param kdGrp the kdGrp to set
	 */
	public void setKdGrp(String kdGrp) {
		this.kdGrp = kdGrp;
	}
	/**
	 * @return the mercNo
	 */
	public String getMercNo() {
		return mercNo;
	}
	/**
	 * @param mercNo the mercNo to set
	 */
	public void setMercNo(String mercNo) {
		this.mercNo = mercNo;
	}
	/**
	 * @return the termId
	 */
	public String getTermId() {
		return termId;
	}
	/**
	 * @param termId the termId to set
	 */
	public void setTermId(String termId) {
		this.termId = termId;
	}
	/**
	 * @return the transDate
	 */
	public String getTransDate() {
		return transDate;
	}
	/**
	 * @param transDate the transDate to set
	 */
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	/**
	 * @return the batchPtlf
	 */
	public String getBatchPtlf() {
		return batchPtlf;
	}
	/**
	 * @param batchPtlf the batchPtlf to set
	 */
	public void setBatchPtlf(String batchPtlf) {
		this.batchPtlf = batchPtlf;
	}
	/**
	 * @return the seq
	 */
	public String getSeq() {
		return seq;
	}
	/**
	 * @param seq the seq to set
	 */
	public void setSeq(String seq) {
		this.seq = seq;
	}
	/**
	 * @return the authCd
	 */
	public String getAuthCd() {
		return authCd;
	}
	/**
	 * @param authCd the authCd to set
	 */
	public void setAuthCd(String authCd) {
		this.authCd = authCd;
	}
	/**
	 * @return the cardNo
	 */
	public String getCardNo() {
		return cardNo;
	}
	/**
	 * @param cardNo the cardNo to set
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	/**
	 * @return the grossAmt
	 */
	public String getGrossAmt() {
		return grossAmt;
	}
	/**
	 * @param grossAmt the grossAmt to set
	 */
	public void setGrossAmt(String grossAmt) {
		this.grossAmt = grossAmt;
	}
	/**
	 * @return the discRate
	 */
	public String getDiscRate() {
		return discRate;
	}
	/**
	 * @param discRate the discRate to set
	 */
	public void setDiscRate(String discRate) {
		this.discRate = discRate;
	}
	/**
	 * @return the discAmt
	 */
	public String getDiscAmt() {
		return discAmt;
	}
	/**
	 * @param discAmt the discAmt to set
	 */
	public void setDiscAmt(String discAmt) {
		this.discAmt = discAmt;
	}
	/**
	 * @return the nettAmt
	 */
	public String getNettAmt() {
		return nettAmt;
	}
	/**
	 * @param nettAmt the nettAmt to set
	 */
	public void setNettAmt(String nettAmt) {
		this.nettAmt = nettAmt;
	}
	/**
	 * @return the authAmt
	 */
	public String getAuthAmt() {
		return authAmt;
	}
	/**
	 * @param authAmt the authAmt to set
	 */
	public void setAuthAmt(String authAmt) {
		this.authAmt = authAmt;
	}
	/**
	 * @return the reward
	 */
	public String getReward() {
		return reward;
	}
	/**
	 * @param reward the reward to set
	 */
	public void setReward(String reward) {
		this.reward = reward;
	}
	/**
	 * @return the redeem
	 */
	public String getRedeem() {
		return redeem;
	}
	/**
	 * @param redeem the redeem to set
	 */
	public void setRedeem(String redeem) {
		this.redeem = redeem;
	}
	/**
	 * @return the tranCode
	 */
	public String getTranCode() {
		return tranCode;
	}
	/**
	 * @param tranCode the tranCode to set
	 */
	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}
	/**
	 * @return the dbaName
	 */
	public String getDbaName() {
		return dbaName;
	}
	/**
	 * @param dbaName the dbaName to set
	 */
	public void setDbaName(String dbaName) {
		this.dbaName = dbaName;
	}
	/**
	 * @return the transTime
	 */
	public String getTransTime() {
		return transTime;
	}
	/**
	 * @param transTime the transTime to set
	 */
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

}
