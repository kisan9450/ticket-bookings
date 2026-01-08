package com.xalts.eventservice.service;

import com.xalts.eventservice.dto.VenueDTO;
import com.xalts.eventservice.exception.ResourceNotFoundException;
import com.xalts.eventservice.model.Venue;
import com.xalts.eventservice.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    public VenueServiceImpl(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public List<VenueDTO> getAllVenues() {
        return venueRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public VenueDTO getVenueById(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + id));
        return mapToDTO(venue);
    }

    @Override
    public VenueDTO createVenue(VenueDTO venueDTO) {
        Venue venue = new Venue();
        venue.setName(venueDTO.getName());
        venue.setLocation(venueDTO.getLocation());

        Venue savedVenue = venueRepository.save(venue);
        return mapToDTO(savedVenue);
    }

    @Override
    public void deleteVenue(Long id) {
        venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + id));
        venueRepository.deleteById(id);
    }

    private VenueDTO mapToDTO(Venue venue) {
        return new VenueDTO(venue.getId(), venue.getName(), venue.getLocation());
    }
}
