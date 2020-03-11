package br.com.tama.projekt.exceptions;

/**
 * Exceção personalizada para lidar com erros no cadastro de entidades
 *
 * @author Wilson Ferreira
 */
public class CadastroException extends RuntimeException {

    /** Construtor da classe, invoca apenas o construtor da classe pai enviando uma mensagem de erro
     *
     * @param message - Mensagem de erro da exceção.
     */
    public CadastroException(String message) {
        super(message);
    }

}
