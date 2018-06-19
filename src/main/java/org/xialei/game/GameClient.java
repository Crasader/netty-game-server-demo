package org.xialei.game;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xialei.game.codec.MessageDecoder;
import org.xialei.game.codec.MessageEncoder;
import org.xialei.game.handler.GameServerHandler;
import org.xialei.game.msg.Message;
import org.xialei.game.msg.account.LoginReqMessage;

public class GameClient {
    private String host;
    private int port;
    private final static Logger logger = LoggerFactory.getLogger(GameClient.class);

    public GameClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: " + GameClient.class.getSimpleName() + " <host> <port>");
            return;
        }
        new GameClient(args[0], Integer.parseInt(args[1])).start();
    }

    public class GameClientHandler extends SimpleChannelInboundHandler<Message> {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            logger.info("{} 已连接", ctx.channel().remoteAddress());
            LoginReqMessage m = new LoginReqMessage();
            m.setUsername("xialei");
            m.setPassword("1111111");
            ctx.writeAndFlush(m);
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

        protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
            logger.info("{} {}", ctx.channel().remoteAddress(), message);
        }
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        try {
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(host, port)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new MessageDecoder());
                            ch.pipeline().addLast(new MessageEncoder());
                            ch.pipeline().addLast(new GameClientHandler());
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
