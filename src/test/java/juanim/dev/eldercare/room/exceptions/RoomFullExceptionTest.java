package juanim.dev.eldercare.room.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoomFullExceptionTest {

    @Test
    void carriesMessage() {
        RoomFullException ex = new RoomFullException("habitación completa");

        assertThat(ex.getMessage()).isEqualTo("habitación completa");
    }

    @Test
    void isRuntimeException() {
        // Al ser RuntimeException (unchecked), no obliga a try/catch ni a declararla
        assertThat(new RoomFullException("x")).isInstanceOf(RuntimeException.class);
    }
}