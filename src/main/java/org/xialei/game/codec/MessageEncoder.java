package org.xialei.game.codec;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xialei.game.msg.Message;

import java.io.IOException;

public class MessageEncoder extends MessageToByteEncoder<Message> {
    private static final Logger logger = LoggerFactory.getLogger(MessageEncoder.class.getSimpleName());

    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        byte[] body = encodeMessage(message);
        // 写入消息长度
        byteBuf.writeInt(body.length);
        // 写入模块号
        byteBuf.writeShort(message.getModule());
        // 写入子类型
        byteBuf.writeShort(message.getCmd());
        // 写入消息体
        byteBuf.writeBytes(body);
        logger.info("encode 模块{}, 类型{}", message.getModule(), message.getCmd());
    }

    private byte[] encodeMessage(Message message) {
        byte[] body = null;
        Class<Message> msgClass = (Class<Message>) message.getClass(); // 获取派生类
        try {
            Codec<Message> codec = ProtobufProxy.create(msgClass);// 使用派生类创建
            body = codec.encode(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }
}
