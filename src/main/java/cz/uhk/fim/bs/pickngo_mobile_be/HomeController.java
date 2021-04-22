package cz.uhk.fim.bs.pickngo_mobile_be;



import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path="")
public class HomeController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String home(){
        return "čau";
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Map<String, Object> detail(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;

        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz
        String email_short = authentication.getPrincipal().getAttribute("preferred_username"); //vraci kratsi1@uhk.cz
        String full_name = authentication.getPrincipal().getAttribute("name"); //vraci prijmeni jmeno

        HashMap<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("email_short", email_short);
        map.put("full_name", full_name);

       //authentication.getPrincipal().getAttributes(); //obsahuje vse
        return map;
    }

    @RequestMapping(value = "/git", method = RequestMethod.GET)
    public String homeGit(){
        return "čau tohle je zkouška automatického nasazení z gitu";
    }


}
