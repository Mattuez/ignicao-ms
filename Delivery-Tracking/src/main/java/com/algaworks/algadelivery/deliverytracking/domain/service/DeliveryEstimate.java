package com.algaworks.algadelivery.deliverytracking.domain.service;

import lombok.Getter;
import java.time.Duration;

@Getter
public class DeliveryEstimate {
    private Duration estimatedTime;
    private Double distanceInKm;

    private DeliveryEstimate(Duration estimatedTime, Double distanceInKm) {
        this.estimatedTime = estimatedTime;
        this.distanceInKm = distanceInKm;
    }

    public static DeliveryEstimate ofMeters(long meters) {
        final double AVERAGE_SPEED_KMH = 35.0;

        double distanceInKm = meters / 1000.0;

        double timeInHours = distanceInKm / AVERAGE_SPEED_KMH;

        long timeInMinutes = (long) (timeInHours * 60);

        Duration estimatedTime = Duration.ofMinutes(timeInMinutes);

        return new DeliveryEstimate(estimatedTime, distanceInKm);
    }
}