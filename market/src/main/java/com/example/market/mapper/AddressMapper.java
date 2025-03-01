package com.example.market.mapper;

import com.example.market.dto.AddressDto;
import com.example.market.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    Address toEntity(AddressDto addressDto);

    AddressDto toDTO(Address address);
}
