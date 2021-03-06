package cz.uhk.fim.bs.pickngo_mobile_be.Item;

import cz.uhk.fim.bs.pickngo_mobile_be.Customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path="/item")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @RequestMapping(value = "/{baguetteItemId}/all", method = RequestMethod.GET)
    public Optional<List<Item>> getItems(@PathVariable("baguetteItemId") Long baguetteItemId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;

        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz
        return itemService.getItems(baguetteItemId, email);
    }

    @RequestMapping(value = "/create/{baguetteItemId}", method = RequestMethod.POST)
    public Item createNewItem(@PathVariable("baguetteItemId") Long baguetteItemId, @RequestBody Item item){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;

        String email = authentication.getPrincipal().getAttribute("email"); //vraci jmeno.prijmeni@uhk.cz

        return  itemService.addNewItem(item, email, baguetteItemId);
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
