package com.souverain73.ImageConverter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ArgumentsConfig {
    Collection<ArgumentsConfigItem> items;

    public ArgumentsConfig(ArgumentsConfigItem... items) {
        this.items = List.of(items);
    }

    public ArgumentsConfigItem getItem(String key){
        return items.stream().filter(i -> i.isValidKey(key))
                .findFirst().orElse(null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        items.forEach(i -> sb.append(String.format("%s %s\n", Arrays.toString(i.getKeys().toArray()), i.getDescription())));

        return sb.toString();
    }
}
