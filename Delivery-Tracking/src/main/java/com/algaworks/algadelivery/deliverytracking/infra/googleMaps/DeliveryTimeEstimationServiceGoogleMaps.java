package com.algaworks.algadelivery.deliverytracking.infra.googleMaps;

import com.algaworks.algadelivery.deliverytracking.domain.model.ContactPoint;
import com.algaworks.algadelivery.deliverytracking.domain.service.DeliveryEstimate;
import com.algaworks.algadelivery.deliverytracking.domain.service.DeliveryTimeEstimationService;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeliveryTimeEstimationServiceGoogleMaps implements DeliveryTimeEstimationService {

    private final GeoApiContext geoApiContext;

    @Override
    public DeliveryEstimate estimate(ContactPoint sender, ContactPoint receiver) {
        try {
            String originStr = formatAddress(sender);
            String destinationString = formatAddress(receiver);

            log.info("Fazendo chamada para matrix api");
            DistanceMatrix matrix = DistanceMatrixApi.newRequest(geoApiContext)
                    .origins(originStr)
                    .destinations(destinationString)
                    .mode(TravelMode.DRIVING)
                    .units(Unit.METRIC)
                    .language("pt-BR")
                    .await();
            log.info("Matrix api retornou {}", matrix.rows[0]);

            DistanceMatrixElement element = matrix.rows[0].elements[0];

            if("OK".equalsIgnoreCase(element.status.toString())) {
                long meters = element.distance.inMeters;

                log.info("Distancia entre {} e {} eh de {} metros", originStr, destinationString, meters);

                return DeliveryEstimate.ofMeters(meters);
            }

            log.error("Não foi possível calcular a distância: {}", element.status);

            throw new RuntimeException("Não foi possível calcular a distância: %s"
                    .formatted(element.status.toString()));
        } catch (Exception e) {
            log.error("Erro ao chamar a Distance Matrix API: {}", e.getMessage());
            throw new RuntimeException("Falha ao se comunicar com o serviço de geolocalização.", e);
        }
    }

    private String formatAddress(ContactPoint address) {
        return String.format("%s, %s - %s, %s, %s",
                address.getStreet(),
                address.getNumber(),
                address.getCity(),
                address.getState(),
                address.getZipCode());
    }
}
