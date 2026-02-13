package com.foodexpress.restaurant.formatter;

import com.foodexpress.restaurant.entity.Dish;
import com.foodexpress.restaurant.entity.Restaurant;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("simple")
public class SimpleMenuFormatter implements MenuFormatter {

    @Override
    public String format(Restaurant restaurant) {
        StringBuilder sb = new StringBuilder();
        sb.append("Menu - ").append(restaurant.name).append("\n");
        for (Dish d : restaurant.dishes) {
            sb.append("- ").append(d.name)
              .append(" (").append(d.price).append("€)")
              .append(d.available ? "" : " [INDISPONIBLE]")
              .append("\n");
        }
        return sb.toString();
    }
}
