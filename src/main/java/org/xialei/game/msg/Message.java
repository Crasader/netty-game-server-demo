package org.xialei.game.msg;

import org.xialei.game.annotation.Protocol;

public class Message {
    public short getModule() {
        Protocol protocol = getClass().getAnnotation(Protocol.class);
        if (protocol != null) {
            return protocol.module();
        }
        return 0;
    }

    public short getCmd() {
        Protocol protocol = getClass().getAnnotation(Protocol.class);
        if (protocol != null) {
            return protocol.cmd();
        }
        return 0;
    }

    public String key() {
        return getModule() + "-" + getCmd();
    }
}
