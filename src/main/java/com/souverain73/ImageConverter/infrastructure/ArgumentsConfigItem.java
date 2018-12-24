package com.souverain73.ImageConverter.infrastructure;

import java.util.Collection;

public class ArgumentsConfigItem {
    private String internalName;
    private Collection<String> keys;
    private ArgumentParser valueParser;
    private String description;

    public ArgumentsConfigItem(String name, Collection<String> keys, ArgumentParser valueParser, String description) {
        this.internalName = name;
        this.keys = keys;
        this.valueParser = valueParser;
        this.description = description;
    }

    public static ArgumentsConfigItem item(String name, Collection<String> keys, ArgumentParser valueParser, String description){
        return new ArgumentsConfigItem(name, keys, valueParser, description);
    }

    public String getInternalName() {
        return internalName;
    }

    public Collection<String> getKeys() {
        return keys;
    }

    public String getDescription() {
        return description;
    }

    public boolean isValidKey(String key){
        return keys.stream().anyMatch(key::equals);
    }

    public Object parseValue(String value){
        try{
            return valueParser.parse(value);
        }catch(Exception e){
            throw  new IllegalArgumentException(String.format("Illegal argument value [%s]", value));
        }
    }
}
