package br.com.fiap.techchallenge.core.usecase.in.useraddress;

import br.com.fiap.techchallenge.core.domain.model.UserAddress;

import java.util.List;

public interface FindUserAddressByUserIdUseCase {

    List<UserAddress> execute(String userId);
}
