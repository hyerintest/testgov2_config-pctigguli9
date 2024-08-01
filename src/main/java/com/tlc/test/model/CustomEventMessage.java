package com.tlc.test.model;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Map;
import java.util.UUID;

@Builder
@AllArgsConstructor
public class CustomEventMessage {
    private String messageId;
    private Map<String, Object> header;
    private Object payload;
    private String correlationId;
    private Object correlationData;

    public CustomEventMessage() {
        this.messageId = UUID.randomUUID().toString();
    }

    public CustomEventMessage messageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public CustomEventMessage header(Map<String, Object> header) {
        this.header = header;
        return this;
    }

    public CustomEventMessage addHeader(String Key, Object value) {
        this.header.put(Key, value);
        return this;
    }

    public CustomEventMessage addHeaders(Map<String, Object> header) {
        this.header.putAll(header);
        return this;
    }

    public CustomEventMessage correlationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public CustomEventMessage payload(Object payload) {
        this.payload = payload;
        return this;
    }

    public CustomEventMessage correlationData(Object correlationData) {
        this.correlationData = correlationData;
        return this;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Map<String, Object> getHeader() {
        return header;
    }

    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

    public Object getPayload() {
        return payload;
    }

    public <T> T getPayloadAsType(Class<T> clazz) {
        Gson gson = new Gson();
        String payload = gson.toJson(this.payload);
        return gson.fromJson(payload, clazz);
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Object getCorrelationData() {
        return correlationData;
    }

    public void setCorrelationData(Object correlationData) {
        this.correlationData = correlationData;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", header=" + header +
                ", payload=" + payload +
                ", correlationId='" + correlationId + '\'' +
                ", correlationData=" + correlationData +
                '}';
    }
}
