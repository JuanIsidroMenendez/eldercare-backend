package juanim.dev.eldercare.resident;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import juanim.dev.eldercare.resident.valueObject.Address;
import juanim.dev.eldercare.resident.valueObject.DependencyGrade;
import juanim.dev.eldercare.resident.valueObject.ReferenceRelative;
import juanim.dev.eldercare.room.RoomEntity;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "residents")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class ResidentEntity {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //foto (pendiente de pasar a DB. Comentario Giaco grupal)
    private String photo;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DependencyGrade dependencyGrade;

    @Embedded 
    private Address address;

    @Embedded 
    private ReferenceRelative referenceRelative;

    private String professional;

    // Relación con entidad Room
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private RoomEntity room;

}
