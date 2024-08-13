# Sistema de Feedback Compatilhado

### Variáveis de Ambiente

- `QUARKUS_MONGODB_CONNECTION_STRING`: "_mongodb+srv://..._";
- `TOKEN_AUTORIZACAO`: um identificador que barra o consumo por uma aplicação desconhecida, pode atribuir qualquer valor;
- `PUBLIC_KEY`: chave pública para ler o token JWT;
- `PRIVATE_KEY`: chave privada para criptografar o token.

### Regra de negócio

1. o colaborador se cadastra e ganha X saldo na carteira;
2. ele pode usar esse saldo para distribuir entre outros colaboradores, debitando da carteira e creditando na carteira do colega;
3. junto da pontuação ele tem que preencher um comentário que explica o pq daquele reconhecimento;
4. se a carteira zerar ele não pode bonificar ninguém. Após o ciclo de feedback todas as carteira são resetadas e o saldo volta pra X valor;
5. o colaborador fica com o valor histórico da bonificação e o chefe pode usar como quiser pra justificar alguma coisa ou bonificar o funcionário de outra forma;
6. não pode dar ponto pra si mesmo.

### Default Values

| Tabela | Campo | Valores Possíveis |
|:------:|:-----:|:-----------------:|
| feedbacks | tipoReconhecimento | "Priorizamos Pessoas", "Aprendizado e Melhoria Contínua", "Inovação", "Tecnologia com Propósito", "Gestão Orientada por Resultados" |
| feedbacks | pontuacao | 100 (valeu!), 200 (mandou bem!) e 300 (UAU!) |
