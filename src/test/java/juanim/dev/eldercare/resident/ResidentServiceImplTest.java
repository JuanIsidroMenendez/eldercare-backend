package juanim.dev.eldercare.resident;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import juanim.dev.eldercare.resident.dtos.ResidentDTORequest;
import juanim.dev.eldercare.resident.dtos.ResidentDTOResponse;
import juanim.dev.eldercare.resident.valueObject.DependencyGrade;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)   
class ResidentServiceImplTest {

    @Mock
    private ResidentRepository repository;   

    @Mock
    private ResidentMapper mapper;          

    @InjectMocks
    private ResidentServiceImpl service;     

    @Test
    void create_guardaYDevuelveElResidente() {
        
        ResidentDTORequest request = new ResidentDTORequest(
                null, "Iker", "Ardu Engo", 85, DependencyGrade.GRADO_I,
                "Calle de la Chavala 45", "Madrid", "28012",
                "Joseppe Ardu", "Hijo", "600123456", "Marta Ruiz");
        ResidentEntity entity = ResidentEntity.builder().firstName("Iker").build();
        ResidentDTOResponse response = new ResidentDTOResponse(
                1L, null, "Iker", "Ardu Engo", 85, DependencyGrade.GRADO_I,
                "Dependencia leve", "Calle de la Chavala 45", "Madrid", "28012",
                "Joseppe Ardu", "Hijo", "600123456", "Marta Ruiz");

        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        ResidentDTOResponse result = service.create(request);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.firstName()).isEqualTo("Iker");
        verify(repository).save(entity);   
    }

    @Test
    void findById_inexistente_lanza404() {
     
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Residente no encontrado");

        verify(mapper, never()).toResponse(any());   
    
}
}