package br.unitins.topicos1.ewine.model.locationentities;

import br.unitins.topicos1.ewine.model.others.DefaultEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Cidade extends DefaultEntity {
    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;

}