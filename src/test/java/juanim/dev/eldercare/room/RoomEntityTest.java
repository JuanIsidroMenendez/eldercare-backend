package juanim.dev.eldercare.room;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoomEntityTest {

    @Test
    void builderCreatesRoomWithFields() {
        RoomEntity room = RoomEntity.builder()
                .id(1L)
                .number("101A")
                .type(RoomType.INDIVIDUAL)
                .build();

        assertThat(room.getId()).isEqualTo(1L);
        assertThat(room.getNumber()).isEqualTo("101A");
        assertThat(room.getType()).isEqualTo(RoomType.INDIVIDUAL);
    }

    @Test
    void residentsDefaultsToEmptyListNotNull() {
    
        RoomEntity room = RoomEntity.builder()
                .number("102A")
                .type(RoomType.DOUBLE)
                .build();

        assertThat(room.getResidents()).isNotNull().isEmpty();
    }

    @Test
    void settersUpdateFields() {
        RoomEntity room = new RoomEntity();
        room.setNumber("201B");
        room.setType(RoomType.DOUBLE);

        assertThat(room.getNumber()).isEqualTo("201B");
        assertThat(room.getType()).isEqualTo(RoomType.DOUBLE);
    }
}