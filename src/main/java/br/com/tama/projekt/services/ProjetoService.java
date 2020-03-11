package br.com.tama.projekt.services;

import br.com.tama.projekt.enums.ProjetoStatus;
import br.com.tama.projekt.exceptions.CadastroException;
import br.com.tama.projekt.models.Projeto;
import br.com.tama.projekt.repositories.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * Classe de serviço para validações e acesso ao repositório de Projeto
 *
 * @author Wilson Ferreira
 */
@Service
public class ProjetoService {

    /** Injeta na classe o repositório de Projeto para acesso ao banco de dados **/
    @Autowired
    private ProjetoRepository projetos;


    /** Método para salvar o projeto
     *  Busca no banco de dados um projeto pelo título, utilizando o valor do projeto a ser salvo.
     *
     *  Se o projeto exisitir:
     *      - Caso o projeto não seja o mesmo projeto que foi passadao para o método;
     *      - Ou caso não seja um projeto novo
     * Então: Lança exceção de cadastro, informando que o projeto já existe.
     *
     * Se o projeto passar pela validação acima e for novo, o atributo favoritado é definido como falso e o status como ABERTO.
     * O projeto então é passado para o repositório realizar a persitência.
     *
     * @param projeto Recebe um projeto
     * @throws CadastroException Lança exceção caso o projeto já exista
     */
    public void salvar(Projeto projeto) {

        Optional<Projeto> projetoOptional = projetos.findByTituloIgnoreCase(projeto.getTitulo().trim());

        if (projetoOptional.isPresent()) {

             if(!projetoOptional.get().equals(projeto) || projeto.isNovo()) {
                 throw new CadastroException("Já existe um projeto com esse título");
             }
        }

        if(projeto.isNovo()) {
            projeto.setFavoritado(false);
            projeto.setStatus(ProjetoStatus.ABERTO);
        }

        projetos.save(projeto);
    }

    /**
     * Busca todos os projetos do banco de dados, ordenados pela data de previsão.
     * A quantidade de resultados é limitada pelo Pageable.
     *
     * @param pageable - Configurações de paginação
     * @return Page contendo lista de projetos e atributos da paginação.
     */
    public Page<Projeto> buscarTodos(Pageable pageable) {

        return projetos.findAllByOrderByPrevisaoEntregaAsc(pageable);
    }

    /** Método para excluir um projeto.
     * Recebe um projeto e repassa para o repositório realizar a exclusão
     *
     * @param projeto - Projeto a ser excluído.
     */
    public void excluir(Projeto projeto) {

        projetos.delete(projeto);
    }

    /**
     * Busca todos os projetos favoritados no banco de dados
     * Por padrão envia sempre True para o repositório, retornando apenas os favoritos.
     *
     * @return Lista de projetos obitdos no banco de dados.
     */
    public List<Projeto> buscarFavoritos() {

       return projetos.findAllByFavoritadoEquals(true);
    }

    /**
     * Busca os 5 próximos projetos a serem entregues
     * Por padrão considera entregas apenas protos com status ABERTO.
     *
     * @return Lista de projetos obitdos no banco de dados.
     */
    public List<Projeto> buscarProximasEntregas() {

       return projetos.findFirst5ByStatusEqualsOrderByPrevisaoEntregaAsc(ProjetoStatus.ABERTO);
    }

    /**
     * Busca no banco de dados projetos de acordo com status informado.
     * Os resultados são limitados pelo Pageable e são ordenados pela data de entrega.
     *
     * @return Page contendo lista de projetos e atributos da paginação.
     */
    public Page<Projeto> buscaPorStatus(ProjetoStatus projetoStatus, Pageable pageable) {
        return  projetos.findByStatusEqualsOrderByPrevisaoEntregaAsc(projetoStatus,pageable);
    }

}
