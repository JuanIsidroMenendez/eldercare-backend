package juanim.dev.eldercare.room;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import juanim.dev.eldercare.resident.ResidentEntity;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;         

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType type;      
    
    @OneToMany(mappedBy = "room")
    @Builder.Default
    private List<ResidentEntity> residents = new ArrayList<>();
}