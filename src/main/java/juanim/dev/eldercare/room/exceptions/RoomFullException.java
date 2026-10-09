package juanim.dev.eldercare.room.exceptions;

// Saltar al asignar a un residente a una habitación completa.
public class RoomFullException extends RuntimeException {
    public RoomFullException(String message) {
        super(message);
    }
}