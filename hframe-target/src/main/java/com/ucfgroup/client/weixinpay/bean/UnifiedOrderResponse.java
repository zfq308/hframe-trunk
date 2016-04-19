package com.ucfgroup.client.weixinpay.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.hframe.common.util.message.*;
import com.ucfgroup.client.weixinpay.*;

@XStreamAlias("xml")
public class UnifiedOrderResponse   {

	@XStreamAlias("return_code")
	private String returnCode;
	@XStreamAlias("return_msg")
	private String returnMsg;
	@XStreamAlias("appid")
	private String appid;
	@XStreamAlias("mch_id")
	private String mchId;
	@XStreamAlias("device_info")
	private String deviceInfo;
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
	@XStreamAlias("prepay_id")
	private String prepayId;
	@XStreamAlias("trade_type")
	private String tradeType;
	@XStreamOmitField
	private boolean converted;

    public UnifiedOrderResponse() {
    }
 
	private String getSign() {
			return sign;
	}

	public void setSign(String sign) {
			this.sign = sign;
	}

	public UnifiedOrderResponse convert()  throws Exception{
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
	 	 	 
     public String getDeviceInfo(){
     	return deviceInfo;
     }

     public void setDeviceInfo(String deviceInfo){
     	this.deviceInfo = deviceInfo;
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
	 	 	 
     public String getPrepayId(){
     	return prepayId;
     }

     public void setPrepayId(String prepayId){
     	this.prepayId = prepayId;
     }
	 	 	 
     public String getTradeType(){
     	return tradeType;
     }

     public void setTradeType(String tradeType){
     	this.tradeType = tradeType;
     }
	 	 
}
