package com.algaworks.algadelivery.deliverytracking.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CourierIdInput {
    private UUID courierId;
}