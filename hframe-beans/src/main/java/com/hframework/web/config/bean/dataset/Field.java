package com.hframework.web.config.bean.dataset;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * generated by hframework on 2016.
 */@XStreamAlias("field")
public class Field   {

	@XStreamAlias("rel")
	private Rel rel;
	@XStreamImplicit
    @XStreamAlias("enum")
	private List<Enum> enumList;
	@XStreamAlias("create-edit-type")
	private String createEditType;
	@XStreamAlias("enum-class")
	private EnumClass enumClass;
	@XStreamAlias("update-edit-type")
	private String updateEditType;
	@XStreamAsAttribute
    @XStreamAlias("code")
	private String code;
	@XStreamAsAttribute
    @XStreamAlias("name")
	private String name;
	@XStreamAsAttribute
    @XStreamAlias("edit-type")
	private String editType;
	@XStreamAsAttribute
    @XStreamAlias("action")
	private String action;
    @XStreamAsAttribute
    @XStreamAlias("is-key")
    private String isKey;
    @XStreamAsAttribute
    @XStreamAlias("is-name")
    private String isName;

 public Field() {
    }
   
 	 	 
     public Rel getRel(){
     	return rel;
     }

     public void setRel(Rel rel){
     	this.rel = rel;
     }
	 	 	 
     public List<Enum> getEnumList(){
     	return enumList;
     }

     public void setEnumList(List<Enum> enumList){
     	this.enumList = enumList;
     }
	 	 	 
     public String getCreateEditType(){
     	return createEditType;
     }

     public void setCreateEditType(String createEditType){
     	this.createEditType = createEditType;
     }
	 	 	 
     public EnumClass getEnumClass(){
     	return enumClass;
     }

     public void setEnumClass(EnumClass enumClass){
     	this.enumClass = enumClass;
     }
	 	 	 
     public String getUpdateEditType(){
     	return updateEditType;
     }

     public void setUpdateEditType(String updateEditType){
     	this.updateEditType = updateEditType;
     }
	 	 	 
     public String getCode(){
     	return code;
     }

     public void setCode(String code){
     	this.code = code;
     }
	 	 	 
     public String getName(){
     	return name;
     }

     public void setName(String name){
     	this.name = name;
     }
	 	 	 
     public String getEditType(){
     	return editType;
     }

     public void setEditType(String editType){
     	this.editType = editType;
     }
	 	 	 
     public String getAction(){
     	return action;
     }

     public void setAction(String action){
     	this.action = action;
     }

     public String getIsKey() {
      return isKey;
     }

     public void setIsKey(String isKey) {
      this.isKey = isKey;
     }

     public String getIsName() {
      return isName;
     }

     public void setIsName(String isName) {
      this.isName = isName;
     }
}
