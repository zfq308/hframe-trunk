package com.ucfgroup.client.weixinpay.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.hframe.common.util.message.*;
import com.ucfgroup.client.weixinpay.*;

@XStreamAlias("xml")
public class RefundResponse   {

	@XStreamAlias("return_code")
	private String returnCode;
	@XStreamAlias("return_msg")
	private String returnMsg;
	@XStreamAlias("appid")
	private String appid;
	@XStreamAlias("mch_id")
	private String mchId;
	@XStreamAlias("nonce_str")
	private String nonceStr;
	@XStreamAlias("sign")
	private String sign;
	@XStreamAlias("result_code")
	private String resultCode;
	@XStreamAlias("err_code")
	private String errCode;
	@XStreamAlias("err_code_des")
	private String errCodeDes;
	@XStreamAlias("device_info")
	private String deviceInfo;
	@XStreamAlias("transaction_id")
	private String transactionId;
	@XStreamAlias("out_trade_no")
	private String outTradeNo;
	@XStreamAlias("out_refund_no")
	private String outRefundNo;
	@XStreamAlias("refund_id")
	private String refundId;
	@XStreamAlias("refund_channel")
	private String refundChannel;
	@XStreamAlias("refund_fee")
	private String refundFee;
	@XStreamAlias("fee_type")
	private String feeType;
	@XStreamAlias("total_fee")
	private String totalFee;
	@XStreamAlias("cash_fee")
	private String cashFee;
	@XStreamAlias("coupon_refund_fee")
	private String couponRefundFee;
	@XStreamOmitField
	private boolean converted;

    public RefundResponse() {
    }
 
	private String getSign() {
			return sign;
	}

	public void setSign(String sign) {
			this.sign = sign;
	}

	public RefundResponse convert()  throws Exception{
			if(!converted) {
			   String beforeInfo = XmlUtils.writeValueAsString(this);
			   System.out.println(beforeInfo);
			   converted = true;
			   sign=WeixinpayHelper.decryptSign(this);
			   String afterInfo = XmlUtils.writeValueAsString(this);
			   System.out.println(afterInfo);
			}
			return this;
	}

  
 	 	 
     public String getReturnCode(){
     	return returnCode;
     }

     public void setReturnCode(String returnCode){
     	this.returnCode = returnCode;
     }
	 	 	 
     public String getReturnMsg(){
     	return returnMsg;
     }

     public void setReturnMsg(String returnMsg){
     	this.returnMsg = returnMsg;
     }
	 	 	 
     public String getAppid(){
     	return appid;
     }

     public void setAppid(String appid){
     	this.appid = appid;
     }
	 	 	 
     public String getMchId(){
     	return mchId;
     }

     public void setMchId(String mchId){
     	this.mchId = mchId;
     }
	 	 	 
     public String getNonceStr(){
     	return nonceStr;
     }

     public void setNonceStr(String nonceStr){
     	this.nonceStr = nonceStr;
     }
	 	 	 	 
     public String getResultCode(){
     	return resultCode;
     }

     public void setResultCode(String resultCode){
     	this.resultCode = resultCode;
     }
	 	 	 
     public String getErrCode(){
     	return errCode;
     }

     public void setErrCode(String errCode){
     	this.errCode = errCode;
     }
	 	 	 
     public String getErrCodeDes(){
     	return errCodeDes;
     }

     public void setErrCodeDes(String errCodeDes){
     	this.errCodeDes = errCodeDes;
     }
	 	 	 
     public String getDeviceInfo(){
     	return deviceInfo;
     }

     public void setDeviceInfo(String deviceInfo){
     	this.deviceInfo = deviceInfo;
     }
	 	 	 
     public String getTransactionId(){
     	return transactionId;
     }

     public void setTransactionId(String transactionId){
     	this.transactionId = transactionId;
     }
	 	 	 
     public String getOutTradeNo(){
     	return outTradeNo;
     }

     public void setOutTradeNo(String outTradeNo){
     	this.outTradeNo = outTradeNo;
     }
	 	 	 
     public String getOutRefundNo(){
     	return outRefundNo;
     }

     public void setOutRefundNo(String outRefundNo){
     	this.outRefundNo = outRefundNo;
     }
	 	 	 
     public String getRefundId(){
     	return refundId;
     }

     public void setRefundId(String refundId){
     	this.refundId = refundId;
     }
	 	 	 
     public String getRefundChannel(){
     	return refundChannel;
     }

     public void setRefundChannel(String refundChannel){
     	this.refundChannel = refundChannel;
     }
	 	 	 
     public String getRefundFee(){
     	return refundFee;
     }

     public void setRefundFee(String refundFee){
     	this.refundFee = refundFee;
     }
	 	 	 
     public String getFeeType(){
     	return feeType;
     }

     public void setFeeType(String feeType){
     	this.feeType = feeType;
     }
	 	 	 
     public String getTotalFee(){
     	return totalFee;
     }

     public void setTotalFee(String totalFee){
     	this.totalFee = totalFee;
     }
	 	 	 
     public String getCashFee(){
     	return cashFee;
     }

     public void setCashFee(String cashFee){
     	this.cashFee = cashFee;
     }
	 	 	 
     public String getCouponRefundFee(){
     	return couponRefundFee;
     }

     public void setCouponRefundFee(String couponRefundFee){
     	this.couponRefundFee = couponRefundFee;
     }
	 	 
}
