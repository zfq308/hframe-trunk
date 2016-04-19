package com.ucfgroup.client.weixinpay.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.hframe.common.util.message.*;
import com.ucfgroup.client.weixinpay.*;

@XStreamAlias("xml")
public class RefundQueryResponse   {

	@XStreamAlias("appid")
	private String appid;
	@XStreamAlias("mch_id")
	private String mchId;
	@XStreamAlias("nonce_str")
	private String nonceStr;
	@XStreamAlias("out_refund_no_0")
	private String outRefundNo0;
	@XStreamAlias("out_trade_no")
	private String outTradeNo;
	@XStreamAlias("refund_count")
	private String refundCount;
	@XStreamAlias("refund_fee_0")
	private String refundFee0;
	@XStreamAlias("refund_id_0")
	private String refundId0;
	@XStreamAlias("refund_status_0")
	private String refundStatus0;
	@XStreamAlias("result_code")
	private String resultCode;
	@XStreamAlias("return_code")
	private String returnCode;
	@XStreamAlias("return_msg")
	private String returnMsg;
	@XStreamAlias("sign")
	private String sign;
	@XStreamAlias("transaction_id")
	private String transactionId;
	@XStreamOmitField
	private boolean converted;

    public RefundQueryResponse() {
    }
 
	private String getSign() {
			return sign;
	}

	public void setSign(String sign) {
			this.sign = sign;
	}

	public RefundQueryResponse convert()  throws Exception{
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
	 	 	 
     public String getOutRefundNo0(){
     	return outRefundNo0;
     }

     public void setOutRefundNo0(String outRefundNo0){
     	this.outRefundNo0 = outRefundNo0;
     }
	 	 	 
     public String getOutTradeNo(){
     	return outTradeNo;
     }

     public void setOutTradeNo(String outTradeNo){
     	this.outTradeNo = outTradeNo;
     }
	 	 	 
     public String getRefundCount(){
     	return refundCount;
     }

     public void setRefundCount(String refundCount){
     	this.refundCount = refundCount;
     }
	 	 	 
     public String getRefundFee0(){
     	return refundFee0;
     }

     public void setRefundFee0(String refundFee0){
     	this.refundFee0 = refundFee0;
     }
	 	 	 
     public String getRefundId0(){
     	return refundId0;
     }

     public void setRefundId0(String refundId0){
     	this.refundId0 = refundId0;
     }
	 	 	 
     public String getRefundStatus0(){
     	return refundStatus0;
     }

     public void setRefundStatus0(String refundStatus0){
     	this.refundStatus0 = refundStatus0;
     }
	 	 	 
     public String getResultCode(){
     	return resultCode;
     }

     public void setResultCode(String resultCode){
     	this.resultCode = resultCode;
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
	 	 	 	 
     public String getTransactionId(){
     	return transactionId;
     }

     public void setTransactionId(String transactionId){
     	this.transactionId = transactionId;
     }
	 	 
}
