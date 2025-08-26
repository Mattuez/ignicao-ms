package com.algaworks.algadelivery.deliverytracking.infra.event;

public interface IntegrationEventPublisher {

    void publish(Object event, String key, String topic);
}
