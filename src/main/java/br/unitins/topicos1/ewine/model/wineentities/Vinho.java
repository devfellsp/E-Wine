package br.unitins.topicos1.ewine.model.wineentities;

import java.util.List;

import br.unitins.topicos1.ewine.model.locationentities.Pais;
import br.unitins.topicos1.ewine.model.others.DefaultEntity;
import br.unitins.topicos1.ewine.model.others.Marca;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable; // Import adicionado
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Vinho extends DefaultEntity {

    private String nome;
    private Double preco;
    private Integer quantEstoque;
    private String descricao;
    private String imagem;
    private String sku;

    // RELAÇÃO CORRIGIDA: Vinho é agora o lado PROPRIETÁRIO (dono da tabela de junção)
    @ManyToMany
    @JoinTable(
        name = "vinho_uva", // Nome da tabela de junção
        joinColumns = @JoinColumn(name = "id_vinho"),
        inverseJoinColumns = @JoinColumn(name = "id_uva")
    )
    private List<Uva> uvas;


    @ManyToOne
    @JoinColumn(name = "id_tipo_vinho")
    private TipoVinho tipoVinho;


    @ManyToOne
    @JoinColumn(name = "id_pais")
    private Pais paisDeOrigem;

    @ManyToOne
    @JoinColumn(name = "id_estilo")
    private Estilo estilo;

    @ManyToOne
    @JoinColumn(name = "id_ocasiao")
    private Ocasiao ocasiao;
    
    @ManyToOne
    @JoinColumn(name = "id_safra")
    private Safra safra;
    
    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca marca;

    // ************************************************
    // Nota: Lembre-se que o Uva.java precisa do mappedBy correto:
    // @ManyToMany(mappedBy = "uvas")
    // private List<Vinho> vinhos;
    // ************************************************
}