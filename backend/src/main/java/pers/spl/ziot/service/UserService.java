package pers.spl.ziot.service;

import java.util.Map;

public interface UserService{
    public Map<String, Object> login(String username, String password);
    public Map<String, Object>  register(String email, String username, String password);
    public Map<String, Object> getInfo(int id);
    public Map<String,Object> modify(Map<String,String> info);

}
