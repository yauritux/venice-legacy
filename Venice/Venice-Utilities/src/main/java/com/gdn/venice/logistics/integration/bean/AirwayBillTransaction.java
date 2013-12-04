package com.gdn.venice.logistics.integration.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class AirwayBillTransaction implements Serializable {
	/**
	 * Field name constant for airwaybill engine data	
	 */
	public static final String GDN_REF = "gdnRef";
	public static final String ORDER_ID = "orderId";
	public static final String ORDER_ITEM_ID = "orderItemId";
	public static final String AIRWAY_BILL_NO = "airwayBillNo";
	public static final String KODE_ORIGIN = "kodeOrigin";
	public static final String KODE_DESTINATION = "kodeDestination";
	public static final String ACCOUNT_NUMBER = "accountNumber";
	public static final String NAMA_PRODUK = "namaProduk";
	public static final String HARGA_PRODUK = "hargaProduk";
	public static final String QTY_PRODUK = "qtyProduk";
	public static final String WEIGHT = "weight";
	public static final String BERAT_CETAK = "beratCetak";
	public static final String PRICE_PER_KG = "pricePerKg";
	public static final String FIXED_PRICE = "fixedPrice";
	public static final String INSURANCE_PERCENTAGE = "insurancePercentage";
	public static final String GIFT_WRAP = "giftWrap";
	public static final String SHIPPING_COST = "shippingCost";
	public static final String INSURANCE_COST = "insuranceCost";
	public static final String AIRWAYBILL_INSURANCE_COST = "airwayBillInsuranceCost";
	public static final String AIRWAYBILL_WEIGHT = "airwayBillWeight";
	public static final String AIRWAYBILL_FIXED_PRICE = "airwayBillFixedPrice";
	public static final String AIRWAYBILL_GIFT_WRAP = "airwayBillGiftWrap";
	public static final String AIRWAYBILL_SHIPPING_COST = "airwayBillShippingCost";
	public static final String NAMA_PENGIRIM = "namaPengirim";
	public static final String ALAMAT_PENGIRIM = "alamatPengirim";
	public static final String KELURAHAN_KECAMATAN_PENGIRIM = "kelurahanKecamatanPengirim";
	public static final String KOTA_KABUPATEN_PENGIRIM = "kotaKabupatenPengirim";
	public static final String PROPINSI_PENGIRIM = "propinsiPengirim";
	public static final String KODEPOS_PENGIRIM = "kodeposPengirim";
	public static final String PIC_PENGIRIM = "picPengirim";
	public static final String NO_HP_PIC_PENGIRIM = "noHPPicPengirim";
	public static final String WCS_FULFILLMENT_ID = "wcsFulfillmentId";
	public static final String RECIPIENT = "recipient";
	public static final String RELATION = "relation";
	public static final String RECEIVED = "received";
	public static final String NAMA_PENERIMA = "namaPenerima";
	public static final String ALAMAT_PENERIMA_1 = "alamatPenerima1";
	public static final String ALAMAT_PENERIMA_2 = "alamatPenerima2";
	public static final String KELURAHAN_KECAMATAN_PENERIMA = "kelurahanKecamatanPenerima";
	public static final String KOTA_KABUPATEN_PENERIMA = "kotaKabupatenPenerima";
	public static final String PROPINSI_PENERIMA = "propinsiPenerima";
	public static final String KODEPOS_PENERIMA = "kodeposPenerima";
	public static final String PIC_PENERIMA = "picPenerima";
	public static final String NO_HP_PIC_PENERIMA = "noHPPicPenerima";
	public static final String INSTRUKSI_PENGIRIMAN = "instruksiPengiriman";
	public static final String KODE_LOGISTIK = "kodeLogistik";
	public static final String SERVICE_TYPE = "serviceType";
	public static final String TANGGAL_PICKUP = "tanggalPickup";
	public static final String TANGGAL_ACTUAL_PICKUP = "tanggalActualPickup";
	public static final String LEVEL = "level";
	public static final String LAST_STATUS_ES = "lastStatusES";
	public static final String STATUS = "status";
	public static final String AIRWAYBILL_PENGGANTI = "airwayBillPengganti";
	public static final String INVOICE_APPROVED = "invoiceApproved";
	public static final String PENANGGUNG_BIAYA = "penanggungBiaya";
	public static final String CREATED_DATE = "createdDate";
	public static final String CREATED_BY = "createdBy";
	public static final String VOID_DATE = "voidDate";
	public static final String VOID_BY = "voidBy";
	public static final String COMMENT = "comment";
	public static final String LAST_PRINTED_DATE = "lastPrintedDate";
	public static final String LAST_PRINTED_BY = "lastPrintedBy";
	public static final String PRINTED_COUNT = "printedCount";
	
	/**
	 * AWB status
	 */
	public static final String STATUS_ASSIGNED = "B";
	public static final String STATUS_PICK_UP = "PU";
	public static final String STATUS_EMAIL_SENT = "ES";
	public static final String STATUS_SETTLED = "CX";
	public static final String STATUS_CLOSED = "D";
	public static final String STATUS_CANCEL = "X";
	
	/**
	 * AWB level
	 */
	public static final String LEVEL_ORDER_MAIN = "OM";
	public static final String LEVEL_ORDER_REFERENCE = "OR";
	public static final String LEVEL_RETUR_MAIN = "RM";
	public static final String LEVEL_RETUR_REFERENCE = "RR";
	
	
	private String gdnRef;
	private String orderId;
	private String orderItemId;
	private String airwayBillNo;
	private String kodeOrigin;
	private String kodeDestination;
	private String accountNumber;
	private String namaProduk;
	private Double hargaProduk;
	private int qtyProduk;
	private Double weight;
	private Double beratCetak;
	private BigDecimal pricePerKg;
	private BigDecimal fixedPrice;
	private Double insurancePercentage;
	private BigDecimal giftWrap;
	private BigDecimal shippingCost;
	private BigDecimal insuranceCost;
	private BigDecimal airwaybillInsuranceCost;
	private Double airwaybillWeight;
	private BigDecimal airwaybillFixedPrice;
	private BigDecimal airwaybillGiftWrap;
	private BigDecimal airwaybillShippingCost;
	private String namaPengirim;
	private String alamatPengirim;
	private String kelurahanKecamatanPengirim;
	private String kotaKabupatenPengirim;
	private String propinsiPengirim;
	private String kodeposPengirim;
	private String picPengirim;
	private String noHPPicPengirim;
	private String wcsFulfillmentId;
	private String recipient;
	private String relation;
	private Timestamp received;
	private String namaPenerima;
	private String alamatPenerima1;
	private String alamatPenerima2;
	private String kelurahanKecamatanPenerima;
	private String kotaKabupatenPenerima;
	private String propinsiPenerima;
	private String kodeposPenerima;
	private String picPenerima;
	private String noHPPicPenerima;
	private String instruksiPengiriman;
	private String kodeLogistik;
	private String serviceType;
	private Timestamp tanggalPickup;
	private Timestamp tanggalActualPickup;
	private String level;
	private boolean lastStatusES;
	private String status;
	private String airwayBillPengganti;
	private boolean invoiceApproved;
	private String penanggungBiaya;
	private Timestamp createdDate;
	private String createdBy;
	private Timestamp voidDate;
	private String voidBy;
	private String comment;
	private Timestamp lastPrintedDate;
	private String lastPrintedBy;
	private int printedCount;
		
	
	public String getGdnRef() {
		return gdnRef;
	}
	public void setGdnRef(String gdnRef) {
		this.gdnRef = gdnRef;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getAirwayBillNo() {
		return airwayBillNo;
	}
	public void setAirwayBillNo(String airwayBillNo) {
		this.airwayBillNo = airwayBillNo;
	}
	public String getKodeOrigin() {
		return kodeOrigin;
	}
	public void setKodeOrigin(String kodeOrigin) {
		this.kodeOrigin = kodeOrigin;
	}
	public String getKodeDestination() {
		return kodeDestination;
	}
	public void setKodeDestination(String kodeDestination) {
		this.kodeDestination = kodeDestination;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getNamaProduk() {
		return namaProduk;
	}
	public void setNamaProduk(String namaProduk) {
		this.namaProduk = namaProduk;
	}
	public Double getHargaProduk() {
		return hargaProduk;
	}
	public void setHargaProduk(Double hargaProduk) {
		this.hargaProduk = hargaProduk;
	}
	public int getQtyProduk() {
		return qtyProduk;
	}
	public void setQtyProduk(int qtyProduk) {
		this.qtyProduk = qtyProduk;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getBeratCetak() {
		return beratCetak;
	}
	public void setBeratCetak(Double beratCetak) {
		this.beratCetak = beratCetak;
	}
	public BigDecimal getPricePerKg() {
		return pricePerKg;
	}
	public void setPricePerKg(BigDecimal pricePerKg) {
		this.pricePerKg = pricePerKg;
	}
	public BigDecimal getFixedPrice() {
		return fixedPrice;
	}
	public void setFixedPrice(BigDecimal fixedPrice) {
		this.fixedPrice = fixedPrice;
	}
	public Double getInsurancePercentage() {
		return insurancePercentage;
	}
	public void setInsurancePercentage(Double insurancePercentage) {
		this.insurancePercentage = insurancePercentage;
	}
	public BigDecimal getGiftWrap() {
		return giftWrap;
	}
	public void setGiftWrap(BigDecimal giftWrap) {
		this.giftWrap = giftWrap;
	}
	public BigDecimal getShippingCost() {
		return shippingCost;
	}
	public void setShippingCost(BigDecimal shippingCost) {
		this.shippingCost = shippingCost;
	}
	public BigDecimal getInsuranceCost() {
		return insuranceCost;
	}
	public void setInsuranceCost(BigDecimal insuranceCost) {
		this.insuranceCost = insuranceCost;
	}
	public BigDecimal getAirwaybillInsuranceCost() {
		return airwaybillInsuranceCost;
	}
	public void setAirwaybillInsuranceCost(BigDecimal airwaybillInsuranceCost) {
		this.airwaybillInsuranceCost = airwaybillInsuranceCost;
	}
	public String getNamaPengirim() {
		return namaPengirim;
	}
	public void setNamaPengirim(String namaPengirim) {
		this.namaPengirim = namaPengirim;
	}
	public String getAlamatPengirim() {
		return alamatPengirim;
	}
	public void setAlamatPengirim(String alamatPengirim) {
		this.alamatPengirim = alamatPengirim;
	}
	public String getKelurahanKecamatanPengirim() {
		return kelurahanKecamatanPengirim;
	}
	public void setKelurahanKecamatanPengirim(String kelurahanKecamatanPengirim) {
		this.kelurahanKecamatanPengirim = kelurahanKecamatanPengirim;
	}
	public String getKotaKabupatenPengirim() {
		return kotaKabupatenPengirim;
	}
	public void setKotaKabupatenPengirim(String kotaKabupatenPengirim) {
		this.kotaKabupatenPengirim = kotaKabupatenPengirim;
	}
	public String getPropinsiPengirim() {
		return propinsiPengirim;
	}
	public void setPropinsiPengirim(String propinsiPengirim) {
		this.propinsiPengirim = propinsiPengirim;
	}
	public String getKodeposPengirim() {
		return kodeposPengirim;
	}
	public void setKodeposPengirim(String kodeposPengirim) {
		this.kodeposPengirim = kodeposPengirim;
	}
	public String getPicPengirim() {
		return picPengirim;
	}
	public void setPicPengirim(String picPengirim) {
		this.picPengirim = picPengirim;
	}
	public String getNoHPPicPengirim() {
		return noHPPicPengirim;
	}
	public void setNoHPPicPengirim(String noHPPicPengirim) {
		this.noHPPicPengirim = noHPPicPengirim;
	}
	public String getWcsFulfillmentId() {
		return wcsFulfillmentId;
	}
	public void setWcsFulfillmentId(String wcsFulfillmentId) {
		this.wcsFulfillmentId = wcsFulfillmentId;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public Timestamp getReceived() {
		return received;
	}
	public void setReceived(Timestamp received) {
		this.received = received;
	}
	public String getNamaPenerima() {
		return namaPenerima;
	}
	public void setNamaPenerima(String namaPenerima) {
		this.namaPenerima = namaPenerima;
	}
	public String getAlamatPenerima1() {
		return alamatPenerima1;
	}
	public void setAlamatPenerima1(String alamatPenerima1) {
		this.alamatPenerima1 = alamatPenerima1;
	}
	public String getAlamatPenerima2() {
		return alamatPenerima2;
	}
	public void setAlamatPenerima2(String alamatPenerima2) {
		this.alamatPenerima2 = alamatPenerima2;
	}
	public String getKelurahanKecamatanPenerima() {
		return kelurahanKecamatanPenerima;
	}
	public void setKelurahanKecamatanPenerima(String kelurahanKecamatanPenerima) {
		this.kelurahanKecamatanPenerima = kelurahanKecamatanPenerima;
	}
	public String getKotaKabupatenPenerima() {
		return kotaKabupatenPenerima;
	}
	public void setKotaKabupatenPenerima(String kotaKabupatenPenerima) {
		this.kotaKabupatenPenerima = kotaKabupatenPenerima;
	}
	public String getPropinsiPenerima() {
		return propinsiPenerima;
	}
	public void setPropinsiPenerima(String propinsiPenerima) {
		this.propinsiPenerima = propinsiPenerima;
	}
	public String getKodeposPenerima() {
		return kodeposPenerima;
	}
	public void setKodeposPenerima(String kodeposPenerima) {
		this.kodeposPenerima = kodeposPenerima;
	}
	public String getPicPenerima() {
		return picPenerima;
	}
	public void setPicPenerima(String picPenerima) {
		this.picPenerima = picPenerima;
	}
	public String getNoHPPicPenerima() {
		return noHPPicPenerima;
	}
	public void setNoHPPicPenerima(String noHPPicPenerima) {
		this.noHPPicPenerima = noHPPicPenerima;
	}
	public String getInstruksiPengiriman() {
		return instruksiPengiriman;
	}
	public void setInstruksiPengiriman(String instruksiPengiriman) {
		this.instruksiPengiriman = instruksiPengiriman;
	}
	public String getKodeLogistik() {
		return kodeLogistik;
	}
	public void setKodeLogistik(String kodeLogistik) {
		this.kodeLogistik = kodeLogistik;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public Timestamp getTanggalPickup() {
		return tanggalPickup;
	}
	public void setTanggalPickup(Timestamp tanggalPickup) {
		this.tanggalPickup = tanggalPickup;
	}
	public Timestamp getTanggalActualPickup() {
		return tanggalActualPickup;
	}
	public void setTanggalActualPickup(Timestamp tanggalActualPickup) {
		this.tanggalActualPickup = tanggalActualPickup;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public boolean isLastStatusES() {
		return lastStatusES;
	}
	public void setLastStatusES(boolean lastStatusES) {
		this.lastStatusES = lastStatusES;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAirwayBillPengganti() {
		return airwayBillPengganti;
	}
	public void setAirwayBillPengganti(String airwayBillPengganti) {
		this.airwayBillPengganti = airwayBillPengganti;
	}
	public boolean isInvoiceApproved() {
		return invoiceApproved;
	}
	public void setInvoiceApproved(boolean invoiceApproved) {
		this.invoiceApproved = invoiceApproved;
	}
	public String getPenanggungBiaya() {
		return penanggungBiaya;
	}
	public void setPenanggungBiaya(String penanggungBiaya) {
		this.penanggungBiaya = penanggungBiaya;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getVoidDate() {
		return voidDate;
	}
	public void setVoidDate(Timestamp voidDate) {
		this.voidDate = voidDate;
	}
	public String getVoidBy() {
		return voidBy;
	}
	public void setVoidBy(String voidBy) {
		this.voidBy = voidBy;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Timestamp getLastPrintedDate() {
		return lastPrintedDate;
	}
	public void setLastPrintedDate(Timestamp lastPrintedDate) {
		this.lastPrintedDate = lastPrintedDate;
	}
	public String getLastPrintedBy() {
		return lastPrintedBy;
	}
	public void setLastPrintedBy(String lastPrintedBy) {
		this.lastPrintedBy = lastPrintedBy;
	}
	public int getPrintedCount() {
		return printedCount;
	}
	public void setPrintedCount(int printedCount) {
		this.printedCount = printedCount;
	}
	public void setAirwaybillWeight(Double airwaybillWeight) {
		this.airwaybillWeight = airwaybillWeight;
	}
	public Double getAirwaybillWeight() {
		return airwaybillWeight;
	}
	public void setAirwaybillFixedPrice(BigDecimal airwaybillFixedPrice) {
		this.airwaybillFixedPrice = airwaybillFixedPrice;
	}
	public BigDecimal getAirwaybillFixedPrice() {
		return airwaybillFixedPrice;
	}
	public void setAirwaybillGiftWrap(BigDecimal airwaybillGiftWrap) {
		this.airwaybillGiftWrap = airwaybillGiftWrap;
	}
	public BigDecimal getAirwaybillGiftWrap() {
		return airwaybillGiftWrap;
	}
	public void setAirwaybillShippingCost(BigDecimal airwaybillShippingCost) {
		this.airwaybillShippingCost = airwaybillShippingCost;
	}
	public BigDecimal getAirwaybillShippingCost() {
		return airwaybillShippingCost;
	}
	
}
