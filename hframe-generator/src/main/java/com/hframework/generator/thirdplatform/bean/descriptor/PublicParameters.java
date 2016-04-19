package com.hframework.generator.thirdplatform.bean.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("publicParameters")
public class PublicParameters   {

	@XStreamImplicit
    @XStreamAlias("parameter")
	private List<Parameter> parameterList;

    public PublicParameters() {
    	}
   
 
 	
	public List<Parameter> getParameterList(){
		return parameterList;
	}

	public void setParameterList(List<Parameter> parameterList){
    	this.parameterList = parameterList;
    }
}
