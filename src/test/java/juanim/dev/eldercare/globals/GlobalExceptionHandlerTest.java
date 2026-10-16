package juanim.dev.eldercare.globals;

import juanim.dev.eldercare.globals.exceptions.GlobalExceptionHandler;
import juanim.dev.eldercare.room.exceptions.RoomFullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
      
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleRoomFullReturnsConflict() {
    
        RoomFullException ex = new RoomFullException("La habitación está completa");

        ResponseEntity<Map<String, Object>> response = handler.handleRoomFull(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody())
                .containsEntry("status", 409)
                .containsEntry("error", "Conflict")
                .containsEntry("message", "La habitación está completa")
                .containsKey("timestamp");
    }

    @Test
    void handleValidationReturnsBadRequestWithFieldErrors() {
        
        FieldError fieldError = new FieldError("resident", "firstName", "El nombre es obligatorio");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, Object>> response = handler.handleValidation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody())
                .containsEntry("status", 400)
                .containsEntry("error", "Bad Request")
                .containsEntry("message", "Datos no válidos")
                .containsKey("timestamp");

        @SuppressWarnings("unchecked")
        Map<String, String> fields = (Map<String, String>) response.getBody().get("fields");
        assertThat(fields).containsEntry("firstName", "El nombre es obligatorio");
    }
}