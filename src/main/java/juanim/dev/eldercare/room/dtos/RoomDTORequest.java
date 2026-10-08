package juanim.dev.eldercare.room.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import juanim.dev.eldercare.room.RoomType;

public record RoomDTORequest(
        @NotBlank String number,
        @NotNull RoomType type
) {}