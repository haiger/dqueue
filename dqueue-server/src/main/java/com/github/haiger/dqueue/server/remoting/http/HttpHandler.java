package com.github.haiger.dqueue.server.remoting.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.github.haiger.dqueue.common.protocol.RequestCode;
import com.github.haiger.dqueue.common.protocol.codec.JsonCodec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;

/**
 * @author haiger
 * @since 2017年2月14日 下午8:00:51
 */
public class HttpHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(HttpHandler.class);
    private DefaultHttpDataFactory httpDataFactory;
    
    public HttpHandler() {
        httpDataFactory = new DefaultHttpDataFactory(false);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("HttpHandler goes wrong at:{}", cause);
        ctx.close();
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String code = getRequestCode(((HttpRequest) msg).uri());
        try {
            Map<String, String> params = parsePostParams(msg);
            switch (RequestCode.valueOf(code)) {
            case PUB:
                
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
                writeResponse(ctx.channel(), 
                        JsonCodec.encodeErrorResponse("this ops not support."), (HttpRequest) msg);
                break;
            }
        } catch (Exception e) {
            writeResponse(ctx.channel(), JsonCodec.encodeErrorResponse(e.getMessage()), (HttpRequest) msg);
            log.error("httpHandler deal exception at:", e);
        }
    }
    
    private String getRequestCode(String uri) {
        int index = uri.indexOf("?");
        if (index != -1) {
            uri = uri.substring(0, index);
        }
        return uri.substring(1);
    }
    
    private Map<String, String> parsePostParams(Object msg) throws IOException {
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(httpDataFactory, (HttpRequest) msg);
        decoder.offer((HttpContent) msg);

        Map<String, String> params = new HashMap<String, String>();
        try {
            while (decoder.hasNext()) {
                InterfaceHttpData data = decoder.next();
                if (data != null) {
                    try {
                        if (data.getHttpDataType() == HttpDataType.Attribute) {
                            Attribute attribute = (Attribute) data;
                            params.put(attribute.getName(), attribute.getValue());
                        }
                    } finally {
                        data.release();
                    }
                }
            }
        } catch (EndOfDataDecoderException e) {
            log.info("参数获取结束((map=)" + JSON.toJSON(params));
        }
        return params;
    }
    
    private Map<String, String> parseGetParams(Object msg) throws IOException {
        return parseGetParamsFromStr(((HttpRequest) msg).uri());
    }

    private Map<String, String> parseGetParamsFromStr(String uri) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        
        int index = uri.indexOf("?");
        if (index == -1) {
            return params;
        }

        String paramStr = uri.substring(index + 1);
        String[] paramArray = paramStr.split("&");
        if (paramArray != null && paramArray.length > 0) {
            for (String param : paramArray) {
                String[] items = param.split("=");
                if (items.length > 1) {
                    params.put(items[0], items[1]);
                }
            }
        }

        return params;
    }

    private void writeResponse(Channel channel, String printStr, HttpRequest request) {
        ByteBuf buf = Unpooled.copiedBuffer(printStr, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

        String contentType = "text/html; charset=UTF-8";
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);

        boolean close = request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE, true)
                || request.protocolVersion().equals(HttpVersion.HTTP_1_0)
                && !request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE, true);

        if (!close) {
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
        }

        ChannelFuture future = channel.writeAndFlush(response);
        future.addListener(ChannelFutureListener.CLOSE);
    }
}
