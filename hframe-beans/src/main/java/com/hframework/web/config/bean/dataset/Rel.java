package com.hframework.web.config.bean.dataset;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * generated by hframework on 2016.
 */@XStreamAlias("rel")
public class Rel   {

	@XStreamAsAttribute
    @XStreamAlias("entity-code")
	private String entityCode;


    @XStreamAsAttribute
    @XStreamAlias("rel-field")
    private String relField;

    @XStreamImplicit
    @XStreamAlias("rel")
    private List<Rel> relList;

    public Rel() {
    }
   
 	 	 
     public String getEntityCode(){
     	return entityCode;
     }

     public void setEntityCode(String entityCode){
     	this.entityCode = entityCode;
     }

    public String getRelField() {
        return relField;
    }

    public void setRelField(String relField) {
        this.relField = relField;
    }

    public List<Rel> getRelList() {
        return relList;
    }

    public void setRelList(List<Rel> relList) {
        this.relList = relList;
    }
}