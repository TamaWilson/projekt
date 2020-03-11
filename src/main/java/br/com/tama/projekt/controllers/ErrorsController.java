package br.com.tama.projekt.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 *  Classe controller das página de erro do sistema.
 *  Contém os métodos para lidar com erros do tipo 404 (Página não encontrada) e 500 (Erro interno do servidor)
 *  Implementa a interfaace ErrorController do Spring para possibilitar a captura dos erros do sistema
 *
 * @author Wilson Ferreira
 */
@Controller
public class ErrorsController implements ErrorController {

    /** Método para processar requisções feita para a página erro.
     *  Ao interceptar os erros 404 ou 500 na requisição HTTP, redireciona o usuário para uma view
     *  com a mensagem de erro correspondente
     *
     * @param request Objeto de requisição http.
     * @return String contendo nome da view a ser processada de acordo com o código de erro.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/500";
            }
        }
        return "error";
    }

    /** Método padrão da intergface Error Controler
     * retorna o caminho para a página de erro.
     *
     * @return String contendo o caminho da página de erro.
     */
    @Override
        public String getErrorPath() {
            return "/error";
        }

}
