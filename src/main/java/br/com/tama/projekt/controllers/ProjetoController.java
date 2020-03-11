package br.com.tama.projekt.controllers;

import br.com.tama.projekt.enums.ProjetoStatus;
import br.com.tama.projekt.exceptions.CadastroException;
import br.com.tama.projekt.models.Projeto;
import br.com.tama.projekt.services.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 *  Classe Controller do Projeto
 *  Contém os métodos tratar requisições relacionadas ao cadastro e listagem da entidade Projeto.
 *
 * @author Wilson Ferreira
 */
@Controller
@RequestMapping(value = "/projeto")
public class ProjetoController {

    /**
     * Injetando camada de serviço do 'Projeto' para ter acesso aos metódos de consulta  e persitencia da entidade
     */
    @Autowired
    private ProjetoService projetoService;


    /**
     * Método para lidar com as requisões feitas para a raiz desse controller.
     * Acessa a o serviço para obter uma lista de projetos, por padrão projetos abertos.
     * Inclui essa lista na view, com um atributo extra para indicar o tipo de filtro utilizado.
     * @return Retorna para o front-end o objeto para renderização do template ListarProjetos.
     */
    @GetMapping()
    public ModelAndView listarProjetos() {
        ModelAndView mv = new ModelAndView("views/ListarProjetos");

        Pageable pageable = PageRequest.of(0, 10);

        mv.addObject("projetos", projetoService.buscaPorStatus(ProjetoStatus.ABERTO, pageable));
        mv.addObject("statusFiltado", ProjetoStatus.ABERTO);
        return mv;
    }


    /**
     * Método para lidar com as requisões feitas para o cadastro de projetos
     * O método trata da exibição do formulário para 'Projetos' novos ou pré-existentes
     * Recebe na requisção um objeto do tipo Projeto, desse modo é possível acessar seus campos diretamente na view.
     * A lista de status é incluida para preencher o dropdown de status caso o objeto não seja novo.
     *
     * @param projeto Objeto projeto instanciado pelo Spring
     * @return Retorna para o front-end objeto para rendereização do template CadastrarProjeto
     */
    @GetMapping("/cadastrar")
    public ModelAndView novo(Projeto projeto) {
        ModelAndView mv = new ModelAndView("views/CadastrarProjeto");
        mv.addObject("statusLista", ProjetoStatus.values());
        return mv;
    }

    /**
     * Método para lidar com as requisões feitas para salvar o Projeto
     * O método trata da persitência do Projeto novo ou alterações em Projetos pré-existentes.
     * Pode ser acessado pelo caminho /cadastrar quando se tratar de um projeto novo ou de /{id} quando for edição
     * O método exije um objeto Projeto com todos os seu campos válidos confome descritos no modelo.
     *
     * Caso o objeto esteja válido é feita uma tentativa de persistência enviado o objeto para o projetoService.salvar()
     * Se a exceção CadastroException for lançada, a requisição é rejeitada e o projeto e o fluxo  é direcionado para o
     * método novo() para exibição do formulário de edição e mensagens de erro.
     *
     * Se o Projeto conter erros, a método novo() é chamado passando o objeto, para exibição do formulário com as mensagens de erro.
     * Se o projeto for novo, redireciona para a lista de Tarefas do Projeto
     * Se o projeto estiver sendo editado, redireciona para a lista de projetos.
     *
     * Caso o projeto tenha persistido sem erros uma mensagem é adicionada ao objeto da view.
     *
     * @param projeto - Objeto Projeto passado para esse método.
     * @param result - Objeto que contém validações, erros e outros bindings
     * @param attributes - Atributos extras a serem processados na view
     * @return view para ser processada exibida de acordo com o fluxo do método.
      */
    @RequestMapping(value = {"/cadastrar", "{\\d+}"}, method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Projeto projeto, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return novo(projeto);
        }

        ModelAndView mv = new ModelAndView("redirect:/projeto/{id}/tarefa");

        if (!projeto.isNovo()) {
            mv = new ModelAndView("redirect:/projeto");
        }

        try {
            projetoService.salvar(projeto);
        } catch (CadastroException e) {
            result.rejectValue("titulo", e.getMessage(), e.getMessage());
            return novo(projeto);
        }

        attributes.addFlashAttribute("mensagem", "Projeto salvo com sucesso!");

        mv.addObject("id", projeto.getIdProjeto());

        return mv;
    }

    /**
     * Método para lidar com edições do Projeto
     * Acessando uma url com um Id de um Projeto existente exibe formulários com dados para edição.
     * Utilizando o Projeto recebido na requisição, repassa para o método novo() que devevolverá o objeto da view.
     * @param projeto - Projeto existente no sistema
     * @return Retorna para o front-end objeto para rendereização do template CadastrarProjeto
     */

    @GetMapping(value = "/{id}")
    public ModelAndView editar(@PathVariable("id") Projeto projeto) {

        ModelAndView mv = novo(projeto);
        mv.addObject(projeto);

        return mv;
    }

    /**
     * Método para lidar com exclusões de um Projeto
     * Recebe via parameto da url um Id de um projeto a ser excluido.
     * O projeto informado é passado para o service que tentará realizar a exclusão.
     *
     * Se algum erro for lançado, uma mensagem de erro é adicionada à view e o método tem seu retorno antecipado.
     * Caso nerhum erro ocorra uma mensagem de sucesso é adicionada.
     *
     * Após o processamento é feito o redirecionamento para a lista de Projetos, tanto no sucesso quanto no erro.
     *
     * @param projeto - Projeto existente no sistema
     * @param attributes - Atributos extras da view
     * @return Objeto da view com redirecionamento para o caminho '/projeto'
     */
    @GetMapping(value = "/{id}/excluir")
    public ModelAndView excluir(@PathVariable("id") Projeto projeto, RedirectAttributes attributes) {

        ModelAndView mv = new ModelAndView("redirect:/projeto");

        try {
            projetoService.excluir(projeto);
        }catch (Exception e){
            attributes.addFlashAttribute("mensagemErro", "Erro ao excluir a tarefa");
            return mv;
        }

        attributes.addFlashAttribute("mensagem", "Tarefa excluída com sucesso!");

        return mv;
    }

    /**
     * Método para sinalizar se um projeto está sendo Favoritado
     * Recebe via parameto da url um Id de um proejto a ser favoritado ou desfavoritado
     *
     * Para o projeto informado é atualizado o valor do atributo Favoritado, para o inverso do valor salvo no próprio objeto.
     * Ex.: Favoritado é True ---> Favoritado vira False
     *
     * O Projeto é repassado para o service que fará a persistência.
     *
     * @param projeto - Projeto existente no sistema
     * @return String contendo nomes de classes para serem injetadas via javascript ao retornar para o front-end'
     */
    @PostMapping(value = "/{id}/favoritar")
    public @ResponseBody String favoritar(@PathVariable("id") Projeto projeto){
        projeto.setFavoritado(!projeto.getFavoritado());

        projetoService.salvar(projeto);

        if(projeto.getFavoritado()) {
            return "fas fa-star gold";
        } else {
            return "far fa-star";
        }
    }

    /**
     * Método para lidar com os filtros de pesquisa do Projeto
     * Pode receber via parametro da requisição um status do projeto conforme enumerador ProjetoStatus.
     * Recebe adicionalmente um objeto Pageable para lidar com a paginação e quantidade de resultados.
     * Acessa o projetoService seguindo a lógica:
     *
     * - Se foi passado um status, buscar pelo status informado.
     * - Se nenhum status foi informado, buscar todos os projetos
     *
     * @param projetoStatus - Status conforme enumerador ProjetoStatus, não é obrigatório informar.
     * @param pageable - Objeto padrão para lidar com paginação e quantidade de resultados.
     * @return Retorna objeto para processamento da view, enviando somente o fragmento 'projeto-tabelas' do template ListarProjetos.
     */
    @GetMapping("/filtrar")
    public ModelAndView filtrarProjetos(@RequestParam(value = "status", required = false) ProjetoStatus projetoStatus, @PageableDefault(size = 10) Pageable pageable) {
        ModelAndView mv = new ModelAndView("views/ListarProjetos :: projetos-tabela");

        if(projetoStatus == null) {
            mv.addObject("projetos", projetoService.buscarTodos(pageable));
        } else {
            mv.addObject("projetos", projetoService.buscaPorStatus(projetoStatus, pageable));
        }
        mv.addObject("statusFiltado", projetoStatus);
        return mv;
    }
}
