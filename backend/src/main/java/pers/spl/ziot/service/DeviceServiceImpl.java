package pers.spl.ziot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pers.spl.ziot.dao.DeviceMapper;
import pers.spl.ziot.dao.MessageMapper;
import pers.spl.ziot.dao.UserMapper;
import pers.spl.ziot.model.Device;
import pers.spl.ziot.model.Message;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeviceServiceImpl implements DeviceService{
    @Autowired
    DeviceMapper deviceMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    MessageMapper messageMapper;
    @Value("${message.limit}")
    private int messageLimit;
    static String prefix = "device";
    @Override
    public Map<String, Object> getInfo(int id) {
        Device device = deviceMapper.selectByPrimaryKey(id);
        Map<String, Object> result = new HashMap<String, Object>();
        if (device == null) {
            result.put("status", 1);
        } else{
            result.put("status", 0);
            result.put("name", device.getName());
            result.put("activateTime", device.getActivateTime());
            result.put("category", device.getType());
            result.put("description", device.getDescription());
            result.put("state", device.getState());
            result.put("owner", userMapper.selectByPrimaryKey(device.getOwnerId()).getName());
        }
        return result;
    }
    @Override
    public Map<String,Object>  modify(int id, String name, int type, String description) {
        Device device = deviceMapper.selectByPrimaryKey(id);
        Map<String,Object> result = new HashMap<String,Object>();
        if (device == null) {
            result.put("code", 1);
            result.put("message", "设备不存在");
            return result;
        }
        device.setName(name);
        device.setType(type);
        device.setDescription(description);
        deviceMapper.updateByPrimaryKey(device);
        result.put("code", 0);
        return result;
    }
    @Override
    public Map<String,Object> add(String name, String owner_id, String type,String description) {
        Device device = new Device();
        device.setName(name);
        device.setOwnerId(Integer.parseInt(owner_id));
        device.setType(Integer.parseInt(type));
        device.setDescription(description);
        device.setState(0);
        deviceMapper.insert(device);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 0);
        return result;
    }

    @Override
    public Map<String, Integer> getStat(int id) {
        List<Device> list = deviceMapper.selectByOwner(id);
        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put("total", list.size());
        int online = 0, car = 0, home = 0, wearableDevice = 0, other = 0,messages = 0,todayMessages = 0,good=0,alert=0;
        for (Device device : list) {
            //如果activateTime不为空，且与当前时间间隔不大于一分钟
            if (device.getActivateTime() != null && device.getActivateTime().getTime() + 60000 > System.currentTimeMillis()) {
                online++;
            }
            if(device.getState()==0)
                good++;
            else alert++;
            switch (device.getType()) {
                case 1:
                    car++;
                    break;
                case 2:
                    home++;
                    break;
                case 3:
                    wearableDevice++;
                    break;
                default:
                    other++;
                    break;
            }
            List<Message> messageList = messageMapper.selectByDevice(device.getId());
            messages += messageList.size();
            LocalDate today = LocalDate.now();
            // 创建今天的 0 点时间
            LocalDateTime todayStart = today.atTime(LocalTime.MIN);
            //获取0点以后的消息数量
            for (Message message : messageList) {
                if (message.getTime().getTime() > todayStart.toInstant(java.time.ZoneOffset.of("+8")).toEpochMilli()) {
                    todayMessages++;
                }
            }
        }
        result.put("good", good);
        result.put("alert", alert);
        result.put("online", online);
        result.put("car", car);
        result.put("home", home);
        result.put("wearableDevice", wearableDevice);
        result.put("other", other);
        result.put("messages", messages);
        result.put("todayMessages", todayMessages);
        return result;
    }
    @Override
    public List<Device> getList(int id) {
        List<Device> list = deviceMapper.selectByOwner(id);
        return list;
    }
    @Override
    public List<Map<String, Object>>getMessages(int id) {
        List<Message> list = messageMapper.selectByDevice(id);
        List<Map<String, Object>> result = new java.util.ArrayList<Map<String, Object>>();
        for (Message message : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            String name = deviceMapper.selectByPrimaryKey(message.getDeviceId()).getName();
            map.put("id", message.getId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedTime = dateFormat.format(message.getTime());
            map.put("time", formattedTime);
            map.put("info", message.getInfo());
            map.put("value", message.getValue());
            map.put("alert", message.getAlert());
            map.put("name", name);
            map.put("lat", message.getLat());
            map.put("lng", message.getLng());
            map.put("device_id", message.getDeviceId());
            result.add(map);
        }
        return result;
    }
    @Override
    public List<Map<String, Object>>getLatestMessages(int id) {
        List<Message> list = messageMapper.selectByDevice(id);
        List<Map<String, Object>> result = new java.util.ArrayList<Map<String, Object>>();
        for (int i = 0; i < Math.min(messageLimit, list.size()); i++) {
            Message message = list.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            String name = deviceMapper.selectByPrimaryKey(message.getDeviceId()).getName();
            map.put("id", message.getId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedTime = dateFormat.format(message.getTime());
            map.put("time", formattedTime);
            map.put("info", message.getInfo());
            map.put("value", message.getValue());
            map.put("alert", message.getAlert());
            map.put("name", name);
            map.put("lat", message.getLat());
            map.put("lng", message.getLng());
            map.put("device_id", message.getDeviceId());
            result.add(map);
        }

        return result;
    }
}
