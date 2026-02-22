package com.moveinsync.metro.dto;

import jakarta.validation.constraints.NotNull;

public class BookingRequestDTO {

    @NotNull(message = "Source stop is required")
    private Long sourceId;

    @NotNull(message = "Destination stop is required")
    private Long destinationId;

    public BookingRequestDTO() {}

    public BookingRequestDTO(Long sourceId, Long destinationId) {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }
}