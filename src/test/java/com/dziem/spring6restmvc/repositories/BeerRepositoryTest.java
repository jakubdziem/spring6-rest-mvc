package com.dziem.spring6restmvc.repositories;

import com.dziem.spring6restmvc.entities.Beer;
import com.dziem.spring6restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;
    @Test
    void testSaveBeerTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            Beer savedBeer = beerRepository.save(Beer.builder()
                    .beerName("My beerMy beerMy beerMy beerMy beerMy beerMy beerMy beerMy beerMy beerMy beerMy beerMy beer")
                    .upc("12123")
                    .price(new BigDecimal("20"))
                    .beerStyle(BeerStyle.PALE_ALE)
                    .build());
            assertThat(savedBeer).isNotNull();

            beerRepository.flush();
        });
    }
    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("My beer")
                .upc("12123")
                .price(new BigDecimal("20"))
                .beerStyle(BeerStyle.PALE_ALE)
                .build());
        assertThat(savedBeer).isNotNull();

        beerRepository.flush();
        //this test runs to quickly so there is a need
        // for using flush so the jakarta can return exception

        assertThat(savedBeer.getId()).isNotNull();
        assertThat(savedBeer.getBeerName()).isEqualTo(beerRepository.findById(savedBeer.getId()).get().getBeerName());
    }
}