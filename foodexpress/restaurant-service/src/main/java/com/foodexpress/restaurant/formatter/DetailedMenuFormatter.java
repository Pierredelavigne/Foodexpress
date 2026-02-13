package com.foodexpress.restaurant.formatter;

import com.foodexpress.restaurant.entity.Dish;
import com.foodexpress.restaurant.entity.Restaurant;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("detailed")
public class DetailedMenuFormatter implements MenuFormatter {

    @Override
    public String format(Restaurant restaurant) {
        StringBuilder sb = new StringBuilder();
        sb.append("Menu détaillé - ").append(restaurant.name).append("\n");
        for (Dish d : restaurant.dishes) {
            sb.append("- ").append(d.name)
              .append(" [").append(d.category).append("] ")
              .append(d.price).append("€\n");
            if (d.description != null && !d.description.isBlank()) {
                sb.append("  desc: ").append(d.description).append("\n");
            }
            if (d.allergens != null && !d.allergens.isBlank()) {
                sb.append("  allergènes: ").append(d.allergens).append("\n");
            }
            if (!d.available) sb.append("  ⚠ indisponible\n");
        }
        return sb.toString();
    }
}
