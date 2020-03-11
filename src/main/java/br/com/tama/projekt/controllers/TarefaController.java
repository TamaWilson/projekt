package br.com.tama.projekt.controllers;

import br.com.tama.projekt.exceptions.CadastroException;
import br.com.tama.projekt.models.Projeto;
import br.com.tama.projekt.models.Tarefa;
import br.com.tama.projekt.services.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Classe Controller da Tarefa
 * Contém os métodos tratar requisições relacionadas ao cadastro e listagem da entidade Tarefa.
 *
 * @author Wilson Ferreira
 */

//
//* Este controller sempre recebe como parameto no caminho da url um id de projeto
//
@Controller
@RequestMapping(value = "/projeto/{idProjeto}/tarefa")
public class TarefaController {

    /**
     * Injetando camada de serviço da 'Tarefa' para ter acesso aos metódos de consulta  e persitencia da entidade
     */
    @Autowired
    private TarefaService tarefaService;

    /**
     * Método para lidar com as requisões feitas para a raiz desse controller.
     * Deve receber por parametro da url um Id de projeto existente.
     * Acessa a o serviço para obter uma lista de terefas do Projeto informado.
     * Inclui essa lista na view, com um atributo extra contendo o Projeto
     * * @return Retorna para o front-end o objeto para renderização do template ListarTarefas.
     */
    @GetMapping()
    public ModelAndView listarTarefas(@PathVariable("idProjeto") Projeto projeto) {
        ModelAndView mv = new ModelAndView("views/ListarTarefas");
        mv.addObject("tarefas", tarefaService.pesquisarPorProjeto(projeto));
        mv.addObject("projeto", projeto);

        return mv;
    }

    /**
     * Método para lidar com as requisões feitas para o cadastro de tarefas
     * O método trata da exibição do formulário para 'Tarefas' novas ou pré-existentes
     * Recebe na requisção um objeto do tipo Tarefa, desse modo é possível acessar seus campos diretamente na view.
     *
     * @param projeto Objeto projeto instanciado de acordo com o Id informado
     * @param tarefa tarefa instanciada pelo Spring para acesso dos campos na view.
     * @return Retorna para o front-end objeto para rendereização do template CadastrarTarefa
     */
    @GetMapping("/cadastrar")
    public ModelAndView novo(@PathVariable("idProjeto") Projeto projeto, Tarefa tarefa) {
        return new ModelAndView("views/CadastrarTarefa");
    }

    /**
     * Método para lidar com as requisões feitas para salvar a Tarefa
     * O método trata da persitência da Tarefa nova ou alterações em Tarefas pré-existentes.
     * Pode ser acessado pelo caminho '/cadastrar' quando se tratar de uma tarefa nova ou de /{id} quando for edição
     * O método exije um objeto Tarefa com todos os seu campos válidos confome descritos no modelo.
     *
     * Caso o objeto esteja válido é feita uma tentativa de persistência enviado o objeto para o tarefaService.salvar()
     * Se a exceção CadastroException for lançada, a requisição é rejeitada e o projeto e o fluxo  é direcionado para o
     * método novo() para exibição do formulário de edição e mensagens de erro.
     *
     * Se a Tarefa conter erros, a método novo() é chamado passando o objeto, para exibição do formulário com as mensagens de erro.
     * Após a pesistência é feito o redirecionamento para a Lista de Tarefas do Projeto, incluindo uma mensagem de sceusso.
     **
     * @param projeto - Objeto Projeto passado para esse método.
     * @param result - Objeto que contém validações, erros e outros bindings
     * @param attributes - Atributos extras a serem processados na view
     * @return view para ser processada exibida de acordo com o fluxo do método.
     */
    @RequestMapping(value = {"/cadastrar", "{\\d+}"}, method = RequestMethod.POST)
    public ModelAndView salvar(@PathVariable("idProjeto") Projeto projeto, @Valid Tarefa tarefa, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return novo(projeto, tarefa);
        }

        try {
            tarefa.setProjeto(projeto);
            tarefaService.salvar(tarefa);
        } catch (CadastroException e) {
            result.rejectValue("titulo", e.getMessage(), e.getMessage());
            return novo(projeto, tarefa);
        }

        attributes.addFlashAttribute("mensagem", "Tarefa salva com sucesso!");

        ModelAndView mv = new ModelAndView("redirect:/projeto/{id}/tarefa");
        mv.addObject("id", projeto);

        return mv;
    }

    /**
     * Método para lidar com edições da Tarefa
     * Acessando uma url com um Id de uma Tarefa existente exibe formulários com dados para edição.
     * Utilizando a Tarefa recebida na requisição, repassa para o método novo() que devevolverá o objeto da view.
     * @param projeto - Projeto existente no sistema
     * @param tarefa - Projeto existente no sistema
     * @return Retorna para o front-end objeto para rendereização do template CadastrarTarefa
     */
    @GetMapping(value = "/{idTarefa}")
    public ModelAndView editar(@PathVariable("idProjeto") Projeto projeto, @PathVariable("idTarefa") Tarefa tarefa) {

        ModelAndView mv = novo(projeto, tarefa);
        mv.addObject(tarefa);

        return mv;
    }

    /**
     * Método para lidar com exclusões de uma Tarefa
     * Recebe via parameto da url um Id de uma tarefa a ser excluida.
     * A tarefa informada é passada para o service que tentará realizar a exclusão.
     *
     * Se algum erro for lançado, uma mensagem de erro é adicionada à view e o método tem seu retorno antecipado.
     * Caso nerhum erro ocorra uma mensagem de sucesso é adicionada.
     *
     * Após o processamento é feito o redirecionamento para a lista de Tarefas do Projeto, tanto no sucesso quanto no erro.
     *
     * @param projeto - Projeto existente no sistema
     * @param tarefa - Tarefa existente no sistema
     * @param attributes - Atributos extras da view
     * @return Objeto da view com redirecionamento para o caminho 'projeto/{idProjeto}/tarefa'
     */
    @GetMapping(value = "/{idTarefa}/excluir")
    public ModelAndView excluir(@PathVariable("idProjeto") Projeto projeto, @PathVariable("idTarefa") Tarefa tarefa,
                                RedirectAttributes attributes) {
        ModelAndView mv = new ModelAndView("redirect:/projeto/{id}/tarefa");
        mv.addObject("id", projeto);

        try {
            tarefaService.excluir(tarefa);
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagemErro", "Erro ao excluir a tarefa");
            return mv;
        }

        attributes.addFlashAttribute("mensagem", "Tarefa excluída com sucesso!");

        return mv;
    }
}
