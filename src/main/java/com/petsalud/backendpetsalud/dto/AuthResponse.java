package com.petsalud.backendpetsalud.dto;

public record AuthResponse(String token, String tipo, String email, String rol) { }
