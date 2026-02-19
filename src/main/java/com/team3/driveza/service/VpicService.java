package com.team3.driveza.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class VpicService {
    private final RestClient restClient = RestClient.create("https://vpic.nhtsa.dot.gov");


    public List<String> getAllMakes() {
        VpicResponseMake resp = restClient.get()
                .uri("/api/vehicles/getallmakes?format=json")
                .retrieve()
                .body(VpicResponseMake.class);

        if (resp == null || resp.getResults() == null) return List.of();

        return resp.getResults().stream()
                .map(VpicMake::getMakeName)
                .distinct()
                .sorted(String::compareToIgnoreCase)
                .toList();

    }

    public List<String> getModelsForMake(String make) {
        if (make == null || make.isBlank()) return List.of();

        VpicResponseModel resp = restClient.get()
                .uri("/api/vehicles/getmodelsformake/{make}?format=json", make.trim())
                .retrieve()
                .body(VpicResponseModel.class);

        if (resp == null || resp.getResults() == null) return List.of();

        return resp.getResults().stream()
                .map(VpicModel::getModelName)
                .distinct()
                .sorted(String::compareToIgnoreCase)
                .toList();
    }

    @Data
    public static class VpicResponseMake {
        private Integer Count;
        private List<VpicMake> Results;
    }

    @Data
    public static class VpicMake {
        private Integer Make_ID;
        private String Make_Name;

        public String getMakeName() { return Make_Name; }
    }

    @Data
    public static class VpicResponseModel {
        private Integer Count;
        private List<VpicModel> Results;
    }

    @Data
    public static class VpicModel {
        private Integer Make_ID;
        private String Make_Name;
        private Integer Model_ID;
        private String Model_Name;

        public String getModelName() { return Model_Name; }
    }
}

