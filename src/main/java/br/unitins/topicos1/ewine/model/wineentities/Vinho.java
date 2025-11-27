package br.unitins.topicos1.ewine.model.wineentities;

import java.util.List;

import br.unitins.topicos1.ewine.model.locationentities. Pais;
import br.unitins.topicos1.ewine.model.others.DefaultEntity;
import br.unitins.topicos1.ewine.model. others.Marca;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta. persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter     
public class Vinho extends DefaultEntity {

    @NotBlank(message = "Nome do vinho é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;
    
    @Size(max = 1000, message = "Descrição não pode exceder 1000 caracteres")
    private String descricao;
    
    private String imagem;
    
    private Double teorAlcoolico;
    
    private Integer volume; // Em ml (ex: 750, 1000)
    
    private String harmonizacao;
    
    private String notasDegustacao;
    
    private String servicoTemperatura; // Ex: "16-18°C"
    
    // ============================================
    // CAMPOS REMOVIDOS (agora estão em Produto):
    // - private Double preco;        ← PRODUTO
    // - private Integer quantEstoque; ← ESTOQUE  
    // - private String sku;          ← PRODUTO
    // ============================================

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

    // ============================================
    // MÉTODOS DE CONVENIÊNCIA:
    // ============================================
    
    public String getTeorAlcoolicoFormatado() {
        return teorAlcoolico != null ?  teorAlcoolico + "%" : null;
    }
    
    public String getVolumeFormatado() {
        return volume != null ? volume + "ml" : null;
    }
    
    public String getNomeCompleto() {
        StringBuilder nome = new StringBuilder();
        if (this.nome != null) nome.append(this.nome);
        if (safra != null && safra.getAno() != null) {
            nome.append(" ").append(safra.getAno());
        }
        if (volume != null) {
            nome.append(" - ").append(getVolumeFormatado());
        }
        return nome.toString();
    }
}