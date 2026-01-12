package br.com.fiap.techchallenge.core.usecase.in.restaurant;

import br.com.fiap.techchallenge.core.domain.enums.CuisineType;
import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.valueobjects.Menu;
import br.com.fiap.techchallenge.core.domain.valueobjects.OpeningHours;
import br.com.fiap.techchallenge.core.usecase.impl.restaurant.CreateRestaurantUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepositoryPort restaurantRepository;

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private AddressRepositoryPort addressRepository;

    @InjectMocks
    private CreateRestaurantUseCaseImpl createRestaurantUseCase;

    @Test
    void mustCreateRestaurant() {
        //Arrange
        var restaurant = generateNewRestaurant();
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(i -> i.getArgument(0));

        //Act
        var result = createRestaurantUseCase.execute(restaurant);

        //Assert
        assertThat(result)
                .isNotNull()
                .isEqualTo(restaurant);
    }

    // Helpers
    private Restaurant generateNewRestaurant() {
        var addressId = generateNewAddressId();
        return new Restaurant(
                UUID.randomUUID().toString(),
                "Burguer King",
                addressId,
                CuisineType.AMERICAN,
                new OpeningHours("08:00", "22:00"),
                addressId,
                generateNewMenu()
        );
    }

    private String generateNewAddressId() {
        Address address = new Address(
                "12345-020",
                "Rua um",
                10,
                "Rua",
                "Bairro principal",
                "Cidade nova",
                "Estado novo",
                "Brasil");
        Address result = addressRepository.save(address);
        return result.getId();
    }

    private List<Menu> generateNewMenu() {
        return List.of(
                Menu.create("Sanduíche 1", "Description 1", BigDecimal.valueOf(10.0), false, "image_url"),
                Menu.create("Sanduíche 2", "Description 2", BigDecimal.valueOf(12.5), false, "image_url"),
                Menu.create("Sanduíche 3", "Description 3", BigDecimal.valueOf(8.0), false, "image_url")
        );
    }
}
