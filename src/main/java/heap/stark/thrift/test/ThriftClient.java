package heap.stark.thrift.test;

import heap.stark.thrift.Bean;
import heap.stark.thrift.HelloService;
import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThriftClient {
    public static void main(String[] args) {
        try {
            // 设置调用的服务地址-端口
            TTransport transport = new TSocket("localhost", 9090);
            // 使用二进制协议
            TProtocol protocol = new TBinaryProtocol(transport);
            // 使用的接口
            HelloService.Client client = new HelloService.Client(protocol);
            // 打开socket
            transport.open();
            System.out.println(client.sayBoolean(true));
            System.out.println(client.sayString("Hello world"));
            System.out.println(client.sayInt(20141111));
            Bean bean = client.echoBean(new Bean());
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            /*增加下面的代码几乎一定会报错，本质上是因为client不是线程安全的
            Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
            at org.apache.thrift.protocol.TBinaryProtocol.readStringBody(TBinaryProtocol.java:378)
            at org.apache.thrift.protocol.TBinaryProtocol.readString(TBinaryProtocol.java:372)
            at org.apache.thrift.protocol.TBinaryProtocol.readMessageBegin(TBinaryProtocol.java:231)
            at org.apache.thrift.TServiceClient.receiveBase(TServiceClient.java:77)
            at heap.stark.thrift.HelloService$Client.recv_echoBean(HelloService.java:165)
            at heap.stark.thrift.HelloService$Client.echoBean(HelloService.java:152)
            at heap.stark.thrift.test.ThriftClient.main(ThriftClient.java:47)
            org.apache.thrift.TApplicationException: echoBean failed: out of sequence response: expected 6 but got 512
            at org.apache.thrift.TServiceClient.receiveBase(TServiceClient.java:86)
            at heap.stark.thrift.HelloService$Client.recv_echoBean(HelloService.java:165)
            at heap.stark.thrift.HelloService$Client.echoBean(HelloService.java:152)
            at heap.stark.thrift.test.ThriftClient$1.run(ThriftClient.java:37)
            at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
            at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
            at java.lang.Thread.run(Thread.java:748)*/

            /**
             * 一个可行的方案是通过动态代理解决,
             */
            /*while (true){
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(client.echoBean(new Bean().setId32(123)));

                        } catch (TException e) {
                            e.printStackTrace();
                        }
                        transport.close();
                    }
                });
                break;
            }*/
            client.echoBean(new Bean().setB(true));

        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException te) {
            te.printStackTrace();
        }
    }
}