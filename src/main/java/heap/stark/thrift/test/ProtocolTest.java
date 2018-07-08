package heap.stark.thrift.test;

import heap.stark.thrift.Bean;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TByteBuffer;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

public class ProtocolTest {
    @Test
    public void test() throws TException {
        TTransport transport = new TSocket("localhost", 9090);
        // 使用二进制协议
        TProtocol protocol =null;
        ByteBuffer byteBuffer = ByteBuffer.allocate(122);
        protocol = new TBinaryProtocol(new TByteBuffer(byteBuffer));

        Bean bean = new Bean();
        bean.setB(true);
        bean.setId32(123);
        bean.write(protocol);

        protocol.writeMessageEnd();
        byteBuffer.flip();

        bean = new Bean();
        bean.read(protocol);
        Assert.assertEquals(bean.id32,123);

    }
}
