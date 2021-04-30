package org.liberal.protobuf;

import com.google.protobuf.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// map: 不支持 再嵌套map、list，但是可以嵌入message
// list :不支持再嵌套 map、list，但是可以嵌入message
// enum :
public class ProtobufUtil {
    private ProtoManager protoManager;
    public void init(InputStream inputStream)throws Exception
    {
        if (protoManager == null) {
            protoManager = new ProtoManager();
        }
        protoManager.init(inputStream);
    }

    public ProtobufUtil() {
    }

    public ProtobufUtil(ProtoManager protoManager) {
        this.protoManager = protoManager;
    }

    public Object parseMessage(byte[] data, String messageName, String fieldName) {

        ProtoMessage messageType = protoManager.getProtoMessageMap().get(messageName);
        ProtoField protoField = messageType.getFields().get(fieldName);
        Object ret = null;
        boolean isMap = false;
        boolean isList = false;
        if (protoField.getTypeName() != null && protoField.getTypeName().endsWith("Entry")) {
            isMap = true;
            ret = new HashMap<>();
        } else if (protoField.isRepeated()) {
            isList = true;
            ret = new ArrayList<>();
        }

        try {

            boolean done = false;
            final CodedInputStream input = CodedInputStream.newInstance(data, 0, data.length);
            while (!done) {
                int tag = input.readTag();
                if (tag == 0) break;
                int number = tag >> 3;
                if (number != protoField.getNumber()) {
                    input.skipField(tag);
                } else {
                    Object object = null;
                    // protoField Map
                    if (isMap) {
                        readMap(input, protoField, ret);
                    } else if (isList) {
                        done = readList(input,protoField,ret,tag);
                    } else {
                        ret = readScalarValue(protoField, input);
                        done = true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return ret;

    }

    private void readMap(CodedInputStream input, ProtoField protoField, Object ret) throws Exception {
        int length = input.readRawVarint32();
        input.readTag();
        Object key = readScalarValue(protoManager.getProtoMessageMap().get(protoField.getTypeName()).getFields().get("key"), input);
        input.readTag();
        Object value = readScalarValue(protoManager.getProtoMessageMap().get(protoField.getTypeName()).getFields().get("value"), input);
        ((Map) ret).put(key, value);
    }

    private boolean readList(CodedInputStream input, ProtoField protoField, Object ret, int tag) throws Exception {
        // packed encoding
        if ((tag & 0x7) == 2) {
            int length = input.readRawVarint32();
            int limit = input.pushLimit(length);
            while (input.getBytesUntilLimit() > 0) {
                Object object = readScalarValue(protoField, input);
                ((List) ret).add(object);
            }
            input.popLimit(limit);
            boolean done = true;
            return done;
        } else {
            Object object = readScalarValue(protoField, input);
            ((List) ret).add(object);
            return false;
        }
    }


    /**
     * Scalar Value Types: https://developers.google.com/protocol-buffers/docs/proto3
     *
     * @param protoField
     * @param input
     * @return
     * @throws Exception
     */
    private Object readScalarValue(ProtoField protoField, CodedInputStream input) throws Exception {
        Object object = null;
        switch (protoField.getType()) {
            case BYTES:
            case MESSAGE:
                object = input.readByteArray();
                break;
            case INT32:
                object = input.readInt32();
                break;
            case UINT32:
                object = input.readUInt32();
                break;
            case SINT32:
                object = input.readSInt32();
                break;
            case FIXED32:
                object = input.readFixed32();
                break;
            case SFIXED32:
                object = input.readSFixed32();
                break;
            case INT64:
                object = input.readInt64();
                break;
            case UINT64:
                object = input.readUInt64();
                break;
            case SINT64:
                object = input.readSInt64();
                break;
            case FIXED64:
                object = input.readFixed64();
                break;
            case SFIXED64:
                object = input.readSFixed64();
                break;
            case FLOAT:
                object = input.readFloat();
                break;
            case DOUBLE:
                object = input.readDouble();
                break;
            case STRING:
                object = input.readStringRequireUtf8();
                break;
            case BOOL:
                object = input.readBool();
                break;
            case ENUM:
                object = input.readEnum();
                break;
            default:
                System.out.println("Unsupported ProtoBuf Type:" + protoField.getType());
                break;
        }
        return object;
    }
}
