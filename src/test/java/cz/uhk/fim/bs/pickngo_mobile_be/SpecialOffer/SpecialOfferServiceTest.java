package cz.uhk.fim.bs.pickngo_mobile_be.SpecialOffer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SpecialOfferServiceTest {

    @Mock
    private SpecialOfferRepository specialOfferRepository;
    private SpecialOfferService underTest;
    @BeforeEach
    void setUp() {
        underTest = new SpecialOfferService(specialOfferRepository);
    }

    @Test
    void getSpecialOffers() {
        //when
        underTest.getSpecialOffers();
        //then
        verify(specialOfferRepository).findAll();
    }


    @Test
    void getSpecialOffer() {
        Long id = 84L;
        SpecialOffer specialOffer = new SpecialOffer(id, "name", 2.0, true);
        given(specialOfferRepository.findById(id)).willReturn(Optional.of(specialOffer));
        underTest.getSpecialOffer(id);
        verify(specialOfferRepository).findById(id);
    }

    @Test
    void getSpecialOffersByActive() {
        //when
        underTest.getSpecialOffersByActive(true);
        //then
        verify(specialOfferRepository).findAllByActive(true);
    }
}