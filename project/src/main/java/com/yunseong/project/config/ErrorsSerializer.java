package com.yunseong.project.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

    @Override
    public void serialize(Errors value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();

        value.getFieldErrors().forEach(field -> {
            try {
                gen.writeStartObject();
                gen.writeStringField(field.getField(), "field");
                gen.writeStringField(field.getObjectName(), "objectName");
                gen.writeStringField(field.getCode(), "code");
                gen.writeStringField(field.getDefaultMessage(), "defaultMessage");
                gen.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        value.getGlobalErrors().forEach(global -> {
            try {
                gen.writeStartObject();
                gen.writeStringField(global.getObjectName(), "objectName");
                gen.writeStringField(global.getCode(), "code");
                gen.writeStringField(global.getDefaultMessage(), "defaultMessage");
                gen.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gen.writeEndArray();
    }
}