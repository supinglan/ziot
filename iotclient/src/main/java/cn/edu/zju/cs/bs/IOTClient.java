package cn.edu.zju.cs.bs;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Vector;

public class IOTClient {
    public static void main(String[] args) {
        int devices = 5;
        String mqttServer = "tcp://120.55.14.3:1883";
        String topic = "testapp";
        String clientPrefix = "device";

        try {
            Vector<WorkerThread> threadVector = new Vector<WorkerThread>();
            for (int i = 0; i < devices; i++) {
                WorkerThread thread = new WorkerThread();
                thread.setDeviceId(i + 1);
                thread.setMqttServer(mqttServer);
                thread.setTopic(topic);
                thread.setClientPrefix(clientPrefix);
                threadVector.add(thread);
                thread.start();
            }
            for (WorkerThread thread : threadVector) {
                thread.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}