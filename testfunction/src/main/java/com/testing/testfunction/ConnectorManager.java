package com.testing.testfunction;

import com.testing.testfunction.request.AuthRequest;
import com.testing.testfunction.request.Request;

/**
 * Created by admin on 2016/5/29.
 */
public class ConnectorManager implements Connector.ConnectorListener {
    private Connector mConnector;
    private static ConnectorManager instance;
    private ConnectorListener mListener;

    public static ConnectorManager getInstance() {
        if (instance == null) {
            synchronized (ConnectorManager.class) {
                if (instance == null) {
                    instance = new ConnectorManager();
                }
            }
        }
        return instance;
    }

    private ConnectorManager() {

    }

    public void connect(String auth) {
        mConnector = new Connector();
        mConnector.setConnectorListener(this);
        mConnector.auth(auth);
        mConnector.connect();

    }

    public void connect(AuthRequest auth) {
        mConnector = new Connector();
        mConnector.setConnectorListener(this);
        mConnector.auth(auth.getData());
        mConnector.connect();

    }


    public void putRequest(String request) {
        mConnector.putRequest(request);
    }

    public void putRequest(Request request) {
        mConnector.putRequest(request.getData());
    }

    @Override
    public void pushData(String data) {
        if (mListener != null) {
            mListener.pushData(data);
        }
    }

    public void setConnectorListener(ConnectorListener listener) {
        mListener = listener;
    }

    public interface ConnectorListener {
        void pushData(String data);
    }
}
