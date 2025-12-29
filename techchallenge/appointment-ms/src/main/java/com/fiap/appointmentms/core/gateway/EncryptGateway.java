package com.fiap.appointmentms.core.gateway;

public interface EncryptGateway {

    String encrypt(String data);
    Boolean validate(String data, String encryptedData);
}
