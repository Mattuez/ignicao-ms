package com.algaworks.algadelivery.deliverytracking.domain.service;

import com.algaworks.algadelivery.deliverytracking.domain.model.ContactPoint;

public interface DeliveryTimeEstimationService {
    DeliveryEstimate estimate(ContactPoint sender, ContactPoint receiver);
}