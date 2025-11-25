package br.unitins.topicos1.ewine.model.perfil;

public enum Perfil {
    ADMIN(1L, "Admin"),
    CLIENTE(2L, "Cliente");

    public final Long id;
    public final String label;

    Perfil(Long id, String label) {
        this.id = id;
        this.label = label;
    }

    public static Perfil valueOf(Long id) {
        if (id == null)
            return null;
        
        for (Perfil perfil : Perfil.values())
            if (perfil.id.equals(id))
                return perfil;
        
        throw new IllegalArgumentException("ID inválido: " + id);
    }
}
