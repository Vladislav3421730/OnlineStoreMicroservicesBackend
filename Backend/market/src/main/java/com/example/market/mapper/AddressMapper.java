package com.example.market.mapper;

import com.example.market.dto.AddressDto;
import com.example.market.model.Address;
import org.mapstruct.Mapper;

@Mapper
public interface AddressMapper {

    Address toEntity(AddressDto addressDto);

    AddressDto toDTO(Address address);
}
