package org.xialei.game.dispatcher;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xialei.game.data.Command;
import org.xialei.game.msg.Message;
import org.xialei.game.msg.account.LoginReqMessage;
import org.xialei.game.msg.account.LoginRespMessage;

public class AccountDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(AccountDispatcher.class.getSimpleName());
    private static AccountDispatcher dispatcher = new AccountDispatcher();

    public static AccountDispatcher getInstance() {
        return dispatcher;
    }

    public void dispatch(ChannelHandlerContext ctx, Message request) {
        Message response = null;
        switch (request.getCmd()) {
            case Command.LOGIN_REQ:
                response = handleLoginRequest((LoginReqMessage) request);
                break;
            default:
                break;
        }
        if (response != null) {
            ctx.writeAndFlush(response);
            ReferenceCountUtil.release(request);
        }
    }

    private Message handleLoginRequest(LoginReqMessage message) {
        LoginRespMessage loginRespMessage = new LoginRespMessage();
        if (!message.getUsername().equals("xialei") || !message.getPassword().equals("111111")) {
            loginRespMessage.setSucceed(false);
            loginRespMessage.setData("账号或密码错误");
        } else {
            loginRespMessage.setSucceed(true);
            loginRespMessage.setData("token");
        }
        return loginRespMessage;
    }
}
