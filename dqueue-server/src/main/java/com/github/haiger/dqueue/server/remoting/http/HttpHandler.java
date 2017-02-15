package com.github.haiger.dqueue.server.remoting.http;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.haiger.dqueue.common.protocol.Request;
import com.github.haiger.dqueue.common.protocol.RequestCode;
import com.github.haiger.dqueue.common.protocol.Response;
import com.github.haiger.dqueue.common.protocol.ResponseType;
import com.github.haiger.dqueue.common.protocol.codec.JsonCodec;
import com.github.haiger.dqueue.server.processer.ProcesserFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * @author haiger
 * @since 2017年2月14日 下午8:00:51
 */
public class HttpHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(HttpHandler.class);
    private ProcesserFactory processerFactory;
    
    public HttpHandler(ProcesserFactory processerFactory) {
        this.processerFactory = processerFactory;
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("HttpHandler goes wrong at:{}", cause);
        ctx.close();
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest httpRequest = (FullHttpRequest)msg;
        String requestContent = httpRequest.content().toString(Charset.forName("utf-8"));
        Response response = null;
        try {
            Request request = JsonCodec.decodeRequest(requestContent);
            switch (RequestCode.valueOf(request.getCode())) {
            case PUB:
                response = processerFactory.getProcesser(RequestCode.PUB).processer(request);
                break;
            case MPUB:
                
                break;

            case FIN:
                
                break;
            case DEL:
                
                break;
            case POP:
                
                break;
            case MPOP:
                break;
            default:
                writeErrorResponse(ctx.channel(), "this ops can not be supported.", (HttpRequest)msg);
                break;
            }
            
            writeResponse(ctx.channel(), response, (HttpRequest)msg);
        } catch (Exception e) {
            writeErrorResponse(ctx.channel(), e.getMessage(), (HttpRequest)msg);
            log.error("httpHandler deal exception at:", e);
        }
    }
    
    private void writeErrorResponse(Channel channel, String errorInfo, HttpRequest httpRequest) {
        Response response = new Response();
        response.setType(ResponseType.ERROR.getType());
        response.setSuccess(false);
        response.setErrorInfo(errorInfo);
        writeResponse(channel, response, httpRequest);
    }
    
    private void writeResponse(Channel channel, Response response, HttpRequest httpRequest) {
        ByteBuf buf = Unpooled.copiedBuffer(JsonCodec.encodeResponse(response), CharsetUtil.UTF_8);
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

        String contentType = "text/html; charset=UTF-8";
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);

        boolean close = httpRequest.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE, true)
                || httpRequest.protocolVersion().equals(HttpVersion.HTTP_1_0)
                || !httpRequest.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE, true);

        if (!close) {
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
        }

        ChannelFuture future = channel.writeAndFlush(response);
        future.addListener(ChannelFutureListener.CLOSE);
    }
}
