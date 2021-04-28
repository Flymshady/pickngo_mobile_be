package cz.uhk.fim.bs.pickngo_mobile_be.SpecialOffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialOfferService {

    private final SpecialOfferRepository specialOfferRepository;

    @Autowired
    public SpecialOfferService(SpecialOfferRepository specialOfferRepository) {
        this.specialOfferRepository = specialOfferRepository;
    }

    public List<SpecialOffer> getSpecialOffers() {
        return specialOfferRepository.findAll();
    }

    public Optional<SpecialOffer> getSpecialOffer(Long specialOfferId) {
        return specialOfferRepository.findById(specialOfferId);
    }

    public Optional<List<SpecialOffer>> getSpecialOffersByActive(boolean active) {
        return specialOfferRepository.findAllByActive(active);
    }
}
