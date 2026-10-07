package juanim.dev.eldercare.resident;

import jakarta.persistence.Embeddable;

import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferenceRelative {
    private String name;          
    private String relationship; 
    private String phone;         
}