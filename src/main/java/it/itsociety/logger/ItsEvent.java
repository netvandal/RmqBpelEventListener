package it.itsociety.logger;

import javax.xml.namespace.QName;

public class ItsEvent {
    private String _name, _user_id, _process_id, _process_name, _event_type;
    private long _timestamp;
    
    public String getName() { return _name; }
    public String getType() { return _event_type; }
    public long getTimestamp() { return _timestamp; }
    public String getProcessName() { return _process_name; }
    public String getProcessId() { return _process_id; }
    public String getUserId() { return _user_id; }
    
    
    public void setName(String n) { _name = n; }
    public void setTimestamp(long t) { _timestamp = t; }
    public void setType(String t) { _event_type = t; }
    public void setProcessName(String n) { _process_name = n; }
    public void setUserId(String id) { _user_id = id; }
    public void setProcessId(String id) { _process_id = id; }

}