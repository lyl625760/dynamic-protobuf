package org.liberal.protobuf;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProtoManager {

    // map< messageName,ProtoMessage >
    private Map<String, ProtoMessage> protoMessageMap = new HashMap<>();


    public void init(InputStream inputStream)throws Exception
    {
        DescriptorProtos.FileDescriptorSet descriptorSet = DescriptorProtos.FileDescriptorSet
                .parseFrom(inputStream);
        for (DescriptorProtos.FileDescriptorProto fdp : descriptorSet.getFileList()) {
            Descriptors.FileDescriptor fileDescriptor = Descriptors.FileDescriptor.buildFrom(fdp, new Descriptors.FileDescriptor[]{});
            for (Descriptors.Descriptor descriptor : fileDescriptor.getMessageTypes()) {
                parseMessageDescriptor(descriptor, protoMessageMap);
            }
        }
    }


    private void parseMessageDescriptor(Descriptors.Descriptor descriptor, Map<String, ProtoMessage> messageTypeMap) {
        if (messageTypeMap.get(descriptor.getName()) != null) return;
        ProtoMessage message = new ProtoMessage();
        message.setName(descriptor.getName());
        Map<String, ProtoField> fieldMap = new HashMap<>();
        message.setFields(fieldMap);
        messageTypeMap.put(descriptor.getName(), message);

        for (Descriptors.FieldDescriptor fieldDescriptor : descriptor.getFields()) {
            ProtoField protoField = new ProtoField();
            protoField.setName(fieldDescriptor.getName());
            protoField.setNumber(fieldDescriptor.getNumber());
            protoField.setType(fieldDescriptor.getType());
            protoField.setRepeated(fieldDescriptor.isRepeated());
            fieldMap.put(fieldDescriptor.getName(), protoField);
            if (fieldDescriptor.getType().equals(Descriptors.FieldDescriptor.Type.MESSAGE)) {
                Descriptors.Descriptor temp = fieldDescriptor.getMessageType();
                protoField.setTypeName(temp.getName());
                parseMessageDescriptor(temp, messageTypeMap);

            }
        }
    }

    public Map<String, ProtoMessage> getProtoMessageMap() {
        return protoMessageMap;
    }

    public void setProtoMessageMap(Map<String, ProtoMessage> protoMessageMap) {
        this.protoMessageMap = protoMessageMap;
    }
}
