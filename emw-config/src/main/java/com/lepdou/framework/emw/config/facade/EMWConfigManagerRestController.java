/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.facade;

import com.google.gson.Gson;
import com.lepdou.framework.emw.config.bean.ConfigDO;
import com.lepdou.framework.emw.config.core.EMWConfigException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 暴露 rest 接口提供管控能力
 *
 * @author lepdou
 * @version : EMWConfigManagerRestController.java, v 0.1 2021年06月18日 下午4:58 lepdou Exp $
 */
public class EMWConfigManagerRestController extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(EMWConfigManagerRestController.class);
    private static final Gson   gson   = new Gson();

    public static void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup);
            serverBootstrap.channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port));
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast("codec", new HttpServerCodec())// HTTP 编解码
                            .addLast("compressor", new HttpContentCompressor())  // HttpContent 压缩
                            .addLast("aggregator", new HttpObjectAggregator(65536)) // HTTP 消息聚合
                            .addLast("handler", new EMWConfigManagerRestController()); // 自定义业务逻辑处理器
                }
            });
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.bind(port).sync();

            logger.info("EMW rest Server started success, listening on port: " + port);
        } catch (Exception e) {
            logger.info("EMW rest Server started failed. ", e);
            throw new EMWConfigException("EMW rest Server started failed. ", e);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        String uri = fullHttpRequest.uri();
        HttpMethod httpMethod = fullHttpRequest.method();
        String content = fullHttpRequest.content().toString(CharsetUtil.UTF_8);

        logger.info("Received emw config rest request. uri = {}, method = {}, content = {}", uri, httpMethod, content);

        String response = "";
        if (uri.equalsIgnoreCase("/emw/config") && httpMethod.equals(HttpMethod.GET)) {
            ConfigDO savedConfig = get(content);
            response = gson.toJson(savedConfig);
        }

        if (uri.equalsIgnoreCase("/emw/config") && (httpMethod.equals(HttpMethod.POST) || httpMethod.equals(HttpMethod.PUT))) {
            ConfigDO savedConfig = saveOrUpdate(content);
            response = gson.toJson(savedConfig);
        }

        //do response
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(response.getBytes()));
        channelHandlerContext.writeAndFlush(defaultFullHttpResponse).addListener(ChannelFutureListener.CLOSE);
    }

    private ConfigDO get(String content) {
        ConfigDO configDO = gson.fromJson(content, ConfigDO.class);

        return EMWConfigManagerFacade.findByNamespace(configDO.getNamespace(), configDO.getProfile());
    }

    private ConfigDO saveOrUpdate(String content) {
        ConfigDO configDO = gson.fromJson(content, ConfigDO.class);

        return EMWConfigManagerFacade.createOrUpdateNamespace(configDO);
    }
}