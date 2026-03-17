package com.moveinsync.metro.dto;


import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;
@Data

public class BookingResponseDTO {

    private Long bookingId;
    private String username;
    private Long sourceId;
    private String sourceName;
    private Long destinationId;
    private String destinationName;
    private List<Long> pathStopIds;

    private List<RouteSegmentDTO> routeSegments;

    private int totalStops;
    private int totalTransfers;

    private String qrString;
    private boolean travelled;
    private LocalDateTime createdAt;

    private ProfileInfo profile;
    private StopInfo source;
    private StopInfo destination;
    private JourneyInfo journey;
    private TicketInfo ticket;

    public BookingResponseDTO() {}

    public BookingResponseDTO(Long bookingId,
                              String username,
                              Long sourceId,
                              String sourceName,
                              Long destinationId,
                              String destinationName,
                              List<Long> pathStopIds,
                              List<RouteSegmentDTO> routeSegments,
                              int totalStops,
                              int totalTransfers,
                              String qrString,
                              boolean travelled,
                              LocalDateTime createdAt) {
        this.bookingId = bookingId;
        this.username = username;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.destinationId = destinationId;
        this.destinationName = destinationName;
        this.pathStopIds = pathStopIds;
        this.routeSegments = routeSegments;
        this.totalStops = totalStops;
        this.totalTransfers = totalTransfers;
        this.qrString = qrString;
        this.travelled = travelled;
        this.createdAt = createdAt;
    }

    @Data
    public static class ProfileInfo {
        private String username;
        public ProfileInfo(String username) {
            this.username = username;
        }
    }

    @Data
    public static class StopInfo {
        private Long id;
        private String name;
        public StopInfo(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Data
    public static class JourneyInfo {
        private List<Long> pathStopIds;
        private List<RouteSegmentDTO> routeSegments;
        private int totalStops;
        private int totalTransfers;

        public JourneyInfo(List<Long> pathStopIds, List<RouteSegmentDTO> routeSegments, int totalStops, int totalTransfers) {
            this.pathStopIds = pathStopIds;
            this.routeSegments = routeSegments;
            this.totalStops = totalStops;
            this.totalTransfers = totalTransfers;
        }
    }

    @Data
    public static class TicketInfo {
        private String qrString;
        private boolean travelled;
        private LocalDateTime createdAt;

        public TicketInfo(String qrString, boolean travelled, LocalDateTime createdAt) {
            this.qrString = qrString;
            this.travelled = travelled;
            this.createdAt = createdAt;
        }
    }

}
