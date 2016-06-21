package com.hframework.web.config.bean.pagetemplates;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * generated by hframework on 2016.
 */@XStreamAlias("pagetemplate")
public class Pagetemplate   {

	@XStreamImplicit
    @XStreamAlias("element")
	private List<Element> elementList;
	@XStreamAsAttribute
    @XStreamAlias("id")
	private String id;
	@XStreamAsAttribute
    @XStreamAlias("path")
	private String path;
	@XStreamAsAttribute
    @XStreamAlias("name")
	private String name;
	@XStreamAsAttribute
    @XStreamAlias("type")
	private String type;
	@XStreamAsAttribute
    @XStreamAlias("parent-id")
	private String parentId;

    public Pagetemplate() {
    }
   
 	 	 
     public List<Element> getElementList(){
     	return elementList;
     }

     public void setElementList(List<Element> elementList){
     	this.elementList = elementList;
     }
	 	 	 
     public String getId(){
     	return id;
     }

     public void setId(String id){
     	this.id = id;
     }
	 	 	 
     public String getPath(){
     	return path;
     }

     public void setPath(String path){
     	this.path = path;
     }
	 	 	 
     public String getName(){
     	return name;
     }

     public void setName(String name){
     	this.name = name;
     }
	 	 	 
     public String getType(){
     	return type;
     }

     public void setType(String type){
     	this.type = type;
     }
	 	 	 
     public String getParentId(){
     	return parentId;
     }

     public void setParentId(String parentId){
     	this.parentId = parentId;
     }
	 
}