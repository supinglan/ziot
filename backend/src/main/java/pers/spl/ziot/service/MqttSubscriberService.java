package pers.spl.ziot.service;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;
import pers.spl.ziot.dao.DeviceMapper;
import pers.spl.ziot.dao.MessageMapper;
import pers.spl.ziot.model.Device;
import pers.spl.ziot.model.IOTMessage;
import com.alibaba.fastjson.JSONObject;
import pers.spl.ziot.model.Message;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class MqttSubscriberService {

    @Value("${mqtt.broker}")
    private String brokerUrl;

    @Value("${mqtt.clientId}")
    private String clientId;

    @Value("${mqtt.topic}")
    private String topic;
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    DeviceMapper deviceMapper;
    static String prefix = "device";

    private MqttClient mqttClient;
    @PostConstruct
    public void subscribe() throws MqttException {
            mqttClient = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            mqttClient.connect(connOpts);
            System.out.println("Connected to broker: " + brokerUrl);
            mqttClient.subscribe(topic, (topic, message) -> {
//                System.out.println("Message received:");
//                System.out.println("\tTopic: " + topic);
//                System.out.println("\tMessage: " + new String(message.getPayload()));
                String content = new String(message.getPayload());
//                System.out.println("Received message: " + content);
                // 将 JSON 转换为对象
                IOTMessage receivedMessage = JSONObject.parseObject(content, IOTMessage.class);

                //For debug
//                System.out.println("Client ID: " + receivedMessage.getClientId());
//                System.out.println("Info: " + receivedMessage.getInfo());
//                System.out.println("Value: " + receivedMessage.getValue());
//                System.out.println("Alert: " + receivedMessage.getAlert());
//                System.out.println("Longitude: " + receivedMessage.getLng());
//                System.out.println("Latitude: " + receivedMessage.getLat());
//                System.out.println("Timestamp: " + receivedMessage.getTimestamp());
                String device_id = receivedMessage.getClientId();
                Integer id =Integer.parseInt(device_id.substring(prefix.length()));
                Device device = deviceMapper.selectByPrimaryKey(id);
                if(device!=null){
                    device.setActivateTime(new java.sql.Date(receivedMessage.getTimestamp()));
                    device.setState(receivedMessage.getAlert());
                    deviceMapper.updateByPrimaryKey(device);
                    Message messageToSave = new Message();
                    messageToSave.setDeviceId(id);
                    messageToSave.setTime(new java.sql.Date(receivedMessage.getTimestamp()));
                    messageToSave.setInfo(receivedMessage.getInfo());
                    messageToSave.setValue(receivedMessage.getValue());
                    messageToSave.setAlert(receivedMessage.getAlert());
                    messageToSave.setLng( receivedMessage.getLng());
                    messageToSave.setLat(receivedMessage.getLat());
                    try {
                        messageMapper.insert(messageToSave);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


    }

    @PreDestroy
    public void unsubscribe() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
                System.out.println("Disconnected from MQTT broker");
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
