# Sistema de Feedback Compatilhado

### Configurando conexão com o MongoDB Atlas

Adicione `QUARKUS_MONGODB_CONNECTION_STRING` nas variáveis de ambiente com a _string_ de conexão, ex: "_mongodb+srv://..._".

### Regra de negócio

1. o colaborador se cadastra e ganha X saldo na carteira;
2. ele pode usar esse saldo para distribuir entre outros colaboradores, debitando da carteira e creditando na carteira do colega;
3. junto da pontuação ele tem que preencher um comentário que explica o pq daquele reconhecimento;
4. se a carteira zerar ele não pode bonificar ninguém. Após o ciclo de feedback todas as carteira são resetadas e o saldo volta pra X valor;
5. o colaborador fica com o valor histórico da bonificação e o chefe pode usar como quiser pra justificar alguma coisa ou bonificar o funcionário de outra forma;
6. não pode dar ponto pra si mesmo e não tem como ter dois usuários com CPF igual.