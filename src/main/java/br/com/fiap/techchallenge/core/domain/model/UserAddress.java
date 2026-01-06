package br.com.fiap.techchallenge.core.domain.model;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.InvalidUserAddressException;


public class UserAddress {

    private final String id;
    private final String userId;
    private final String addressId;
    private AddressType type;
    private String label;
    private boolean principal;


    public UserAddress(String userId,
                       String addressId,
                       AddressType type,
                       String label,
                       boolean principal) {
        this(null, userId, addressId, type, label, principal);
    }

    public UserAddress(String id,
                       String userId,
                       String addressId,
                       AddressType type,
                       String label,
                       boolean principal) {

        this.id = id;
        this.userId = requireNonBlank(userId, "userId");
        this.addressId = requireNonBlank(addressId, "addressId");
        this.type = requireNonNull(type, "type");
        this.label = requireNonBlank(label, "label");
        this.principal = principal;
    }

    private String requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidUserAddressException(fieldName + " must not be null or blank");
        }
        return value;
    }

    private AddressType requireNonNull(AddressType value, String fieldName) {
        if (value == null) {
            throw new InvalidUserAddressException(fieldName + " must not be null");
        }
        return value;
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
        this.type = requireNonNull(type, "type must not be null");
    }

    public void updateLabel(String label) {
        this.label = label;
    }

    public void updatePrincipal(boolean principal) {
        this.principal = principal;
    }
}
