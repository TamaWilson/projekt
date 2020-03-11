package br.com.tama.projekt.enums;

/**
 * Enumerador para parametrizar os possíveis status que um projeto pode ter
 *
 * @author Wilson Ferreira
 */
public enum ProjetoStatus {
    ABERTO("Aberto"),
    FINALIZADO("Finalizado");

    private String descricao;

    ProjetoStatus(String descricao){

        this.descricao = descricao;
    }

    public String getDescricao(){

        return descricao;
    }
}
