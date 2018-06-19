package org.xialei.game.msg.account;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.xialei.game.annotation.Protocol;
import org.xialei.game.data.Command;
import org.xialei.game.data.Module;
import org.xialei.game.msg.Message;

@Protocol(module = Module.ACCOUNT, cmd = Command.LOGIN_RESPONSE)
public class LoginRespMessage extends Message {
    @Protobuf(order = 1)
    private boolean succeed;
    @Protobuf(order = 2)
    private String data;

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginRespMessage [succeed=" + succeed + ", data=" + data + "]";
    }
}
