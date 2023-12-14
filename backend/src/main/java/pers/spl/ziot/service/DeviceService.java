package pers.spl.ziot.service;

import pers.spl.ziot.model.Device;
import pers.spl.ziot.model.Message;

import java.util.List;
import java.util.Map;

public interface DeviceService {

    public Map<String, Object> getInfo(int id);
    public Map<String,Object>  modify(int id, String name, int type, String description);
    public Map<String,Object>  add(String name, String owner_id, String category,String description);
    public Map<String, Integer> getStat(int id);
    public List<Device> getList(int id);
    public List<Map<String, Object>> getMessages(int id);
    public List<Map<String, Object>> getLatestMessages(int id);
}
