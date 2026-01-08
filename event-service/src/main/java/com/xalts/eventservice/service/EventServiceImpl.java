package com.xalts.eventservice.service;

import com.xalts.eventservice.dto.EventDTO;
import com.xalts.eventservice.exception.ResourceNotFoundException;
import com.xalts.eventservice.model.Event;
import com.xalts.eventservice.model.Venue;
import com.xalts.eventservice.repository.EventRepository;
import com.xalts.eventservice.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    public EventServiceImpl(EventRepository eventRepository, VenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    @Override
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public EventDTO getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
        return mapToDTO(event);
    }

    @Override
    public EventDTO createEvent(EventDTO eventDTO) {
        Venue venue = venueRepository.findById(eventDTO.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + eventDTO.getVenueId()));

        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setEventDate(eventDTO.getEventDate());
        event.setVenue(venue);
        event.setAvailableSeats(eventDTO.getAvailableSeats());

        Event savedEvent = eventRepository.save(event);
        return mapToDTO(savedEvent);
    }

    @Override
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
        eventRepository.delete(event);
    }

    @Override
    public List<EventDTO> getEventsByVenue(Long venueId) {
        return eventRepository.findByVenueId(venueId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private EventDTO mapToDTO(Event event) {
        return new EventDTO(event.getId(), event.getName(), event.getDescription(),
                event.getEventDate(), event.getVenue().getId(), event.getAvailableSeats());
    }
}
