package br.com.tama.projekt.models;

import br.com.tama.projekt.enums.ProjetoStatus;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * Entiidade representando o Projeto.
 *
 * @author Wilson Ferreira
 */

@Entity
@Data
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProjeto;

    @NotBlank(message = "Título deve ser informado.")
    @Size(min = 5, message = "O título do projeto deve possuir pelo menos 5 caracteres")
    private String titulo;

    @NotNull(message = "Informe uma previsão de entrega.")
    private LocalDate previsaoEntrega;

    private Boolean favoritado;

    /**
     * Anotação configurada para salvar no banco uma representação númerica do enumerador, em vez da sua descrição.
     * **/
    @Enumerated(EnumType.ORDINAL)
    private ProjetoStatus status;

    /**
     * Cascade definido para remover as tarefas que possuem relacionamento com o projeto que foi excluído.
     */
    @OneToMany(mappedBy = "projeto", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Tarefa> tarefas;


    /** Método para verificar se o objeto instanciado trata-se de um objeto novo ou já existente.
     *  Se o objeto não possuir ID, então ele ainda não foi salvo no banco de dados.
     * @return Retorna se True se o objeto for novo
     */
    public boolean isNovo() {

        return idProjeto == null;
    }


    /** Método para verificar se a entrega do projeto está atrasada
     *  Checa se o Status do projeto é ABERTO e a data atual é posterior a data de entrega.
     * @return Retorna se True se projeto estiver atrasado.
     */
    public boolean isAtrasado() {
        LocalDate hoje = LocalDate.now();

        return status == ProjetoStatus.ABERTO && hoje.isAfter(previsaoEntrega);
    }

    /**
     * Método para verificar se dois objetos da classe Projeto são iguais.
     * A validação é feita apenas com o ID, se 2 objetos distintitos possuirem o mesmo "id" então são o mesmo Projeto.
     * @param o - Objeto a ser comparado.
     * @return Retorna true se o resultado da comparação for verdadeiro.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Projeto projeto = (Projeto) o;
        return idProjeto.equals(projeto.idProjeto);
    }
}
