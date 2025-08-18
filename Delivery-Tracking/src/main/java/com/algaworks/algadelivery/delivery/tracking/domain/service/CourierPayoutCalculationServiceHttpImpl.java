package com.algaworks.algadelivery.delivery.tracking.domain.service;

import com.algaworks.algadelivery.delivery.tracking.infra.http.CourierAPIClient;
import com.algaworks.algadelivery.delivery.tracking.infra.http.CourierPayoutCalculationInput;
import com.algaworks.algadelivery.delivery.tracking.infra.http.CourierPayoutResultModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CourierPayoutCalculationServiceHttpImpl implements CourierPayoutCalculationService {

    private final CourierAPIClient courierAPIClient;

    @Override
    public BigDecimal calculatePayout(Double distanceInKm) {
        CourierPayoutResultModel model = courierAPIClient.payoutCalculation(
                new CourierPayoutCalculationInput(distanceInKm)
        );

        return model.payoutFee();
    }
}
