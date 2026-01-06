package br.com.fiap.techchallenge.core.domain.model;

import br.com.fiap.techchallenge.core.domain.exception.address.InvalidAddressException;

public class Address {

    private final String id;
    private String postalCode;
    private String streetName;
    private int streetNumber;
    private String additionalInfo;
    private String neighborhood;
    private String city;
    private String stateProvince;
    private String country;


    public Address(
            String id,
            String postalCode,
            String streetName,
            int streetNumber,
            String additionalInfo,
            String neighborhood,
            String city,
            String stateProvince,
            String country) {

        this.id = id;
        this.postalCode = requireNonBlank(postalCode, "postalCode");
        this.streetName = requireNonBlank(streetName, "streetName");
        this.streetNumber = requirePositive(streetNumber, "streetNumber");
        this.additionalInfo = additionalInfo; // opcional
        this.neighborhood = requireNonBlank(neighborhood, "neighborhood");
        this.city = requireNonBlank(city, "city");
        this.stateProvince = requireNonBlank(stateProvince, "stateProvince");
        this.country = requireNonBlank(country, "country");
    }


    public Address(String postalCode,
                   String streetName,
                   int streetNumber,
                   String additionalInfo,
                   String neighborhood,
                   String city,
                   String stateProvince,
                   String country) {

        this(null, postalCode, streetName, streetNumber, additionalInfo,
                neighborhood, city, stateProvince, country);
    }

    private String requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidAddressException(fieldName + " must not be null or blank");
        }
        return value;
    }

    private int requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new InvalidAddressException(fieldName + " must be greater than zero");
        }
        return value;
    }

    public String getId() {
        return id;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCity() {
        return city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public String getCountry() {
        return country;
    }


    public void updatePostalCode(String postalCode) {
        this.postalCode = requireNonBlank(postalCode, "postalCode");
    }

    public void updateStreetName(String streetName) {
        this.streetName = requireNonBlank(streetName, "streetName");
    }

    public void updateStreetNumber(int streetNumber) {
        this.streetNumber = requirePositive(streetNumber, "streetNumber");
    }

    public void updateAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public void updateNeighborhood(String neighborhood) {
        this.neighborhood = requireNonBlank(neighborhood, "neighborhood");
    }

    public void updateCity(String city) {
        this.city = requireNonBlank(city, "city");
    }

    public void updateStateProvince(String stateProvince) {
        this.stateProvince = requireNonBlank(stateProvince, "stateProvince");
    }

    public void updateCountry(String country) {
        this.country = requireNonBlank(country, "country");
    }
}
