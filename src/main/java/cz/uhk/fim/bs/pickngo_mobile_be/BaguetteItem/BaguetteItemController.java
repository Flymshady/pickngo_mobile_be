package cz.uhk.fim.bs.pickngo_mobile_be.BaguetteItem;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path="/baguetteItem")
public class BaguetteItemController {

    private final BaguetteItemService baguetteItemService;

    @Autowired
    public BaguetteItemController(BaguetteItemService baguetteItemService) {
        this.baguetteItemService = baguetteItemService;
    }
    @RequestMapping(value = "/all/{baguetteOrderId}", method = RequestMethod.GET)
    public List<BaguetteItem> getBaguetteItems(@PathVariable("baguetteOrderId") Long baguetteOrderId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;
        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz
        return baguetteItemService.getBaguetteItems(baguetteOrderId, email);
    }

    @RequestMapping(value = "/detail/{baguetteItemId}", method = RequestMethod.GET)
    public Optional<BaguetteItem> getBaguetteItem(@PathVariable("baguetteItemId") Long baguetteItemId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;
        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz
        return baguetteItemService.getBaguetteItem(baguetteItemId, email);
    }

    @RequestMapping(value = "/remove/{baguetteItemId}", method = RequestMethod.DELETE)
    public void removeBaguetteItem(@PathVariable("baguetteItemId") Long baguetteItemId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;
        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz
        baguetteItemService.removeBaguetteItem(baguetteItemId, email);
    }
    @RequestMapping(value = "/create/{baguetteOrderId}", method = RequestMethod.POST)
    public BaguetteItem createBaguetteItem(@PathVariable("baguetteOrderId") Long baguetteOrderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;
        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz
        return baguetteItemService.createBaguetteItem(baguetteOrderId, email);
    }

    @RequestMapping(value = "/create/{baguetteOrderId}/{specialOfferId}", method = RequestMethod.POST)
    public BaguetteItem createBaguetteItemFromSpecialOffer(@PathVariable("baguetteOrderId") Long baguetteOrderId, @PathVariable("specialOfferId") Long specialOfferId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;
        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz
        return baguetteItemService.createBaguetteItemFromSpecialOffer(baguetteOrderId, specialOfferId, email);
    }

}
