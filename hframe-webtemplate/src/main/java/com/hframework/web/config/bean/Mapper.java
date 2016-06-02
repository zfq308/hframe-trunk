package com.hframework.web.config.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.hframework.web.config.bean.mapper.*;

/**
 * generated by hframework on 2016.
 */@XStreamAlias("mapper")
public class Mapper   {

	@XStreamAlias("baseMapper")
	private BaseMapper baseMapper;
	@XStreamAlias("eventMapper")
	private EventMapper eventMapper;
	@XStreamAsAttribute
    @XStreamAlias("component-id")
	private String componentId;
	@XStreamAsAttribute
    @XStreamAlias("data-set")
	private String dataSet;
	@XStreamAsAttribute
    @XStreamAlias("data-auth")
	private String dataAuth;

    public Mapper() {
    }
   
 	 	 
     public BaseMapper getBaseMapper(){
     	return baseMapper == null ? new BaseMapper() : baseMapper;
     }

     public void setBaseMapper(BaseMapper baseMapper){
     	this.baseMapper = baseMapper;
     }
	 	 	 
     public EventMapper getEventMapper(){
     	return eventMapper == null ? new EventMapper() : eventMapper;
     }

     public void setEventMapper(EventMapper eventMapper){
     	this.eventMapper = eventMapper;
     }
	 	 	 
     public String getComponentId(){
     	return componentId;
     }

     public void setComponentId(String componentId){
     	this.componentId = componentId;
     }
	 	 	 
     public String getDataSet(){
     	return dataSet;
     }

     public void setDataSet(String dataSet){
     	this.dataSet = dataSet;
     }
	 	 	 
     public String getDataAuth(){
     	return dataAuth;
     }

     public void setDataAuth(String dataAuth){
     	this.dataAuth = dataAuth;
     }
	 
}
