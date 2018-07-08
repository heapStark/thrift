package heap.stark.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class EchoServer {public void run(final int port) throws Exception {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter))
                                .addLast(new StringDecoder()).addLast(new StringEncoder())
                                .addLast(new DefaultEventExecutorGroup(8),
                                        new NettyServerHandler());
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture future = b.bind(port);
        future.channel().closeFuture();
    } finally {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}

    public static void main(String[] args) throws Exception {
        int port = 8084;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new EchoServer().run(port);
        System.out.println("1234");
    }

    public static class NettyServerHandler extends SimpleChannelInboundHandler<Object> {
         static  AtomicInteger count = new AtomicInteger(1);

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelActived");
            super.channelActive(ctx);
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            String body = (String) msg;
            System.out.println(count.getAndIncrement() + ":" + body);
            ctx.writeAndFlush("Welcome to Netty.$_");
        }
    }
}
