package com.codex.easytourmanager.event_class;

import java.io.Serializable;

public class EventInfo  implements Serializable {
    private String fromDate;
    private String toDate;
    private String eventBudget;
    private String eventDestination;
    private String eventKey;

    public EventInfo() {
    }

    public EventInfo(String fromDate, String toDate, String eventBudget, String eventDestination, String eventKey) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.eventBudget = eventBudget;
        this.eventDestination = eventDestination;
        this.eventKey = eventKey;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getEventBudget() {
        return eventBudget;
    }

    public String getEventDestination() {
        return eventDestination;
    }

    public String getEventKey() {
        return eventKey;
    }
}

