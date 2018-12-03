package com.codex.easytourmanager.event_class;

public class SharedEvent extends EventInfo{

    private String userKey;

    public SharedEvent(){

    }

    public SharedEvent(String fromDate, String toDate, String eventBudget, String eventDestination, String eventKey, String userKey) {
        super(fromDate, toDate, eventBudget, eventDestination, eventKey);
        this.userKey = userKey;
    }

    public String getUserKey() {
        return userKey;
    }
}
