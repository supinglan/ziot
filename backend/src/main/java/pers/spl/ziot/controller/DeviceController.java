package pers.spl.ziot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.spl.ziot.authentication.JwtUitls;
import pers.spl.ziot.authentication.PrivateMethod;
import pers.spl.ziot.model.Device;
import pers.spl.ziot.model.Message;
import pers.spl.ziot.service.DeviceService;

import java.util.List;
import java.util.Map;

@RequestMapping("/device")
@RestController
public class DeviceController {
    @Autowired
    DeviceService deviceService;
    @Autowired
    JwtUitls jwtUitls;
    @PrivateMethod
    @GetMapping("/getInfo")
    public Map<String,Object> getInfo(@RequestParam("id") int id) {
        return deviceService.getInfo(id);
    }
    @PrivateMethod
    @PostMapping("/modify")
    public Map<String, Object> modify(@RequestBody Map<String,String> info) {
        String name = info.get("name");
        int device_id = Integer.parseInt(info.get("deviceid"));
        int  type = Integer.parseInt(info.get("type"));
        String description = info.get("description");
        return deviceService.modify(device_id,name,type,description);
    }
    @PrivateMethod
    @PostMapping("/add")
    public Map<String,Object> add(@RequestBody Map<String,String> info) {
        String name = info.get("name");
        String owner_id = info.get("id");
        String type = info.get("type");
        String description = info.get("description");
        return deviceService.add(name,owner_id,type,description);
    }
    @PrivateMethod
    @GetMapping("/getStat")
    public Map<String, Integer> getStat(@RequestParam("id") int id)
    {
        return deviceService.getStat(id);
    }
    @PrivateMethod
    @GetMapping("/getList")
    public List<Device> getList(@RequestParam("id") int id)
    {
        return deviceService.getList(id);
    }
    @PrivateMethod
    @GetMapping("/getMessages")
    public List<Map<String, Object>> getMessages(@RequestParam("id") int id)
    {
        return deviceService.getMessages(id);
    }
    @PrivateMethod
    @GetMapping("/getLatestMessages")
    public List<Map<String, Object>> getLatestMessages(@RequestParam("id") int id)
    {
        return deviceService.getLatestMessages(id);
    }
}