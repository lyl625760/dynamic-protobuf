# dynamic-protobuf
Combine the schema of protobuf and the flexible of json format. 
It helps you parse any message which is serialized by Google protobuf  without  compiled protobuf java class

Features :
1. Deserialize protobuf message without Protobuf Java Class。
2. All its need is .desc file,which is generated by .proto file .
3. Campatibale with Google protobuf 2.0 & 3.0 format. 



[h1]Example : 

// Step 1: init ProtobufUtil
```
        
        ProtobufUtil util = new ProtobufUtil();
        try(InputStream inputStream = Test.class.getResourceAsStream("/message.desc"))
        {
            util.init(inputStream);
        }
 ```     
 Notice :
 ```
 // You can generate proto desc file from proto file like this:
protoc --proto_path=./  --java_out=./ a.proto --descriptor_set_out=message.desc
 ```
 
// Step2 : deserialize protobuf message without Protobuf Java Class

```
        util.parseMessage(bytes,messageName,fieldName)
```
you can refer to the examples in src/main/test/Test
