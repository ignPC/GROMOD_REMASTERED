package com.gromod.client.settings;

import com.gromod.client.utils.MessageUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadSettings {

    public static final String FILE_PATH = "gromod_settings.json";

    public LoadSettings(){
        readJson();
    }

    private void readJson() {
        try {
            List<Map<String, Object>> settingsList = parseJson();
            System.out.println("Parsed settingsList: " + settingsList);
            applySettings(settingsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, Object>> parseJson() throws IOException {
        List<Map<String, Object>> settingsList = new ArrayList<>();
        StringBuilder jsonContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(LoadSettings.FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
        }

        String jsonString = jsonContent.toString().trim();

        if (jsonString.startsWith("[") && jsonString.endsWith("]")) {
            jsonString = jsonString.substring(1, jsonString.length() - 1);
        }

        String[] settingsArray = jsonString.split("\\},\\{");
        for (String settingsString : settingsArray) {
            Map<String, Object> settingsMap = parseSettingsString(settingsString);
            settingsList.add(settingsMap);
        }

        return settingsList;
    }

    private void applySettings(List<Map<String, Object>> settingsList) {
        try {
            for (Map<String, Object> setting : settingsList) {
                String className = (String) setting.get("className");

                System.out.println(className);
                setting.remove("className"); // Remove the className entry to get the field values only.

                Class<?> clazz = Class.forName(className);
                Object instance = clazz.getMethod("getInstance").invoke(null);

                for (Map.Entry<String, Object> fieldValueEntry : setting.entrySet()) {
                    String fieldName = fieldValueEntry.getKey();
                    Object value = fieldValueEntry.getValue();

                    System.out.println("\t" + fieldName);
                    System.out.println("\t" + value.toString());

                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(instance, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> parseSettingsString(String settingsString) {
        Map<String, Object> settingsMap = new HashMap<>();
        String[] keyValuePairs = settingsString.split(",");
        for (String keyValuePair : keyValuePairs) {
            String[] keyValue = keyValuePair.split(":");
            String key = keyValue[0].replaceAll("[\"{}]", "").trim();
            String value = keyValue[1].replaceAll("[\"{}]", "").trim();

            // Check if the value is a boolean or an integer
            if (value.equals("true") || value.equals("false")) {
                settingsMap.put(key, Boolean.parseBoolean(value));
            } else {
                try {
                    settingsMap.put(key, Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    // If the value is not a boolean or integer, treat it as a string.
                    settingsMap.put(key, value.replaceAll("\"", ""));
                }
            }
        }
        return settingsMap;
    }

    private Object parseValue(String value) {
        if (value.equals("true") || value.equals("false")) {
            return Boolean.parseBoolean(value);
        } else {
            // Check if the value is surrounded by double quotes (indicating a string).
            if (value.startsWith("\"") && value.endsWith("\"")) {
                // Remove the double quotes and return the value as a string.
                return value.substring(1, value.length() - 1);
            } else {
                // If it's not a boolean or string, handle it as an integer.
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
