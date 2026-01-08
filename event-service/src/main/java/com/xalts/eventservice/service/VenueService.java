package com.xalts.eventservice.service;

import com.xalts.eventservice.dto.VenueDTO;

import java.util.List;

public interface VenueService {
    List<VenueDTO> getAllVenues();
    VenueDTO getVenueById(Long id);
    VenueDTO createVenue(VenueDTO venueDTO);
    void deleteVenue(Long id);
}
