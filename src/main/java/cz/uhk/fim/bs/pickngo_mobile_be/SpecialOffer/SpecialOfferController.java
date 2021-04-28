package cz.uhk.fim.bs.pickngo_mobile_be.SpecialOffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path="/specialOffer")
public class SpecialOfferController {

    private final SpecialOfferService specialOfferService;

    @Autowired
    public SpecialOfferController(SpecialOfferService specialOfferService){
        this.specialOfferService=specialOfferService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<SpecialOffer> getSpecialOffers() {
        return specialOfferService.getSpecialOffers();
    }

    @RequestMapping(value = "/all/{active}", method = RequestMethod.GET)
    public Optional<List<SpecialOffer>> getSpecialOffersByActive(@PathVariable boolean active) {
        return specialOfferService.getSpecialOffersByActive(active);
    }


    @RequestMapping(value = "/detail/{specialOfferId}", method = RequestMethod.GET)
    public Optional<SpecialOffer> getSpecialOffer(
            @PathVariable("specialOfferId") Long specialOfferId) {
        return  specialOfferService.getSpecialOffer(specialOfferId);
    }
}
