package pers.spl.ziot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.spl.ziot.authentication.JwtUitls;
import pers.spl.ziot.authentication.PrivateMethod;
import pers.spl.ziot.service.UserService;

import java.util.Map;

@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    JwtUitls jwtUitls;
    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody Map<String,String> info) {
        String username = info.get("username");
        String password = info.get("password");
        return userService.login(username, password);
    }
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String,String> info) {
        String email = info.get("email");
        String username = info.get("username");
        String password = info.get("password");
        return userService.register(email,username, password);
    }
    @PrivateMethod
    @GetMapping("/getInfo")
    public Map<String,Object> getInfo(@RequestParam("id") int id) {
        return userService.getInfo(id);
    }
    @PrivateMethod
    @PostMapping("/modify")
    public Map<String,Object> modify(@RequestBody Map<String,String> info) {
        return userService.modify(info);
    }
}