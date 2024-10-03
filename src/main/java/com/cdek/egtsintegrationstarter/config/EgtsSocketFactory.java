package com.cdek.egtsintegrationstarter.config;

import com.cdek.egtsintegrationstarter.config.properties.EgtsConfigProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;
import java.net.Socket;

@RequiredArgsConstructor
public class EgtsSocketFactory implements PooledObjectFactory<Socket> {

    private final EgtsConfigProperties egtsConfigProperties;

    @Override
    public void destroyObject(PooledObject<Socket> pooledObject) {
        Socket socket = pooledObject.getObject();
        try {
            socket.close();
        } catch (IOException e) {
            //ignore
        }
    }

    @Override
    public PooledObject<Socket> makeObject() throws Exception {
        return new DefaultPooledObject<>(new Socket(egtsConfigProperties.getHost(), egtsConfigProperties.getPort()));
    }

    @Override
    public void passivateObject(PooledObject<Socket> pooledObject) {
        //nothing
    }

    @Override
    public void activateObject(PooledObject<Socket> pooledObject) {
        //nothing
    }

    @Override
    public boolean validateObject(PooledObject<Socket> pooledObject) {
        Socket socket = pooledObject.getObject();
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}
