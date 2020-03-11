package br.com.tama.projekt.repositories;

import br.com.tama.projekt.models.Projeto;
import br.com.tama.projekt.models.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório de acesso ao banco de dados utilizando, herda a clase JpaRepository
 *
 * @author Wilson Ferreira
 */
@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {

    /** Busca por uma única tarea utilizando o título informado **/
    Optional<Tarefa> findByTituloIgnoreCaseAndProjeto(String titulo, Projeto Projeto);

    /** Busca por tarefas que façam parte um projeto informado **/
    List<Tarefa> findByProjeto(Projeto projeto);


    /** Busca por tarefas que contennham em parte do tútlo a string informada **/
    List<Tarefa> findByTituloContainingIgnoreCase(String tituloTarefa);

}
