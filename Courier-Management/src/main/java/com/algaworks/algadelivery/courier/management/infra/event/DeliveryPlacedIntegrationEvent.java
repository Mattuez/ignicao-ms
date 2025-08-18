package com.algaworks.algadelivery.courier.management.infra.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class DeliveryPlacedIntegrationEvent {

    private OffsetDateTime occurredAt;
    private UUID deliveryId;
}
