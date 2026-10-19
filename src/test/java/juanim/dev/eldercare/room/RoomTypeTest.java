package juanim.dev.eldercare.room;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoomTypeTest {

    @Test
    void hasExpectedValues() {
        assertThat(RoomType.values())
                .containsExactly(RoomType.INDIVIDUAL, RoomType.DOUBLE);
    }

    @Test
    void eachTypeHasItsCapacity() {
        assertThat(RoomType.INDIVIDUAL.getCapacity()).isEqualTo(1);
        assertThat(RoomType.DOUBLE.getCapacity()).isEqualTo(2);
    }

    @Test
    void valueOfResolvesByName() {
        assertThat(RoomType.valueOf("DOUBLE")).isEqualTo(RoomType.DOUBLE);
    }
}