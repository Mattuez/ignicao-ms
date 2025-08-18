package com.algaworks.algadelivery.delivery.tracking.infra.event;

public interface IntegrationEventPublisher {

    void publish(Object event, String key, String topic);
}
