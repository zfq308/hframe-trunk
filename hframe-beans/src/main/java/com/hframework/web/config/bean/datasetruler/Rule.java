package com.hframework.web.config.bean.datasetruler;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * generated by hframework on 2016.
 */@XStreamAlias("rule")
public class Rule   {

	@XStreamAlias("rule-type")
	private String ruleType;
	@XStreamAlias("source-code")
	private String sourceCode;
	@XStreamAlias("source-value")
	private String sourceValue;
	@XStreamAlias("target-code")
	private String targetCode;
	@XStreamAlias("target-value")
	private String targetValue;
	@XStreamAlias("editable")
	private String editable;

    public Rule() {
    }
   
 	 	 
     public String getRuleType(){
     	return ruleType;
     }

     public void setRuleType(String ruleType){
     	this.ruleType = ruleType;
     }
	 	 	 
     public String getSourceCode(){
     	return sourceCode;
     }

     public void setSourceCode(String sourceCode){
     	this.sourceCode = sourceCode;
     }
	 	 	 
     public String getSourceValue(){
     	return sourceValue;
     }

     public void setSourceValue(String sourceValue){
     	this.sourceValue = sourceValue;
     }
	 	 	 
     public String getTargetCode(){
     	return targetCode;
     }

     public void setTargetCode(String targetCode){
     	this.targetCode = targetCode;
     }
	 	 	 
     public String getTargetValue(){
     	return targetValue;
     }

     public void setTargetValue(String targetValue){
     	this.targetValue = targetValue;
     }
	 	 	 
     public String getEditable(){
     	return editable;
     }

     public void setEditable(String editable){
     	this.editable = editable;
     }
	 
}
