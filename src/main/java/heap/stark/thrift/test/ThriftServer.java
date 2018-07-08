package heap.stark.thrift.test;

import heap.stark.thrift.HelloService;
import heap.stark.thrift.impl.HelloServiceImpl;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

public class ThriftServer {
    public static void main(String[] args) {
        try {
            // 设置服务器端口
            TServerSocket serverTransport = new TServerSocket(9090);
            // 设置二进制协议工厂
            TJSONProtocol.Factory protocolFactory = new TJSONProtocol.Factory();
            // 处理器关联业务实现
            HelloService.Processor<HelloService.Iface> processor = new HelloService.Processor<HelloService.Iface>(
                    new HelloServiceImpl());
            //使用单线程标准阻塞I/O模型
            TServer.Args simpleArgs = new TServer.Args(serverTransport)
                    .processor(processor)
                    .protocolFactory(protocolFactory);
            TServer server = new TSimpleServer(simpleArgs);
            System.out.println("开启thrift服务器，监听端口：9090");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}