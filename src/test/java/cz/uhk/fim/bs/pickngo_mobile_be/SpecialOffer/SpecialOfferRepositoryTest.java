package cz.uhk.fim.bs.pickngo_mobile_be.SpecialOffer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SpecialOfferRepositoryTest {

    @Autowired
    private SpecialOfferRepository underTest;

    @AfterEach
    void tearDown(){
        underTest.deleteAll();
    }

    @Test
    void findAllByActive() {

        SpecialOffer specialOffer = new SpecialOffer();
        specialOffer.setActive(true);
        underTest.save(specialOffer);
        Optional<List<SpecialOffer>> list = underTest.findAllByActive(true);
        assertThat(list.get()).contains(specialOffer);
    }
    @Test
    void WontFindAllByActive() {

        SpecialOffer specialOffer = new SpecialOffer();
        specialOffer.setActive(false);
        underTest.save(specialOffer);
        Optional<List<SpecialOffer>> list = underTest.findAllByActive(true);
        assertThat(list.get()).isEmpty();
    }
}