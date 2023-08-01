package ru.practicum.ewm.main;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Explore with me Api",
                description = "Event discovery platforms", version = "1.1",
                contact = @Contact(
                        name = "Azarkin Vitaliy",
                        email = "v.azarkin@gmail.com",
                        url = "https://github.com/Azark1n"
                )
        )
)
public class OpenApiConfig {
}
