package com.xalts.eventservice.service;

import com.xalts.eventservice.dto.EventDTO;

import java.util.List;

public interface EventService {
    List<EventDTO> getAllEvents();
    EventDTO getEventById(Long eventId);
    EventDTO createEvent(EventDTO eventDTO);
    void deleteEvent(Long eventId);
    List<EventDTO> getEventsByVenue(Long venueId);
}
