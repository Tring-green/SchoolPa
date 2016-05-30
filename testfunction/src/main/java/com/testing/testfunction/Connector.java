package com.testing.testfunction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by admin on 2016/5/29.
 */
public class Connector {

    private Socket mClient;
    private String mDstName = "10.0.2.2";
    private int mDstPort = 10000;
    private ConnectorListener mListener;

    private ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(8);

    public Connector() {

    }

    public void connect() {
        try {
            if (mClient == null || mClient.isClosed()) {
                mClient = new Socket(mDstName, mDstPort);
            }

            new Thread(new RequestWorker()).start();

            InputStream is = mClient.getInputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {

                final String text = new String(buffer, 0, len);

                if (mListener != null) {
                    mListener.pushData(text);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public class RequestWorker implements Runnable {

        @Override
        public void run() {
            OutputStream os;
            try {
                os = mClient.getOutputStream();
                while (true) {
                    String content = queue.take();
                    os.write(content.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void auth(String auth) {
        if (mClient == null || mClient.isClosed()) {
            try {
                mClient = new Socket(mDstName, mDstPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        putRequest(auth);
        //OutputStream os;
        //try {
        //    os = mClient.getOutputStream();
        //    os.write(auth.getBytes());
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
    }

    //public void send(String content) {
    //    OutputStream os;
    //    try {
    //        os = mClient.getOutputStream();
    //        os.write(content.getBytes());
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    }
    //}

    public void putRequest(String content) {
        try {
            queue.put(content);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void disconncet() {
        try {
            if (mClient != null && !mClient.isClosed()) {
                mClient.close();
                mClient = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setConnectorListener(ConnectorListener listener) {
        mListener = listener;
    }

    public interface ConnectorListener {
        void pushData(String data);
    }
}
