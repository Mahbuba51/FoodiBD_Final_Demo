package com.foodibd.backend;

import com.foodibd.backend.entity.*;
import com.foodibd.backend.entity.embedded.Address;
import com.foodibd.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    public void run(String... args) {
        if (restaurantRepository.count() > 0) {
            log.info("DataSeeder: Database already seeded. Skipping.");
            return;
        }

        log.info("DataSeeder: Seeding database with demo data...");
        seedBurgerBoss();
        seedSpicyNoodle();
        seedGreenBowl();
        log.info("DataSeeder: Seeding complete.");
    }

    private void seedBurgerBoss() {
        Address addr = Address.builder()
                .street("12 Gulshan Avenue")
                .city("Dhaka")
                .state("Dhaka Division")
                .postalCode("1212")
                .country("Bangladesh")
                .build();

        Restaurant restaurant = Restaurant.builder()
                .name("Burger Boss")
                .address(addr)
                .cuisineTypes(List.of("Burgers", "Fast Food", "American"))
                .rating(4.5)
                .available(true)
                .build();

        restaurant = restaurantRepository.save(restaurant);

        Menu menu = Menu.builder()
                .restaurant(restaurant)
                .build();
        menu = menuRepository.save(menu);

        List<MenuItem> items = List.of(
                MenuItem.builder()
                        .name("Classic Beef Burger")
                        .description("Juicy beef patty with lettuce, tomato, and our secret sauce")
                        .price(320.0)
                        .availability(true)
                        .categories(List.of("Burgers", "Bestseller"))
                        .menu(menu)
                        .build(),
                MenuItem.builder()
                        .name("Crispy Chicken Burger")
                        .description("Golden fried chicken fillet with coleslaw and mayo")
                        .price(280.0)
                        .availability(true)
                        .categories(List.of("Burgers", "Chicken"))
                        .menu(menu)
                        .build(),
                MenuItem.builder()
                        .name("Double Smash Burger")
                        .description("Two smashed beef patties with double cheese and caramelized onions")
                        .price(420.0)
                        .availability(true)
                        .categories(List.of("Burgers", "Premium"))
                        .menu(menu)
                        .build(),
                MenuItem.builder()
                        .name("Loaded Fries")
                        .description("Crispy fries topped with cheese sauce and jalapeños")
                        .price(150.0)
                        .availability(true)
                        .categories(List.of("Sides"))
                        .menu(menu)
                        .build(),
                MenuItem.builder()
                        .name("Chocolate Milkshake")
                        .description("Thick and creamy chocolate milkshake")
                        .price(180.0)
                        .availability(true)
                        .categories(List.of("Drinks"))
                        .menu(menu)
                        .build()
        );

        menuItemRepository.saveAll(items);
        log.info("DataSeeder: Seeded Burger Boss with {} items.", items.size());
    }

    private void seedSpicyNoodle() {
        Address addr = Address.builder()
                .street("88 Banani Road 11")
                .city("Dhaka")
                .state("Dhaka Division")
                .postalCode("1213")
                .country("Bangladesh")
                .build();

        Restaurant restaurant = Restaurant.builder()
                .name("Spicy Noodle House")
                .address(addr)
                .cuisineTypes(List.of("Chinese", "Noodles", "Asian"))
                .rating(4.3)
                .available(true)
                .build();

        restaurant = restaurantRepository.save(restaurant);

        Menu menu = Menu.builder()
                .restaurant(restaurant)
                .build();
        menu = menuRepository.save(menu);

        List<MenuItem> items = List.of(
                MenuItem.builder()
                        .name("Beef Chow Mein")
                        .description("Stir-fried noodles with tender beef strips and vegetables")
                        .price(260.0)
                        .availability(true)
                        .categories(List.of("Noodles", "Bestseller"))
                        .menu(menu)
                        .build(),
                MenuItem.builder()
                        .name("Szechuan Chicken Noodles")
                        .description("Spicy Szechuan-style chicken noodles with chili oil")
                        .price(240.0)
                        .availability(true)
                        .categories(List.of("Noodles", "Spicy"))
                        .menu(menu)
                        .build(),
                MenuItem.builder()
                        .name("Vegetable Fried Rice")
                        .description("Wok-tossed rice with mixed vegetables and soy sauce")
                        .price(180.0)
                        .availability(true)
                        .categories(List.of("Rice"))
                        .menu(menu)
                        .build(),
                MenuItem.builder()
                        .name("Chicken Dumpling (6 pcs)")
                        .description("Steamed dumplings filled with seasoned chicken and ginger")
                        .price(200.0)
                        .availability(true)
                        .categories(List.of("Starters"))
                        .menu(menu)
                        .build(),
                MenuItem.builder()
                        .name("Hot & Sour Soup")
                        .description("Classic hot and sour soup with tofu and mushrooms")
                        .price(130.0)
                        .availability(true)
                        .categories(List.of("Soups"))
                        .menu(menu)
                        .build(),
                MenuItem.builder()
                        .name("Kung Pao Beef")
                        .description("Wok-fried beef with peanuts, chilies, and Szechuan pepper")
                        .price(300.0)
                        .availability(true)
                        .categories(List.of("Main Course", "Spicy"))
                        .menu(menu)
                        .build()
        );

        menuItemRepository.saveAll(items);
        log.info("DataSeeder: Seeded Spicy Noodle House with {} items.", items.size());
    }

    private void seedGreenBowl() {
        Address addr = Address.builder()
                .street("5 Dhanmondi Lake Road")
                .city("Dhaka")
                .state("Dhaka Division")
                .postalCode("1205")
                .country("Bangladesh")
                .build();

        Restaurant restaurant = Restaurant.builder()
                .name("Green Bowl")
                .address(addr)
                .cuisineTypes(List.of("Healthy", "Salads", "Wraps", "Vegan"))
                .rating(4.6)
                .available(true)
                .build();

        restaurant = restaurantRepository.save(restaurant);

        Menu menu = Menu.builder()
                .restaurant(restaurant)
                .build();
        menu = menuRepository.save(menu);

        List<MenuItem> items = List.of(
                MenuItem.builder()
                        .name("Grilled Chicken Bowl")
                        .description("Brown rice, grilled chicken, avocado, and tahini dressing")
                        .price(350.0)
                        .availability(true)
                        .categories(List.of("Bowls", "Bestseller"))
                        .menu(menu)
                        .build(),
                MenuItem.builder()
                        .name("Falafel Wrap")
                        .description("Crispy falafel, hummus, cucumber, and fresh herbs in a whole wheat wrap")
                        .price(220.0)
                        .availability(true)
                        .categories(List.of("Wraps", "Vegan"))
                        .menu(menu)
                        .build(),
                MenuItem.builder()
                        .name("Greek Salad")
                        .description("Tomato, cucumber, olives, feta cheese with olive oil and oregano")
                        .price(190.0)
                        .availability(true)
                        .categories(List.of("Salads"))
                        .menu(menu)
                        .build(),
                MenuItem.builder()
                        .name("Quinoa Power Bowl")
                        .description("Quinoa, roasted chickpeas, spinach, beets, and lemon dressing")
                        .price(320.0)
                        .availability(true)
                        .categories(List.of("Bowls", "Vegan"))
                        .menu(menu)
                        .build(),
                MenuItem.builder()
                        .name("Mango Smoothie")
                        .description("Fresh Bangladeshi mango blended with yogurt and honey")
                        .price(160.0)
                        .availability(true)
                        .categories(List.of("Drinks"))
                        .menu(menu)
                        .build()
        );

        menuItemRepository.saveAll(items);
        log.info("DataSeeder: Seeded Green Bowl with {} items.", items.size());
    }
}