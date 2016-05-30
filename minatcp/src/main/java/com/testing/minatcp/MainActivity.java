package com.testing.minatcp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

public class MainActivity extends AppCompatActivity implements IoHandler {
    private EditText mEt_content;
    private IoSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEt_content = (EditText) findViewById(R.id.content);

    }

    public void clickMessage(View view) {
        final String content = mEt_content.getText().toString();
        if (content == null && content.equals("")) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String host = "10.0.2.2";
                int port = 10003;
                NioSocketConnector connector = new NioSocketConnector();
                connector.setHandler(MainActivity.this);
                ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
                future.awaitUninterruptibly();
                session = future.getSession();
                session.write(content);
                session.close(true);
            }
        }).start();
    }


    @Override
    public void sessionCreated(IoSession session) throws Exception {

    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {

    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        System.out.println("send: " + message);
    }
}
