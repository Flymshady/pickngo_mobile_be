package cz.uhk.fim.bs.pickngo_mobile_be.SpecialOffer;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialOfferRepository extends JpaRepository<SpecialOffer, Long> {

    Optional<List<SpecialOffer>> findAllByActive(boolean active);
}