package br.com.tama.projekt.repositories;

import br.com.tama.projekt.enums.ProjetoStatus;
import br.com.tama.projekt.models.Projeto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {

    /** Busca por projetos utilizando o título **/
    Optional<Projeto> findByTituloIgnoreCase(String titulo);

    /** Busca todos os projetos ordenados pela data de previsão de entrega **/
    Page<Projeto> findAllByOrderByPrevisaoEntregaAsc(Pageable pageable);

    /** Busca todos os projetos que possuam um status informado ordenados pela data de previsão de entrega,
     * contendo configurações de paginação do resultado
     * **/
    Page<Projeto> findByStatusEqualsOrderByPrevisaoEntregaAsc(ProjetoStatus projetoStatus, Pageable pageable);

    /** Busca todos os projetos favoritados **/
    List<Projeto> findAllByFavoritadoEquals(Boolean favoritado);

    /** Busca os 5 primeiros projetos de um determinado status ordenados pela data de previsão de entega **/
    List<Projeto> findFirst5ByStatusEqualsOrderByPrevisaoEntregaAsc(ProjetoStatus projetoStatus);
}
