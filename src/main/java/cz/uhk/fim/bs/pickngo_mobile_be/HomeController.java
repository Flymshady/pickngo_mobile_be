package cz.uhk.fim.bs.pickngo_mobile_be;


import cz.uhk.fim.bs.pickngo_mobile_be.Customer.Customer;
import jdk.nashorn.internal.parser.JSONParser;
import net.minidev.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="")
public class HomeController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String home(){
        return "čau";
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Object detail(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth;
    }
}
