package br.com.tama.projekt.controllers;

import br.com.tama.projekt.services.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *  Classe Controller da página inicial do sistema.
 *  Contém os métodos tratar requisições ao Dashboard
 *
 * @author Wilson Ferreira
 */
@Controller
@RequestMapping("/")
public class DashboardController {

    /**
     * Injetando camada de serviço do 'Projeto' para ter acesso aos metódos de consulta da entidade
     */
    @Autowired
    private ProjetoService projetoService;

    /**
     * Método para processar a requisição à pagina do Dashboard.
     * Acessa o ProjetoService para recuperar os projetos favoritos e as próximas entregas.
     * @return objeto modelview parametrizado para a view do Dashboard e contendo a lista de favoritos e entregas.
     */
    @GetMapping("")
    public ModelAndView dashboard() {
        ModelAndView mv = new ModelAndView("views/Dashboard");
        mv.addObject("projetosFavoritos", projetoService.buscarFavoritos());
        mv.addObject("proximasEntregas", projetoService.buscarProximasEntregas());
        return mv;
    }
}
