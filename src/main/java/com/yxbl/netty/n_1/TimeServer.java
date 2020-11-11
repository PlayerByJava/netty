package com.yxbl.netty.n_1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author: yxz
 * @Description:
 * @Date: 2020/11/10
 */
public class TimeServer {

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        new TimeServer().bind(port);
    }

    public void bind(int port) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 引导类，它主要对应用程序进行配置，并使其运行起来的过程
            ServerBootstrap b = new ServerBootstrap();

            //设置一个EventLoop
            b.group(bossGroup, workerGroup).
                    channel(NioServerSocketChannel.class)               //用来设置一个服务器端的通道实现
                    .option(ChannelOption.SO_BACKLOG, 1024)               //用来给ServerChannel添加配置
                    .childHandler(new ChildChannelHandler());                       //该方法用来设置业务处理类(自定义handler)

            ChannelFuture f = b.bind(port).sync();                      //用来设置占用的端口号
            f.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel channel) {
            channel.pipeline().addLast(new TimeServerHandler());
        }

    }
}
