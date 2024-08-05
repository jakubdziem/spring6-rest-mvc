package com.dziem.spring6restmvc.mappers;

import com.dziem.spring6restmvc.entities.Beer;
import com.dziem.spring6restmvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    Beer beerDtoToBeer(BeerDTO dto);
    BeerDTO beerToBeerDto(Beer beer);
}
