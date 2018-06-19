package org.xialei.game.codec;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xialei.game.msg.Message;
import org.xialei.game.msg.MessageFactory;

import java.io.IOException;
import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
    private static final int HEADER_LENGTH = 8;
    private static final Logger logger = LoggerFactory.getLogger(MessageDecoder.class);

    /**
     * |---消息体长度---|--module--|--cmd--|
     * |---4字节-------|--2字节---|--2字节-|
     *
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < HEADER_LENGTH) {
            return;
        }
        int bodyLength = byteBuf.readInt();
        if (byteBuf.readableBytes() < HEADER_LENGTH - 4 + bodyLength) { // 去掉已经读取过的4字节
            return;
        }
        short module = byteBuf.readShort();
        short cmd = byteBuf.readShort();
        byte[] body = new byte[bodyLength];
        byteBuf.readBytes(body);

        Message message = decodeMessage(module, cmd, body);
        if (message != null) {
            logger.info("decode 模块{}, 类型{}", module, cmd);
            list.add(message);
        }
    }

    private Message decodeMessage(short module, short cmd, byte[] body) {
        Class<?> msgClass = MessageFactory.getInstance().getMessage(module, cmd);
        if (msgClass == null) {
            logger.warn("不支持的消息类型,模块{}, 类型{}", module, cmd);
            return null;
        }
        try {
            Codec<?> codec = ProtobufProxy.create(msgClass);
            return (Message) codec.decode(body);
        } catch (IOException e) {
            logger.error("读取消息失败,模块{}, 类型{}, 异常{}", module, cmd, e);
        }
        return null;
    }
}
