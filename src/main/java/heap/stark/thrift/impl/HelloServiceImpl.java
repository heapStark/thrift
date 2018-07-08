package heap.stark.thrift.impl;

import heap.stark.thrift.Bean;
import heap.stark.thrift.HelloService;
import org.apache.thrift.TException;

public class HelloServiceImpl implements HelloService.Iface{
    @Override
    public int sayInt(int param) throws TException {
        return param;
    }

    @Override
    public String sayString(String param) throws TException {
        return param;
    }

    @Override
    public boolean sayBoolean(boolean param) throws TException {
        return param;
    }

    @Override
    public void sayVoid() throws TException {

    }

    @Override
    public Bean echoBean(Bean bean) throws TException {
        return bean;
    }
}
