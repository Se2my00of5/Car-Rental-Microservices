package org.example.bookingservice.mapper;

import org.example.bookingservice.model.Booking;
import org.example.commonservice.dto.BookingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingDTO.Response.Info toResponseInfoDTO(Booking booking);
}
