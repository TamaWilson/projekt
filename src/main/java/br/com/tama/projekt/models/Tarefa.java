package br.com.tama.projekt.models;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTarefa;

    @NotBlank(message = "Título deve ser informado.")
    @Size(min=5, message = "O título da tarefa deve possuir pelo menos 5 caracteres")
    private String titulo;

    @NotBlank(message = "Informe a descrição da tarefa")
    @Size(max = 140, message = "Descrição deve possuir até 140 caracteres")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_projeto")
    @ToString.Exclude
    private Projeto projeto;

    public boolean isNovo(){

        return idTarefa == null;
    }
}
