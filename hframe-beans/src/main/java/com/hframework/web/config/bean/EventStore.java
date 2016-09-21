package com.hframework.web.config.bean;

import com.hframework.common.util.StringUtils;
import com.hframework.web.config.bean.component.Element;
import com.hframework.web.config.bean.component.Event;
import com.hframework.web.config.bean.dataset.Descriptor;
import com.hframework.web.config.bean.dataset.Entity;
import com.hframework.web.config.bean.dataset.Fields;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * generated by hframework on 2016.
 */@XStreamAlias("event-store")
public class EventStore {
    @XStreamAsAttribute
    @XStreamAlias("group")
    private String group;

    @XStreamImplicit
    @XStreamAlias("event")
    private List<Event> eventList;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }
}
