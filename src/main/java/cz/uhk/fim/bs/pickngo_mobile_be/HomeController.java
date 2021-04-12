package cz.uhk.fim.bs.pickngo_mobile_be;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("home")
    public String home(){
        return "Hello there";
    }
}
