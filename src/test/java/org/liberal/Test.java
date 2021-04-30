package org.liberal;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.protobuf.*;
import org.liberal.protobuf.ProtobufUtil;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.protobuf.FieldType.MESSAGE;

// map: 不支持 再嵌套map、list，但是可以嵌入message
// list :不支持再嵌套 map、list，但是可以嵌入message
// enum :
public class Test {

    private static ObjectMapper mapper = new ObjectMapper();


    public static void main(String[] args) throws Exception {

        // Step 1: init ProtobufUitl
        // You can generate proto desc file from proto file like this: protoc --proto_path=./  --java_out=./ a.proto --descriptor_set_out=message.desc
        ProtobufUtil util = new ProtobufUtil();
        try(InputStream inputStream = Test.class.getResourceAsStream("/message.desc"))
        {
            util.init(inputStream);
        }

        // Step 2: serialize protobufMessage
        A.SearchRequest.Builder ret = A.SearchRequest.newBuilder().setPageNumber(10000);
        A.CustomMessage customMessage = A.CustomMessage.newBuilder().setHellokey(100).setHellovalue(1000).build();
        ret.setMessage(customMessage);
        ret.setQuery("query");
        ret.setEmbeddingData(ByteString.copyFrom("abc".getBytes()));
        ret.setIsMan(true);
        ret.setSalary(0.02);
        List<Integer> tem = new ArrayList<>();
        tem.add(12);
        tem.add(34);
        tem.add(46);
        ret.addAllTest(tem);
        ret.putOo(123, 456);
        ret.putOo(789, 100);

        // Second Map
        A.CustomMessage customMessage2 = A.CustomMessage.newBuilder().setHellokey(200).setHellovalue(2000).build();
        A.CustomMessage customMessage3 = A.CustomMessage.newBuilder().setHellokey(300).setHellovalue(3000).build();
        ret.putSecondMap(2, customMessage2);
        ret.putSecondMap(3, customMessage3);

        ret.setGender(A.Gender.woman);

        A.SearchRequest.MyMessage myMessage = A.SearchRequest.MyMessage.newBuilder().setMy(100).setHoney(101).build();
        ret.setPoney(myMessage);

        byte[] bytes = ret.build().toByteArray();


        // Step3 : deserialize protobuf message without Protobuf Java Class
        println(bytes,"SearchRequest","query",util);
        println(bytes,"SearchRequest","salary",util);
        println(bytes,"SearchRequest","test",util);


        Object object = util.parseMessage(bytes,"SearchRequest","poney");
        Object object1 = util.parseMessage((byte[])object,"MyMessage","my");
        System.out.println(mapper.writeValueAsString(object1));



    }
    static void println(byte[]bytes,String messageName,String fieldName,ProtobufUtil util)throws Exception
    {
        Object object = util.parseMessage(bytes,messageName,fieldName);
        System.out.println(mapper.writeValueAsString(object));
    }


}
