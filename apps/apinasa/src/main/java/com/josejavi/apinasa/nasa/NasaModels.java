package com.josejavi.apinasa.nasa;

import java.util.List;
import java.util.Map;

/** DTOs reducidos a los campos que utiliza la interfaz didáctica. */
public final class NasaModels {
    private NasaModels() {}

    public record NeoFeed(Map<String, List<NeoObject>> near_earth_objects) {}

    public record NeoObject(
        String id,
        String name,
        boolean is_potentially_hazardous_asteroid,
        List<CloseApproach> close_approach_data
    ) {}

    public record CloseApproach(
        String close_approach_date,
        Map<String, String> miss_distance
    ) {}

    public record DonkiFlare(
        String flrID,
        String beginTime,
        String peakTime,
        String endTime,
        String classType,
        String sourceLocation,
        Integer activeRegionNum,
        List<DonkiInstrument> instruments,
        String link
    ) {}

    public record DonkiInstrument(String id, String displayName) {}

    public record EpicImage(
        String identifier,
        String caption,
        String image,
        String date,
        String version,
        EpicCoordinates centroid_coordinates
    ) {}

    public record EpicCoordinates(Double lat, Double lon) {}
}