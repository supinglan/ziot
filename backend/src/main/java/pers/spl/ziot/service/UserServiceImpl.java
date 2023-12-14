package pers.spl.ziot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pers.spl.ziot.authentication.JwtUitls;
import pers.spl.ziot.dao.UserMapper;
import pers.spl.ziot.model.User;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userDao;
    @Autowired
    JwtUitls jwtUitls;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Override
    public Map<String, Object> login(String username, String password) {
        User user = userDao.selectByName(username);
        Map<String, Object> result = new HashMap<String, Object>();
        if (user == null) {
            result.put("message", "用户不存在");
        } else if (encoder.matches(password, user.getPassword())) {
//            System.out.println(username + " login success");
            result.put("token", jwtUitls.createToken(user));
            result.put("id", user.getId());
        } else {
            result.put("message", "密码错误");
        }
        return result;
    }

    @Override
    public Map<String, Object> register(String email, String username, String password) {

        Map<String, Object> result = new HashMap<String, Object>();
        User user = userDao.selectByEmail(email);
        if (user != null) {
            result.put("message", "电子邮件已被注册");
            return result;
        }
        user = userDao.selectByName(username);
        if (user != null) {
            result.put("message", "用户名已被注册");
            return result;
        }
        user = new User();
        user.setEmail(email);
        user.setName(username);
        String encodePassword = encoder.encode(password);
        user.setPassword(encodePassword);
        userDao.insert(user);
        return result;
    }
    @Override
    public Map<String, Object> getInfo(int id) {
        User user = userDao.selectByPrimaryKey(id);
        Map<String, Object> result = new HashMap<String, Object>();
        if (user == null) {
            result.put("message", "用户不存在");
        } else{
            result.put("email", user.getEmail());
            result.put("username", user.getName());
            result.put("phone", user.getPhone());
            result.put("location", user.getLocation());
            result.put("address", user.getAddress());
            // 获取性别
            Integer sex = user.getSex();
            String sexString;
            if (sex != null) {
                switch (sex) {
                    case 0:
                        sexString = "男";
                        break;
                    case 1:
                        sexString = "女";
                        break;
                    case 2:
                        sexString = "保密";
                        break;
                    default:
                        sexString = "";
                        break;
                }
            } else {
                sexString = "";
            }
            result.put("sex", sexString);

        }
        return result;
    }

    @Override
    public Map<String,Object> modify(Map<String,String> info) {
        User user = userDao.selectByPrimaryKey(Integer.parseInt(info.get("id")));
        Map<String, Object> result = new HashMap<String, Object>();
        if (user == null) {
            result.put("message", "用户不存在");
            result.put("code",1);
            return result;
        }
        user.setPhone(info.get("phone"));
        user.setLocation(info.get("location"));
        user.setAddress(info.get("address"));
        String sexString=info.get("sex");
        int sex;
        switch (sexString) {
            case "男":
                sex = 0;
                break;
            case "女":
                sex = 1;
                break;
            case "保密":
                sex = 2;
                break;
            default:
                sex = -1;
                break;
        }
        user.setSex(sex);
        userDao.updateByPrimaryKey(user);
        result.put("code",0);
        return result;
    }
}
