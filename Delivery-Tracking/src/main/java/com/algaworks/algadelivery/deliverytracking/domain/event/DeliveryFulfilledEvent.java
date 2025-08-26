package com.algaworks.algadelivery.deliverytracking.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@ToString
@Getter
public class DeliveryFulfilledEvent {

    private final OffsetDateTime occurredAt;
    private final UUID deliveryId;
}
