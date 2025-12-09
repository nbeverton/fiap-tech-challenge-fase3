package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.entity;

@Entity
@Table(name = "restaurants")
public class RestaurantEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String cuisineType;
    private String openingHours;
    private Long ownerId;
}