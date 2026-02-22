package com.moveinsync.metro.dto;

import jakarta.validation.constraints.NotBlank;

public class AdminRouteRequestDTO {

    @NotBlank(message = "Route color is required")
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
