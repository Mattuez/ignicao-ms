package com.algaworks.algadelivery.deliverytracking.infra.event;

import com.algaworks.algadelivery.deliverytracking.domain.event.DeliveryFulfilledEvent;
import com.algaworks.algadelivery.deliverytracking.domain.event.DeliveryPickedUpEvent;
import com.algaworks.algadelivery.deliverytracking.domain.event.DeliveryPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeliveryDomainEventHandler {

    private final IntegrationEventPublisher integrationEventPublisher;

    @EventListener
    public void handle(DeliveryPlacedEvent event) {
        log.info(event.toString());

        integrationEventPublisher.publish(event, event.getDeliveryId().toString(), "deliveries.v1.events");
    }

    @EventListener
    public void handle(DeliveryPickedUpEvent event) {
        log.info(event.toString());

        integrationEventPublisher.publish(event, event.getDeliveryId().toString(), "deliveries.v1.events");
    }

    @EventListener
    public void handle(DeliveryFulfilledEvent event) {
        log.info(event.toString());

        integrationEventPublisher.publish(event, event.getDeliveryId().toString(), "deliveries.v1.events");
    }
}
