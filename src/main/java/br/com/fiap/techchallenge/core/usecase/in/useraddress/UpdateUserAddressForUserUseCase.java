package br.com.fiap.techchallenge.core.usecase.in.useraddress;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressSummaryOutput;

import java.util.List;

public interface UpdateUserAddressForUserUseCase {

    List<UserAddressSummaryOutput> execute(
            String userId,
            String userAddressId,
            AddressType addressType,
            String label,
            boolean principal
    );
}
