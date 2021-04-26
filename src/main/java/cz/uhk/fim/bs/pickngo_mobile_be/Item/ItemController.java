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

    @RequestMapping(value = "/{baguetteItemId}/all", method = RequestMethod.GET)
    public List<Item> getItems(@PathVariable("baguetteItemId") Long baguetteItemId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;

        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz
        return itemService.getItems(baguetteItemId, email);
    }

    @RequestMapping(value = "/create/{baguetteItemId}", method = RequestMethod.POST)
    public void createNewItem(@PathVariable("baguetteItemId") Long baguetteItemId, @RequestBody Item item){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;

        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz

        itemService.addNewItem(item, email, baguetteItemId);
    }

    @RequestMapping(value = "/update/{itemId}", method = RequestMethod.PUT)
    public void updateItem(@PathVariable("itemId") Long itemId,
                           @RequestParam(required = false) int amount){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;

        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz

        itemService.updateItem(itemId, email, amount);
    }

    @RequestMapping(value = "/remove/{itemId}", method = RequestMethod.DELETE)
    public void removeItem(@PathVariable("itemId") Long itemId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;

        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz

        itemService.removeItem(itemId, email);
    }
}
