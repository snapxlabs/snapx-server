package com.digcoin.snapx.server.app.restaurant.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/2 11:59
 * @description
 */
@Mapper(componentModel = "spring")
public interface CustomConverter {

    @Named("urlsSeparator")
    default List<String> urlsSeparator(String input) {
        List<String> output = new ArrayList<>();
        if (input != null) {
            String[] parts = input.split(",");
            for (String part : parts) {
                output.add(part.trim());
            }
        }
        return output;
    }

}
