package com.gromod.client.settings;

import com.gromod.client.annotation.GuiField;
import com.gromod.client.annotation.GuiModule;
import org.reflections.Reflections;
import scala.util.parsing.json.JSONArray;
import scala.util.parsing.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class SaveSettings {
    public SaveSettings() {
        Reflections reflections = new Reflections("com.gromod.client");

        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(GuiModule.class);

        Map<String, Map<String, Object>> valuesMap = new HashMap<>();

        for (Class<?> clazz : classes) {
            Map<String, Object> fieldValues = new HashMap<>();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getAnnotation(GuiField.class) == null) continue;

                field.setAccessible(true);
                try {
                    Object instance = clazz.getMethod("getInstance").invoke(null);
                    Object value = field.get(instance);

                    fieldValues.put(field.getName(), value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!fieldValues.isEmpty()) {
                valuesMap.put(clazz.getSimpleName(), fieldValues);
            }
        }

        saveToJson(valuesMap, "output.json");
    }


    private void saveToJson(Map<String, Map<String, Object>> valuesMap, String fileName) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        String separator = "";

        for (Map.Entry<String, Map<String, Object>> entry : valuesMap.entrySet()) {
            String className = entry.getKey();
            Map<String, Object> fieldValues = entry.getValue();

            jsonBuilder.append(separator);
            jsonBuilder.append("{");
            jsonBuilder.append("\"className\":\"").append(className).append("\",");

            for (Map.Entry<String, Object> fieldEntry : fieldValues.entrySet()) {
                String fieldName = fieldEntry.getKey();
                Object fieldValue = fieldEntry.getValue();

                jsonBuilder.append("\"").append(fieldName).append("\":");

                if (fieldValue instanceof String) {
                    jsonBuilder.append("\"").append(fieldValue).append("\",");
                } else {
                    jsonBuilder.append(fieldValue).append(",");
                }
            }

            // Remove the last comma
            if (jsonBuilder.charAt(jsonBuilder.length() - 1) == ',') {
                jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
            }

            jsonBuilder.append("}");
            separator = "," + System.lineSeparator(); // Add a newline after each class
        }

        jsonBuilder.append("]");

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(jsonBuilder.toString());
            System.out.println("Data saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

