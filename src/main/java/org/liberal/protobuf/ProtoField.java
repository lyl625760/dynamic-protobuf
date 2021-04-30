package org.liberal.protobuf;

import com.google.protobuf.Descriptors;

public class ProtoField {
    private String name ;
    private Descriptors.FieldDescriptor.Type type;
    private String typeName;
    private int number;
    private boolean isRepeated;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Descriptors.FieldDescriptor.Type getType() {
        return type;
    }

    public void setType(Descriptors.FieldDescriptor.Type type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isRepeated() {
        return isRepeated;
    }

    public void setRepeated(boolean repeated) {
        isRepeated = repeated;
    }
}
