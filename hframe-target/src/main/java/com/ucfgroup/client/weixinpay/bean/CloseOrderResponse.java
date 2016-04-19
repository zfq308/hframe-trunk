package com.ucfgroup.client.weixinpay.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.hframe.common.util.message.*;
import com.ucfgroup.client.weixinpay.*;

@XStreamAlias("xml")
public class CloseOrderResponse   {

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
	@XStreamAlias("err_code")
	private String errCode;
	@XStreamAlias("err_code_des")
	private String errCodeDes;
	@XStreamOmitField
	private boolean converted;

    public CloseOrderResponse() {
    }
 
	private String getSign() {
			return sign;
	}

	public void setSign(String sign) {
			this.sign = sign;
	}

	public CloseOrderResponse convert()  throws Exception{
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
	 	 
}
