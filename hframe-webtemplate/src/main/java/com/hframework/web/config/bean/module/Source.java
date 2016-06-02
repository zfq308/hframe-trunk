package com.hframework.web.config.bean.module;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * generated by hframework on 2016.
 */@XStreamAlias("source")
public class Source   {

	@XStreamAsAttribute
    @XStreamAlias("scope")
	private String scope;
	@XStreamAsAttribute
    @XStreamAlias("param")
	private String param;

    public Source() {
    }
   
 	 	 
     public String getScope(){
     	return scope;
     }

     public void setScope(String scope){
     	this.scope = scope;
     }
	 	 	 
     public String getParam(){
     	return param;
     }

     public void setParam(String param){
     	this.param = param;
     }
	 
}
