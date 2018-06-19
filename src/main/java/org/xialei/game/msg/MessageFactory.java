package org.xialei.game.msg;

import org.xialei.game.msg.account.LoginReqMessage;
import org.xialei.game.msg.account.LoginRespMessage;

import java.util.HashMap;

public class MessageFactory {
    private static MessageFactory instance = new MessageFactory();
    private HashMap<String, Class<?>> map = new HashMap();

    private MessageFactory() {
        // 载入消息
        map.put("1-1", LoginReqMessage.class);
        map.put("1-2", LoginRespMessage.class);
    }

    public static MessageFactory getInstance() {
        return instance;
    }

    public Class<?> getMessage(short module, short cmd) {
        String key = module + "-" + cmd;
        return map.get(key);
    }
}
