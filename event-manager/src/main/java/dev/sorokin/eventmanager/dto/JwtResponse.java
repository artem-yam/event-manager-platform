package dev.sorokin.eventmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class JwtResponse {

    @Schema(description = "JWT token for authenticated user",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI5OTkiLCJuYW1lIjoidGVzdF91c2VyIiwiYWRtaW4iOmZhbHNlLCJpYXQiOjE1MTYyMzkwMjJ9.yIkfC_w00lgsSCMYn0IA0UJqXEoMpnw4PvHnEXABHAc")
    String jwtToken;

}