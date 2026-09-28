#!/usr/bin/env bash
# Cria labels, milestones e issues do MVP de Gestão de Gastos.
# Pré-requisitos: GitHub CLI (gh) instalado e autenticado (gh auth login).
# Uso: dentro da pasta do repositório clonado, rode:  bash criar-issues.sh
set -e

echo ">> Criando labels..."
gh label create setup    --color 6f42c1 --description "Configuração inicial" --force
gh label create backend  --color 0e8a16 --description "Java / Spring Boot"   --force
gh label create frontend --color 1d76db --description "HTML / CSS / JS"      --force
gh label create teste    --color fbca04 --description "Testes e validação"   --force
gh label create docs     --color 5319e7 --description "Documentação"         --force

echo ">> Criando milestones..."
for m in "Etapa 1 - Base do projeto" "Etapa 2 - API de gastos e categorias" \
         "Etapa 3 - Resumo mensal" "Etapa 4 - Tela: lançamento e listagem" \
         "Etapa 5 - Tela: filtro e resumo" "Etapa 6 - Validações e testes"; do
  gh api repos/{owner}/{repo}/milestones -f title="$m" >/dev/null || true
done

issue() { # título | labels | milestone | corpo
  gh issue create --title "$1" --label "$2" --milestone "$3" --body "$4"
}

E1="Etapa 1 - Base do projeto"
E2="Etapa 2 - API de gastos e categorias"
E3="Etapa 3 - Resumo mensal"
E4="Etapa 4 - Tela: lançamento e listagem"
E5="Etapa 5 - Tela: filtro e resumo"
E6="Etapa 6 - Validações e testes"

# ---------------- ETAPA 1 ----------------
issue "Criar projeto Spring Boot com Maven" "setup" "$E1" "$(cat <<'EOF'
## Descrição
Criar o esqueleto do projeto com Java 21, Spring Boot 3.x e Maven.

## Tarefas
- [ ] Gerar projeto (Spring Initializr) com Web, Data JPA e Validation
- [ ] Definir pacote base `com.exemplo.gastos`
- [ ] Criar pastas `model`, `repository`, `service`, `controller`, `dto`
- [ ] Confirmar que `mvn spring-boot:run` sobe a aplicação em `localhost:8080`

## Critério de aceite
Aplicação inicia sem erros.
EOF
)"

issue "Configurar banco de dados em arquivo (SQLite ou H2)" "setup,backend" "$E1" "$(cat <<'EOF'
## Descrição
Configurar persistência em arquivo local para os dados sobreviverem ao reinício (RNF02, RNF08).

## Tarefas
- [ ] Adicionar dependência do banco escolhido
- [ ] Configurar `application.properties` (URL do arquivo, dialect, ddl-auto)
- [ ] Configurar `server.address=127.0.0.1` (RNF07)

## Critério de aceite
Arquivo do banco é criado e mantido após reiniciar a aplicação.
EOF
)"

issue "Criar entidades Categoria e Gasto e seus repositories" "backend" "$E1" "$(cat <<'EOF'
## Descrição
Implementar o modelo de dados da seção 6 do DRS.

## Tarefas
- [ ] `Categoria`: id, nome (único, até 30 caracteres)
- [ ] `Gasto`: id, data, descricao (até 100), valor (`BigDecimal` 12,2), categoria (ManyToOne)
- [ ] `CategoriaRepository` e `GastoRepository`
- [ ] Usar `BigDecimal`, nunca `double` (RNF05)

## Critério de aceite
Tabelas criadas corretamente com as restrições do DRS.
EOF
)"

issue "Carregar categorias iniciais" "backend" "$E1" "$(cat <<'EOF'
## Descrição
Pré-cadastrar as categorias (RF05): Mercado, Transporte, Moradia, Lazer, Saúde e Outros.

## Tarefas
- [ ] Criar `data.sql` ou `CommandLineRunner` que insere as categorias
- [ ] Não duplicar categorias ao reiniciar a aplicação

## Critério de aceite
Após o primeiro start existem 6 categorias; reiniciar não cria duplicatas.
EOF
)"

# ---------------- ETAPA 2 ----------------
issue "API de categorias (listar e criar)" "backend" "$E2" "$(cat <<'EOF'
## Descrição
Implementar `GET /api/categorias` e `POST /api/categorias` (RF06, RF07).

## Tarefas
- [ ] `CategoriaController` e `CategoriaService`
- [ ] Validar nome obrigatório, até 30 caracteres
- [ ] Rejeitar nome duplicado sem diferenciar maiúsculas/minúsculas (RN05)

## Critério de aceite (CA06, CA07)
Criar "Educação" funciona; criar de novo retorna 400 com mensagem clara.
EOF
)"

issue "API de gastos: criar (POST /api/gastos)" "backend" "$E2" "$(cat <<'EOF'
## Descrição
Cadastrar um gasto (RF01).

## Tarefas
- [ ] `GastoDTO` com validações: valor > 0 e no máximo 2 casas (RN01), descrição obrigatória até 100 (RN02), data obrigatória (RN03), categoriaId obrigatório (RN04)
- [ ] `GastoController` e `GastoService`
- [ ] Retornar 201 com o gasto criado

## Critério de aceite (CA01, CA02)
Gasto válido é salvo; valor 0 ou vazio retorna 400.
EOF
)"

issue "API de gastos: listar por mês (GET /api/gastos?mes=YYYY-MM)" "backend" "$E2" "$(cat <<'EOF'
## Descrição
Listar gastos de um mês, ordenados por data decrescente (RF04, RF08).

## Tarefas
- [ ] Query por intervalo de datas do mês
- [ ] Ordenação por data desc (desempate por id desc)
- [ ] Mês inexistente na base retorna lista vazia (não erro)

## Critério de aceite (CA05, CA09)
Retorna apenas gastos do mês pedido.
EOF
)"

issue "API de gastos: editar e excluir (PUT e DELETE)" "backend" "$E2" "$(cat <<'EOF'
## Descrição
Implementar `PUT /api/gastos/{id}` e `DELETE /api/gastos/{id}` (RF02, RF03).

## Tarefas
- [ ] Reaproveitar as validações do POST no PUT
- [ ] Retornar 404 se o id não existir
- [ ] DELETE retorna 204

## Critério de aceite (CA03, CA04)
Editar e excluir refletem na listagem.
EOF
)"

issue "Tratamento global de erros da API" "backend" "$E2" "$(cat <<'EOF'
## Descrição
Padronizar respostas de erro com `@RestControllerAdvice`.

## Tarefas
- [ ] 400 com mensagem por campo (validação)
- [ ] 404 para recurso inexistente
- [ ] 400 para conflito de nome de categoria

## Critério de aceite
O frontend consegue exibir a mensagem retornada em qualquer erro.
EOF
)"

# ---------------- ETAPA 3 ----------------
issue "Endpoint de resumo mensal (GET /api/resumo?mes=YYYY-MM)" "backend" "$E3" "$(cat <<'EOF'
## Descrição
Retornar total do mês e total por categoria com percentual (RF09, RF10).

## Tarefas
- [ ] `ResumoService` com query agrupada por categoria
- [ ] Percentual = total da categoria ÷ total do mês × 100, 1 casa decimal (RN07)
- [ ] Total zero retorna 0% sem divisão por zero
- [ ] Ordenar categorias por valor decrescente
- [ ] Formato de resposta conforme seção 7 do DRS

## Critério de aceite (CA09)
Totais batem com a soma dos gastos; mês sem gastos retorna R$ 0,00.
EOF
)"

# ---------------- ETAPA 4 ----------------
issue "Página base e layout (index.html, style.css, app.js)" "frontend" "$E4" "$(cat <<'EOF'
## Descrição
Criar a tela única em `src/main/resources/static` com as quatro áreas: cabeçalho, formulário, resumo e tabela (seção 8 do DRS).

## Tarefas
- [ ] Estrutura HTML e CSS responsivo simples
- [ ] Função utilitária de chamadas `fetch` à API
- [ ] Função de formatação de moeda em R$ (RN08)

## Critério de aceite
Abrir `localhost:8080` mostra a tela com os blocos vazios.
EOF
)"

issue "Formulário de lançamento de gasto" "frontend" "$E4" "$(cat <<'EOF'
## Descrição
Formulário para cadastrar gasto na tela principal (RF01, RNF01).

## Tarefas
- [ ] Campos: data (padrão hoje), descrição, valor, categoria (select carregado da API)
- [ ] Foco automático na descrição após salvar
- [ ] Exibir mensagens de erro vindas da API
- [ ] Limpar formulário após sucesso

## Critério de aceite
Lançar um gasto leva menos de 10 segundos.
EOF
)"

issue "Listagem de gastos com editar e excluir" "frontend" "$E4" "$(cat <<'EOF'
## Descrição
Tabela com data, descrição, categoria, valor e ações (RF02, RF03, RF04, RF11).

## Tarefas
- [ ] Renderizar gastos do mês
- [ ] Botão Editar preenche o formulário e salva via PUT
- [ ] Botão Excluir pede confirmação e chama DELETE
- [ ] Atualizar lista e resumo sem recarregar a página

## Critério de aceite (CA03, CA04)
Alterações aparecem imediatamente na tela.
EOF
)"

# ---------------- ETAPA 5 ----------------
issue "Seletor de mês/ano" "frontend" "$E5" "$(cat <<'EOF'
## Descrição
Filtro de mês na tela (RF08).

## Tarefas
- [ ] `input type=month` com valor padrão = mês atual
- [ ] Ao trocar, recarregar lista e resumo

## Critério de aceite (CA05)
Trocar o mês mostra apenas os dados do mês escolhido.
EOF
)"

issue "Cartão de resumo mensal na tela" "frontend" "$E5" "$(cat <<'EOF'
## Descrição
Exibir total do mês e tabela por categoria com valor e percentual (RF09, RF10).

## Tarefas
- [ ] Mostrar total em destaque
- [ ] Tabela: categoria, valor (R$), percentual
- [ ] Estado vazio: "Nenhum gasto neste mês"

## Critério de aceite (CA09)
Mês sem gastos mostra R$ 0,00 sem erro.
EOF
)"

issue "Criar nova categoria pela tela" "frontend" "$E5" "$(cat <<'EOF'
## Descrição
Botão "+ nova categoria" ao lado do select (RF06).

## Tarefas
- [ ] Campo/prompt para o nome
- [ ] Chamar `POST /api/categorias` e recarregar o select já selecionando a nova
- [ ] Exibir erro em caso de nome duplicado

## Critério de aceite (CA06, CA07)
Nova categoria fica disponível e selecionada no formulário.
EOF
)"

# ---------------- ETAPA 6 ----------------
issue "Testes automatizados de service e API" "teste,backend" "$E6" "$(cat <<'EOF'
## Descrição
Cobrir regras de negócio principais.

## Tarefas
- [ ] Testes de validação do gasto (RN01 a RN04)
- [ ] Teste de categoria duplicada (RN05)
- [ ] Teste do cálculo de resumo e percentuais (RN07), incluindo total zero
- [ ] Testes de integração dos endpoints (`@SpringBootTest` / MockMvc)

## Critério de aceite
`mvn test` passa com todos os testes verdes.
EOF
)"

issue "Validação manual dos critérios de aceite (CA01 a CA09)" "teste" "$E6" "$(cat <<'EOF'
## Descrição
Executar cada cenário do DRS manualmente na aplicação rodando.

## Checklist
- [ ] CA01 Cadastrar gasto válido
- [ ] CA02 Valor 0 ou vazio é recusado
- [ ] CA03 Editar valor
- [ ] CA04 Excluir com confirmação
- [ ] CA05 Trocar de mês
- [ ] CA06 Criar categoria
- [ ] CA07 Categoria duplicada recusada
- [ ] CA08 Dados persistem após reiniciar
- [ ] CA09 Mês sem gastos sem erro
EOF
)"

issue "README com instruções de uso e backup" "docs" "$E6" "$(cat <<'EOF'
## Descrição
Documentar como rodar o projeto.

## Tarefas
- [ ] Pré-requisitos (Java 21, Maven)
- [ ] Como executar e acessar `localhost:8080`
- [ ] Onde fica o arquivo do banco e como fazer backup (RNF08)
- [ ] Lista de funcionalidades do MVP e do que ficou para a versão 2
EOF
)"

echo ">> Pronto! 19 issues criadas."
