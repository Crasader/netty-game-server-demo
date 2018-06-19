package org.xialei.game.msg;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import org.junit.Assert;
import org.junit.Test;
import org.xialei.game.msg.account.LoginReqMessage;

import java.io.IOException;

public class LoginReqMessageTest {
    @Test
    public void testProtobuf() {
        LoginReqMessage loginReqMessage = new LoginReqMessage();
        loginReqMessage.setUsername("xialei");
        loginReqMessage.setPassword("111111");
        Codec<LoginReqMessage> codec = ProtobufProxy.create(LoginReqMessage.class);

        try {
            byte[] bytes = codec.encode(loginReqMessage);
            LoginReqMessage msg2 = codec.decode(bytes);
            Assert.assertEquals(loginReqMessage.getUsername(), msg2.getUsername());
            Assert.assertEquals(loginReqMessage.getPassword(), msg2.getPassword());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}