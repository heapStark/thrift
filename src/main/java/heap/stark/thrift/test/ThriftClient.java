package heap.stark.thrift.test;

import heap.stark.thrift.HelloService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class ThriftClient {
    public static void main(String[] args) {
        try {
            // 设置调用的服务地址-端口
            TTransport transport = new TSocket("localhost", 9090);
            // 使用二进制协议
            TProtocol protocol = new TJSONProtocol(transport);
            // 使用的接口
            HelloService.Client client = new HelloService.Client(protocol);
            // 打开socket
            transport.open();
            System.out.println(client.sayBoolean(true));
            System.out.println(client.sayString("Hello world"));
            System.out.println(client.sayInt(20141111));
            client.sayVoid();
            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException te) {
            te.printStackTrace();
        }
    }
}