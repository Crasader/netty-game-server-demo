package org.xialei.game.msg.account;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.xialei.game.annotation.Protocol;
import org.xialei.game.data.Command;
import org.xialei.game.data.Module;
import org.xialei.game.msg.Message;

@Protocol(module = Module.ACCOUNT, cmd = Command.LOGIN_REQ)
public class LoginReqMessage extends Message {
    @Protobuf(order = 1)
    private String username;
    @Protobuf(order = 2)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginReqMessage [username=" + username + ", password=" + password + "]";
    }
}
