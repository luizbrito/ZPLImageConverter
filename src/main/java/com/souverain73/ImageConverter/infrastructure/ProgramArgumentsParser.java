package com.souverain73.ImageConverter.infrastructure;

import java.util.HashMap;
import java.util.Map;

public class ProgramArgumentsParser {
    private final ArgumentsConfig config;

    public ProgramArgumentsParser(ArgumentsConfig config) {
        this.config = config;
    }

    public Map<String, Object> parseParams(String[] args){
        HashMap<String, Object> result = new HashMap<>();
        String key = null;
        String value = null;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            String[] split = arg.split("=");
            if (split.length > 1){
                key = split[0];
                value = split[1];
            }else{
                key = arg;
                value = args[++i];
            }

            ArgumentsConfigItem item = getConfigItem(key);

            result.put(item.getInternalName(), item.parseValue(value));
        }

        return result;
    }

    private ArgumentsConfigItem getConfigItem(String key){
        ArgumentsConfigItem item = config.getItem(key);
        if (item == null){
            throw new IllegalArgumentException(String.format("Unknown parameter [%s]", key));
        }
        return item;
    }
}
