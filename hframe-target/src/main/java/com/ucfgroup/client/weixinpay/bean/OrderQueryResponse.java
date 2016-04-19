package com.ucfgroup.client.weixinpay.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.hframe.common.util.message.*;
import com.ucfgroup.client.weixinpay.*;

@XStreamAlias("xml")
public class OrderQueryResponse   {

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
	@XStreamAlias("openid")
	private String openid;
	@XStreamAlias("device_info")
	private String deviceInfo;
	@XStreamAlias("is_subscribe")
	private String isSubscribe;
	@XStreamAlias("trade_type")
	private String tradeType;
	@XStreamAlias("bank_type")
	private String bankType;
	@XStreamAlias("total_fee")
	private String totalFee;
	@XStreamAlias("fee_type")
	private String feeType;
	@XStreamAlias("cash_fee")
	private String cashFee;
	@XStreamAlias("cash_fee_type")
	private String cashFeeType;
	@XStreamAlias("coupon_fee")
	private String couponFee;
	@XStreamAlias("coupon_count")
	private String couponCount;
	@XStreamAlias("transaction_id")
	private String transactionId;
	@XStreamAlias("out_trade_no")
	private String outTradeNo;
	@XStreamAlias("attach")
	private String attach;
	@XStreamAlias("time_end")
	private String timeEnd;
	@XStreamAlias("trade_state")
	private String tradeState;
	@XStreamAlias("trade_state_desc")
	private String tradeStateDesc;
	@XStreamOmitField
	private boolean converted;

    public OrderQueryResponse() {
    }
 
	private String getSign() {
			return sign;
	}

	public void setSign(String sign) {
			this.sign = sign;
	}

	public OrderQueryResponse convert()  throws Exception{
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
	 	 	 
     public String getOpenid(){
     	return openid;
     }

     public void setOpenid(String openid){
     	this.openid = openid;
     }
	 	 	 
     public String getDeviceInfo(){
     	return deviceInfo;
     }

     public void setDeviceInfo(String deviceInfo){
     	this.deviceInfo = deviceInfo;
     }
	 	 	 
     public String getIsSubscribe(){
     	return isSubscribe;
     }

     public void setIsSubscribe(String isSubscribe){
     	this.isSubscribe = isSubscribe;
     }
	 	 	 
     public String getTradeType(){
     	return tradeType;
     }

     public void setTradeType(String tradeType){
     	this.tradeType = tradeType;
     }
	 	 	 
     public String getBankType(){
     	return bankType;
     }

     public void setBankType(String bankType){
     	this.bankType = bankType;
     }
	 	 	 
     public String getTotalFee(){
     	return totalFee;
     }

     public void setTotalFee(String totalFee){
     	this.totalFee = totalFee;
     }
	 	 	 
     public String getFeeType(){
     	return feeType;
     }

     public void setFeeType(String feeType){
     	this.feeType = feeType;
     }
	 	 	 
     public String getCashFee(){
     	return cashFee;
     }

     public void setCashFee(String cashFee){
     	this.cashFee = cashFee;
     }
	 	 	 
     public String getCashFeeType(){
     	return cashFeeType;
     }

     public void setCashFeeType(String cashFeeType){
     	this.cashFeeType = cashFeeType;
     }
	 	 	 
     public String getCouponFee(){
     	return couponFee;
     }

     public void setCouponFee(String couponFee){
     	this.couponFee = couponFee;
     }
	 	 	 
     public String getCouponCount(){
     	return couponCount;
     }

     public void setCouponCount(String couponCount){
     	this.couponCount = couponCount;
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
	 	 	 
     public String getAttach(){
     	return attach;
     }

     public void setAttach(String attach){
     	this.attach = attach;
     }
	 	 	 
     public String getTimeEnd(){
     	return timeEnd;
     }

     public void setTimeEnd(String timeEnd){
     	this.timeEnd = timeEnd;
     }
	 	 	 
     public String getTradeState(){
     	return tradeState;
     }

     public void setTradeState(String tradeState){
     	this.tradeState = tradeState;
     }
	 	 	 
     public String getTradeStateDesc(){
     	return tradeStateDesc;
     }

     public void setTradeStateDesc(String tradeStateDesc){
     	this.tradeStateDesc = tradeStateDesc;
     }
	 	 
}
