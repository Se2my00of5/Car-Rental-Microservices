package org.example.bookingservice.mapper;

import org.example.bookingservice.model.Booking;
import org.example.commonservice.dto.BookingDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingDTO.Response.Info toResponseInfoDTO(Booking booking);
}
