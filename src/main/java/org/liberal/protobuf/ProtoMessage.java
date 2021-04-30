package org.liberal.protobuf;
import java.util.Map;

public class ProtoMessage {
    private String name ;
    private Map<String, ProtoField> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, ProtoField> getFields() {
        return fields;
    }

    public void setFields(Map<String, ProtoField> fields) {
        this.fields = fields;
    }
}
