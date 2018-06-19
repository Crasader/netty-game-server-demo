package org.xialei.game;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xialei.game.codec.MessageDecoder;
import org.xialei.game.codec.MessageEncoder;
import org.xialei.game.handler.GameServerHandler;

public class GameServer {
    private int port;
    private static final Logger logger = LoggerFactory.getLogger(GameServer.class);

    public GameServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: " + GameServer.class.getSimpleName() + " <port>");
            return;
        }
        new GameServer(Integer.parseInt(args[0])).start();
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new MessageDecoder());
                            ch.pipeline().addLast(new MessageEncoder());
                            ch.pipeline().addLast( new GameServerHandler());
                        }
                    });
            ChannelFuture f = b.bind().sync();
            logger.info("listen on {}", f.channel().localAddress());
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }
    }
}
