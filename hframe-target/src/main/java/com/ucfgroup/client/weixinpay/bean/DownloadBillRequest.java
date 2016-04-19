package com.ucfgroup.client.weixinpay.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.hframe.common.util.message.*;
import com.ucfgroup.client.weixinpay.*;

@XStreamAlias("xml")
public class DownloadBillRequest   {

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
	@XStreamAlias("bill_date")
	private String billDate;
	@XStreamAlias("bill_type")
	private String billType;
	@XStreamOmitField
	private boolean converted;

    public DownloadBillRequest() {
    }
 
	public String getAppid() {
			return appid;
	}

	private void setAppid(String appid) {
			this.appid = appid;
	}

	public String getMchId() {
			return mchId;
	}

	private void setMchId(String mchId) {
			this.mchId = mchId;
	}

	public String getDeviceInfo() {
			return deviceInfo;
	}

	private void setDeviceInfo(String deviceInfo) {
			this.deviceInfo = deviceInfo;
	}

	public String getNonceStr() {
			return nonceStr;
	}

	private void setNonceStr(String nonceStr) {
			this.nonceStr = nonceStr;
	}

	public String getSign() {
			return sign;
	}

	private void setSign(String sign) {
			this.sign = sign;
	}

	public DownloadBillRequest convert()  throws Exception{
			if(!converted) {
			   String beforeInfo = XmlUtils.writeValueAsString(this);
			   System.out.println(beforeInfo);
			   converted = true;
			   appid=WeixinpayConfig.getInstance().getAppid();
			   mchId=WeixinpayConfig.getInstance().getMchId();
			   deviceInfo=WeixinpayConfig.getInstance().getDeviceInfo();
			   nonceStr=WeixinpayHelper.md5RandomNumber();
			   sign=WeixinpayHelper.encryptSign(this);
			   String afterInfo = XmlUtils.writeValueAsString(this);
			   System.out.println(afterInfo);
			}
			return this;
	}

  
 	 	 	 	 	 	 	 
     public String getBillDate(){
     	return billDate;
     }

     public void setBillDate(String billDate){
     	this.billDate = billDate;
     }
	 	 	 
     public String getBillType(){
     	return billType;
     }

     public void setBillType(String billType){
     	this.billType = billType;
     }
	 	 
}
