package juanim.dev.eldercare.resident.valueObject;

public enum DependencyGrade {

    GRADO_I("Dependencia leve"),
    GRADO_II("Dependencia moderada"),
    GRADO_III("Dependencia severa");

    private final String description;

    DependencyGrade(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
