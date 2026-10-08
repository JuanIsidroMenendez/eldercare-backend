package juanim.dev.eldercare.resident.objectValue;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    private String street;      
    private String city;        
    private String postalCode;  
}