package cz.sangis.personal.cv_builder_app.users;

import cz.sangis.personal.cv_builder_app.core.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController extends BaseController {
    @GetMapping("/register")
    public String register(){
    return "register";
    }

    @GetMapping("/me")
    public String me(){
        return "me";
    }



}
