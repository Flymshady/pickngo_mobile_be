package cz.uhk.fim.bs.pickngo_mobile_be.Item;

import cz.uhk.fim.bs.pickngo_mobile_be.Customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/item")
public class ItemController {

    private final CustomerService customerService;
    private final ItemService itemService;

    @Autowired
    public ItemController(CustomerService customerService, ItemService itemService) {
        this.customerService = customerService;
        this.itemService = itemService;
    }

    @RequestMapping(value = "/{baguetteOrderId}/all", method = RequestMethod.GET)
    public List<Item> getItems(@PathVariable("baguetteOrderId") Long baguetteOrderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;

        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz
        return itemService.getItems(baguetteOrderId, email);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void createNewItem(@RequestBody Item item){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;

        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz

        itemService.addNewItem(item, email);
    }
}
