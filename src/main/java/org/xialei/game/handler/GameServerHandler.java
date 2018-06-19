package org.xialei.game.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xialei.game.data.Module;
import org.xialei.game.dispatcher.AccountDispatcher;
import org.xialei.game.msg.Message;

public class GameServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(GameServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        logger.info("{} {}", ctx.channel().remoteAddress(), msg);
        switch (message.getModule()) {
            case Module.ACCOUNT:
                AccountDispatcher.getInstance().dispatch(ctx, message);
                break;
            default:
                logger.error("不支持的消息类型 模块{}, 类型{}", message.getModule(), message.getCmd());
                ctx.close();
                break;
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("{} 已连接", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("{} 断开连接", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("{} 异常{}", ctx.channel().remoteAddress(), cause);
        ctx.close();
    }
}
