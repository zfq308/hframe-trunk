package com.hframework.generator.thirdplatform.bean.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("publicParameters")
public class PublicParameters   {

	@XStreamImplicit
    @XStreamAlias("parameter")
	private List<Parameter> parameterList;

    public PublicParameters() {
    	}
   
 
 	
	public List<Parameter> getParameterList(){
		return parameterList == null ? new ArrayList<Parameter>() : parameterList;
	}

	public void setParameterList(List<Parameter> parameterList){
    	this.parameterList = parameterList;
    }
}
