package br.com.fiap.techchallenge.infra.persistence.documents.embedded;

public class DeliveryAddressSnapshotEmbedded {

    private String streetName;
    private int streetNumber;
    private String neighborhood;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String additionalInfo;


    public DeliveryAddressSnapshotEmbedded() {
    }

    public DeliveryAddressSnapshotEmbedded(
            String streetName,
            int streetNumber,
            String neighborhood,
            String city,
            String stateProvince,
            String postalCode,
            String additionalInfo
    ) {
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

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
