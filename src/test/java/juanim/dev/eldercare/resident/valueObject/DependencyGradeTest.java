package juanim.dev.eldercare.resident.valueObject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DependencyGradeTest {

    @Test
    void hasExpectedValues() {
        // El enum debe tener exactamente ésto.
        assertThat(DependencyGrade.values())
                .containsExactly(
                        DependencyGrade.SIN_GRADO,
                        DependencyGrade.GRADO_I,
                        DependencyGrade.GRADO_II,
                        DependencyGrade.GRADO_III
                );
    }

    @Test
    void eachValueHasItsDescription() {
        //Cada grado debe tener la descripción esperada.
        assertThat(DependencyGrade.SIN_GRADO.getDescription()).isEqualTo("Sin grado de dependencia");
        assertThat(DependencyGrade.GRADO_I.getDescription()).isEqualTo("Dependencia leve");
        assertThat(DependencyGrade.GRADO_II.getDescription()).isEqualTo("Dependencia moderada");
        assertThat(DependencyGrade.GRADO_III.getDescription()).isEqualTo("Dependencia severa");
    }

    @Test
    void valueOfResolvesByName() {
        // valueOf debe resolver el nombre del enum (lo que va cn JSON).
        assertThat(DependencyGrade.valueOf("GRADO_II")).isEqualTo(DependencyGrade.GRADO_II);
    }
}