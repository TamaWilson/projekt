
# Projekt
Teste prático para Grupo Mateus.

**Objetivo:** Criação de uma aplicação para gerenciamento de projetos e tarefas.

# Tecnologia Empregada
- Spring Framework
- Thymeleaf
- Spring Data
- H2 Database
- Flyway
- JQuery
- Boostrap

# Execução
Clonar este repositório e executar o projeto conforme instruções abaixo:

**Opção 1:**
 * Entrar na raiz do projeto via terminal ou cmd e executar o comando abaixo:
   * No Linux: `./mvnw spring-boot:run`
   * No Windows: `mvnw.cmd spring-boot:run`

**Opção 2:**
 * Entrar na pastar *Release* via terminal ou cmd e executar o comando `java -Dfile.encoding=UTF-8 -jar projekt-0.0.1-SNAPSHOT.jar`
 
Aguardar o Maven baixar toda as depedências.
Após iniciado a aplicação pode ser acessada no pela url `http://localhost:8080`

# Base de dados
O banco de dados da aplicação é resetado a cada execução e populados com dados pré-configurados contendo:

- 26 Projetos, dentre eles 2 Favoritados e 1 Atradasado.
- 5 Tarefas atreladas ao Projeto 1
