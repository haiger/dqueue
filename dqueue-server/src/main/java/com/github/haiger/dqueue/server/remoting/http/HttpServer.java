package com.github.haiger.dqueue.server.remoting.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.haiger.dqueue.server.remoting.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author haiger
 * @since 2017年2月14日 下午8:00:01
 */
public class HttpServer implements Server {
    private static final Logger log = LoggerFactory.getLogger(HttpServer.class);
    private int port;
    private EventLoopGroup boss;
    private EventLoopGroup work;
    
    public HttpServer(int port) {
        this.port = port;
        boss = new NioEventLoopGroup();
        work = new NioEventLoopGroup();
    }

    @Override
    public void start() {
        ServerBootstrap boot = new ServerBootstrap();
        try {
            boot.group(boss, work);
            boot.channel(NioServerSocketChannel.class);
            boot.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new HttpResponseEncoder());
                    p.addLast(new HttpRequestDecoder());
                    p.addLast(new HttpObjectAggregator(65536));
                    p.addLast(new HttpHandler());
                }
            });
            boot.option(ChannelOption.SO_BACKLOG, 1024);
            boot.option(ChannelOption.SO_REUSEADDR, true);
            boot.option(ChannelOption.SO_TIMEOUT, 1000);
            boot.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);

            ChannelFuture f = boot.bind(port).sync();

            log.info("httpServer start success. port:{}", port);

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("httpServer has be Interrupted by :", e);
            System.exit(1);
        } finally {
            work.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }

    @Override
    public void shutdown() {
        if (boss != null) {
            boss.shutdownGracefully();
        } 
        if (work != null) {
            work.shutdownGracefully();
        } 
        log.info("HttpServer shutdown.");
    }

}
