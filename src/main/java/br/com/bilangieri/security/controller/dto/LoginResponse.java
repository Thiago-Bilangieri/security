package br.com.bilangieri.security.controller.dto;

public record LoginResponse(String accessToken,Long expiresIn) {
}
