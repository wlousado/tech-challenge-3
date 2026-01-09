package com.fiap.appointmentms.infra.gateway.gson;

import com.fiap.appointmentms.core.gateway.SerializerGateway;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

@Service
public class SerializerGsonGateway implements SerializerGateway {

    private final Gson gson;

    public SerializerGsonGateway(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String serialize(Object object) {
        return gson.toJson(object);
    }
}
