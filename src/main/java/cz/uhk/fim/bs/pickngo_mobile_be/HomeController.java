package cz.uhk.fim.bs.pickngo_mobile_be;



import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;

        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz
        String email_short = authentication.getPrincipal().getAttribute("email"); //vraci kratsi1@uhk.cz
        String full_name = authentication.getPrincipal().getAttribute("name"); //vraci jmeno.prijmeni@uhk.cz

        return "Hello there General "+ full_name + ", email: "+ email+", short email: "+ email_short;
    }


}
