package com.gobeyond.registration;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceTest {
    private final RegistrationService service = new RegistrationService();

    @Test
    void shouldRegisterValidUser() {
        String result = service.register("Faisal", "faisal@gmail.com");
        assertEquals("Registration successful for Ahmad", result);
    }

    @Test
    void shouldRejectEmptyName() {
        assertThrows(IllegalArgumentException.class,
                () -> service.register("", "faisal@gmail.com"));
    }

    @Test
    void shouldRejectInvalidEmail() {
        assertThrows(IllegalArgumentException.class,
                () -> service.register("Faisal", "invalid-email"));
    }
}