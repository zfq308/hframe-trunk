package com.hframework.web.config.bean.pagetemplates;

import com.hframework.web.config.bean.component.Events;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * generated by hframework on 2016.
 */@XStreamAlias("element")
public class Element   {

	@XStreamAsAttribute
    @XStreamAlias("id")
	private String id;
	@XStreamAsAttribute
    @XStreamAlias("type")
	private String type;
    @XStreamAsAttribute
    @XStreamAlias("event-extend")
    private String eventExtend;



    @XStreamAlias("events")
    private Events events;

    public Element() {
    }
   
 	 	 
     public String getId(){
     	return id;
     }

     public void setId(String id){
     	this.id = id;
     }
	 	 	 
     public String getType(){
     	return type;
     }

     public void setType(String type){
     	this.type = type;
     }

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    public String getEventExtend() {
        return eventExtend;
    }

    public void setEventExtend(String eventExtend) {
        this.eventExtend = eventExtend;
    }
}