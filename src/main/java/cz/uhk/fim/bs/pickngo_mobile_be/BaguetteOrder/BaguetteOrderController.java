package cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/baguetteOrder")
public class BaguetteOrderController {


    private final BaguetteOrderService baguetteOrderService;

    @Autowired
    public BaguetteOrderController(BaguetteOrderService baguetteOrderService) {
        this.baguetteOrderService = baguetteOrderService;
    }
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<BaguetteOrder> getBaguetteOrders(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;
        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz
        return baguetteOrderService.getBaguetteOrders(email);
    }

    @RequestMapping(value = "/all/{state}", method = RequestMethod.GET)
    public List<BaguetteOrder> getBaguetteOrdersByState(@PathVariable("state") int state){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;
        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz
        return baguetteOrderService.getBaguetteOrdersByState(email, state);
    }

    @RequestMapping(value = "/detail/{baguetteOrderId}", method = RequestMethod.GET)
    public BaguetteOrder getBaguetteOrder(@PathVariable("baguetteOrderId") Long baguetteOrderId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;
        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz
        return baguetteOrderService.getBaguetteOrderId(baguetteOrderId, email);
    }

    @RequestMapping(value = "/remove/{baguetteOrderId}", method = RequestMethod.DELETE)
    public void removeBaguetteOrder(@PathVariable("baguetteOrderId") Long baguetteOrderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;
        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz
        baguetteOrderService.removeBaguetteOrder(baguetteOrderId, email);
    }

    @RequestMapping(value = "/update/{baguetteOrderId}", method = RequestMethod.PUT)
    public void updateBaguetteOrder(@PathVariable("baguetteOrderId") Long baguetteOrderId, @RequestParam(required = false) String note) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;
        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz
        baguetteOrderService.updateBaguetteOrder(baguetteOrderId, note, email);
    }

    @RequestMapping(value = "/confirm/{baguetteOrderId}", method = RequestMethod.PUT)
    public void confirmBaguetteOrder(@PathVariable("baguetteOrderId") Long baguetteOrderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;
        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz
        baguetteOrderService.confirmBaguetteOrder(baguetteOrderId, email);
    }




}
