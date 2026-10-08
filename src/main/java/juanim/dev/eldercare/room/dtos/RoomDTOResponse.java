package juanim.dev.eldercare.room.dtos;

import juanim.dev.eldercare.room.RoomType;

public record RoomDTOResponse(
        Long id,
        String number,
        RoomType type,
        int capacity         
) {}