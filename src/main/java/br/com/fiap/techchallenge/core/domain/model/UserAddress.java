package br.com.fiap.techchallenge.core.domain.model;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;

public class UserAddress {

    private final String id;
    private final String userId;
    private final String addressId;
    private AddressType type;
    private String label;
    private boolean principal;


    public UserAddress(String userId, String addressId, AddressType type, String label, boolean principal) {
        this.id = null;
        this.userId = userId;
        this.addressId = addressId;
        this.type = type;
        this.label = label;
        this.principal = principal;
    }

    public UserAddress(String id, String userId, String addressId, AddressType type, String label, boolean principal) {
        this.id = id;
        this.userId = userId;
        this.addressId = addressId;
        this.type = type;
        this.label = label;
        this.principal = principal;
    }

    public String getId() { return id; }

    public String getUserId() { return userId; }

    public String getAddressId() { return addressId; }

    public AddressType getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void updateType(AddressType type) {
        this.type = type;
    }

    public void updateLabel(String label) {
        this.label = label;
    }

    public void updatePrincipal(boolean principal) {
        this.principal = principal;
    }
}
