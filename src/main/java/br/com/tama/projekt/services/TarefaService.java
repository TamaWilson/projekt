package br.com.tama.projekt.services;

import br.com.tama.projekt.exceptions.CadastroException;
import br.com.tama.projekt.models.Projeto;
import br.com.tama.projekt.models.Tarefa;
import br.com.tama.projekt.repositories.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Classe de serviço para validações e acesso ao repositório da Tarefa
 *
 * @author Wilson Ferreira
 */
@Service
public class TarefaService {

    /**
     * Injeta na classe o repositório de Tarefa para acesso ao banco de dados
     **/
    @Autowired
    private TarefaRepository tarefas;

    /**
     * Método para salvar a tarefa
     * Busca no banco de dados uma tarefa pelo título, utilizando o valor da tarefa a ser salva.
     *
     * Se a tarefa existir:
     * - Caso a tarefa não seja a mesmo tarefa que foi passadao para o método;
     * - Ou caso não seja uma tarefa nova
     * Então: Lança exceção de cadastro, informando que a tarefa já existe.
     *
     * Se a tarefa passar pela validação acima, a tarefa então é passada para o repositório realizar a persitência.
     *
     * @param tarefa Recebe um projeto
     * @throws CadastroException Lança exceção caso o projeto já exista
     */
    public void salvar(Tarefa tarefa) {

        Optional<Tarefa> projetoOptional = tarefas.findByTituloIgnoreCaseAndProjeto(tarefa.getTitulo().trim(), tarefa.getProjeto());

        if (projetoOptional.isPresent()) {
            if (!projetoOptional.get().equals(tarefa) || tarefa.isNovo()) {
                throw new CadastroException("Já existe uma tarefa com esse título");
            }
        }

        tarefas.save(tarefa);
    }

    /** Busca no banco de dados por tarefas que pertençam a um projeto
     *
     * @param projeto - Projeto pai das tarefas
     * @return Retorna lista de Tarefas pertencentes ao projeto.
     */
    public List<Tarefa> pesquisarPorProjeto(Projeto projeto) {
        return tarefas.findByProjeto(projeto);
    }

    /** Método para excluir uma tarefa.
     * Recebe uma tarefa e repassa para o repositório realizar a exclusão
     *
     * @param tarefa - Projeto a ser excluído.
     */
    public void excluir(Tarefa tarefa) {

        tarefas.delete(tarefa);
    }

    /** Pesquisar por uma tarefa que contentar um determinado termo no seu título.
     * Informando uma string para o método, é realizada uma busca no banco de dados por tarefas que contenham
     * a String informada no título;
     * @param nomeTarefa - String para ser utilizada na pesquisa
     * @return Retorna lista de tarefas filtradas pela pesquisa.
     */
    public List<Tarefa> pesquisarPorTitulo(String nomeTarefa) {

        return tarefas.findByTituloContainingIgnoreCase(nomeTarefa);
    }
}
