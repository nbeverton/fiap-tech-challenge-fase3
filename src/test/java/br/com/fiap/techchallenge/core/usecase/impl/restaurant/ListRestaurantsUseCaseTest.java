package br.com.fiap.techchallenge.core.usecase.impl.restaurant;

import br.com.fiap.techchallenge.core.domain.enums.CuisineType;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.valueobjects.OpeningHours;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListRestaurantsUseCaseTest {

    @Mock
    private RestaurantRepositoryPort restaurantRepository;

    @InjectMocks
    private ListRestaurantsUseCaseImpl listRestaurantsUseCase;

    private Restaurant restaurant1;
    private Restaurant restaurant2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        OpeningHours openingHours = new OpeningHours("10:00", "22:00");

        restaurant1 = new Restaurant("1",
                "Restaurant One",
                "address1",
                CuisineType.BRAZILIAN,
                openingHours,
                "user1",
                Collections.emptyList());

        restaurant2 = new Restaurant("2",
                "Restaurant Two",
                "address2",
                CuisineType.JAPANESE,
                openingHours,
                "user2",
                Collections.emptyList());
    }

    @Test
    void shouldReturnListOfRestaurantsWhenTheyExist() {
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant1, restaurant2));

        List<Restaurant> restaurants = listRestaurantsUseCase.execute();

        assertNotNull(restaurants);
        assertEquals(2, restaurants.size());
        assertEquals(restaurant1.getName(), restaurants.get(0).getName());
        assertEquals(restaurant2.getName(), restaurants.get(1).getName());
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoRestaurantsExist() {
        when(restaurantRepository.findAll()).thenReturn(Collections.emptyList());

        List<Restaurant> restaurants = listRestaurantsUseCase.execute();

        assertNotNull(restaurants);
        assertTrue(restaurants.isEmpty());
        verify(restaurantRepository, times(1)).findAll();
    }
}
