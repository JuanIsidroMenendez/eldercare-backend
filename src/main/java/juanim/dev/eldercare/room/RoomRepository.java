package juanim.dev.eldercare.room;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    boolean existsByNumber(String number);   // Evitará números duplicados en la habitación.
} 
