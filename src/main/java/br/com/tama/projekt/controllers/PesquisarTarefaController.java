package br.com.tama.projekt.controllers;

import br.com.tama.projekt.services.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *  Classe Controller da página de Pesquisa
 *  Contém os métodos tratar requisições relacionadas a pesquisa de Tarefas
 *
 * @author Wilson Ferreira
 */
@Controller
@RequestMapping("/tarefa")
public class PesquisarTarefaController {


    /**
     * Injetando camada de serviço da 'Tarefa' para ter acesso aos metódos de consulta da entidade
     */
    @Autowired
    private TarefaService tarefaService;

    /** Método mapeando o caminho da raiz desse controlador.
     *  Ao acessar esse caminho é devolvida a view contendo a tela de pesquisa.
     * @return objeto da view contendo o nome da view a ser carregada.
     */
    @GetMapping()
    public ModelAndView carregarPesquisa(){
        ModelAndView mv = new ModelAndView("views/PesquisarTarefas");

        return mv;
    }

    /** Método para processar as pesquisas
     * Ao enviar uma requsição POST para esse caminho contendo uma String com título da tarefa,
     * o método acessa o serviço chamando a pesquisa de tarefas por títulos, retornando uma lista com 0 ou mais elementos.
     * Essa lista é incluida na view junto com um atributo para indicar que na View o subtitulo
     * do card de tarefas deve ser exibido.
     * A view a ser retornada é apenas o fragmento 'tarefas-block' do template 'ListarTarefas', para faciltiar a injeção do
     * HTML na página via javascript
     *
     * @param tituloTarefa - Recebe uma String contendo parte, ou todo, título de uma tarefa.
     * @return objeto da view contendo a view e seus atributos extras.
     */
    @PostMapping("/pesquisar")
    public ModelAndView pesquisarPorNome(String tituloTarefa){
        ModelAndView mv = new ModelAndView("views/ListarTarefas :: tarefas-block");
        
        mv.addObject("tarefas", tarefaService.pesquisarPorTitulo(tituloTarefa));
        mv.addObject("exibirSubtitulo", true);

        return mv;
    }
}
