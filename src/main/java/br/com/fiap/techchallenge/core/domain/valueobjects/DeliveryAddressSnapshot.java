package br.com.fiap.techchallenge.core.domain.valueobjects;

public class DeliveryAddressSnapshot {

    private String streetName;
    private int streetNumber;
    private String neighborhood;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String additionalInfo;


    public DeliveryAddressSnapshot(
            String streetName,
            int streetNumber,
            String neighborhood,
            String city,
            String stateProvince,
            String postalCode,
            String additionalInfo) {

        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.neighborhood = neighborhood;
        this.city = city;
        this.stateProvince = stateProvince;
        this.postalCode = postalCode;
        this.additionalInfo = additionalInfo;
    }


    public String getStreetName() {
        return streetName;
    }

    public int getStreetNumber() {
        return streetNumber;
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

    public String getPostalCode() {
        return postalCode;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
}
