package com.gdn.venice.constants;

/**
 * 
 * @author yauritux
 *
 */
public enum FinTransactionTypeConstants {

	 FIN_TRANSACTION_TYPE_UANG_MUKA_PELANGGAN(0),
	 FIN_TRANSACTION_TYPE_REFUND_PELANGGAN(1),
	 FIN_TRANSACTION_TYPE_COMMISSION(2),
	 FIN_TRANSACTION_TYPE_REBATE(3),
	 FIN_TRANSACTION_TYPE_PROCESS_FEE(4),
	 FIN_TRANSACTION_TYPE_LOGISTICS_REVENUE(5),
	 FIN_TRANSACTION_TYPE_LOGISTICS_PAYMENT(6),
	 FIN_TRANSACTION_TYPE_LOGISTICS_PENALTY(7),
	 FIN_TRANSACTION_TYPE_OVER_LESS_PAYMENT(8),
	 FIN_TRANSACTION_TYPE_MERCHANT_PAYMENT(9),
	 FIN_TRANSACTION_TYPE_MERCHANT_PENALTY(10),
	 FIN_TRANSACTION_TYPE_PROMO_SPONSORSHIP(11),
	 FIN_TRANSACTION_TYPE_PPN(12),
	 FIN_TRANSACTION_TYPE_MANUAL_ADJUSTMENT_JOURNAL(13),
	 FIN_TRANSACTION_TYPE_UANG_JAMINAN_TRANSAKSI(14),
	 FIN_TRANSACTION_TYPE_SM_PROMOSI_KONSUMEN(15),
	 FIN_TRANSACTION_TYPE_HPP_BIAYA_BARANG(16),
	 FIN_TRANSACTION_TYPE_POT_PENJUALAN(17),
	 FIN_TRANSACTION_TYPE_VOUCHER_YANG_BELUM_DIREALISASI(18),
	 FIN_TRANSACTION_TYPE_SM_VOUCHER_CUSTOMER_SERVICE(19),
	 FIN_TRANSACTION_TYPE_HUTANG_MERCHANT(20),
	 FIN_TRANSACTION_TYPE_PENJUALAN(21),
	 FIN_TRANSACTION_TYPE_PENDAPATAN_KOMISI(22),
	 FIN_TRANSACTION_TYPE_PENDAPATAN_LOGISTIK(23),
	 FIN_TRANSACTION_TYPE_PENDAPATAN_JASA_TRANSAKSI(24),
	 FIN_TRANSACTION_TYPE_PENDAPATAN_JASA_HANDLING(25),
	 FIN_TRANSACTION_TYPE_INVENTORY_OVER_VOUCHER(26),
	 FIN_TRANSACTION_TYPE_SM_BEBAN_PROMOSI_BEBAS_BIAYA_KIRIM(27),
	 FIN_TRANSACTION_TYPE_BIAYA_HARUS_DIBAYAR_LOGISTIK(28),
	 FIN_TRANSACTION_TYPE_INVENTORY(29),
	 FIN_TRANSACTION_TYPE_BARANG_DITERIMA_DIMUKA(30),
	 FIN_TRANSACTION_TYPE_REFUND_BANK(31),
	 FIN_TRANSACTION_TYPE_HPP_LOGISTIK(32),
	 FIN_TRANSACTION_TYPE_PIUTANG_MERCHANT(33),
	 FIN_TRANSACTION_TYPE_PIUTANG_LAIN_LAIN(34),
	 FIN_TRANSACTION_TYPE_PERSEDIAAN_BARANG_DAGANG(35),
	 FIN_TRANSACTION_TYPE_UANG_JAMINAN_BELUM_TERIDENTIFIKASI(36);	
	 
	 private long id;
	 
	 public long id() {
		 return id;
	 }
	 
	 private FinTransactionTypeConstants(long id) {
		 this.id = id;
	 }
}