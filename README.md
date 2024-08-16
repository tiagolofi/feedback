# Sistema de Feedback Compatilhado

Esta aplicação oferece a infraestrutura de _backend_ para um sistema de feedback compartilhado entre gestores e colaboradores

### Variáveis de Ambiente

- `QUARKUS_MONGODB_CONNECTION_STRING`: "_mongodb+srv://..._";
- `TOKEN_AUTORIZACAO`: um identificador que barra o consumo por uma aplicação desconhecida, pode atribuir qualquer valor;
- `PUBLIC_KEY`: chave pública para ler o token JWT;
- `PRIVATE_KEY`: chave privada para criptografar o token.

OBS: as chaves podem ser geradas com `br.gov.ma.feedback.seguranca.GerarChaves`;

### Regra de negócio

1. Jornada do Colaborador
- Cadastro e Saldo Inicial: O colaborador se cadastra na aplicação e recebe um saldo inicial de X pontos em sua carteira virtual.
- Distribuição de Pontos: O colaborador pode usar esse saldo para distribuir pontos entre outros colaboradores, debitando de sua própria carteira e creditando na carteira do colega.
- Comentário Obrigatório: Ao distribuir pontos, o colaborador deve preencher um comentário explicando o motivo do reconhecimento.
2. Detalhes da Jornada
- Saldo Zerado: Se o saldo da carteira do colaborador zerar, ele não poderá bonificar ninguém até o próximo ciclo de feedback.
- Reset de Saldo: Após cada ciclo de feedback, todas as carteiras são resetadas e o saldo volta para o valor inicial X.
- Histórico de Bonificação: O colaborador mantém um histórico das bonificações recebidas, que pode ser utilizado pelo gestor para justificar promoções ou outras recompensas.
- Proibição de Auto-Bonificação: O colaborador não pode dar pontos para si mesmo.
- Moderação de Comentários: Comentários agressivos, discriminatórios ou fora de contexto serão analisados, moderados ou removidos por um usuário com a regra de acesso “moderator”.
3. Regras de Acesso
- Admin: O administrador pode executar todas as ações, exceto moderar comentários e resetar as carteiras.
- Recuperação de Senha: Caso um usuário esqueça a senha, ele deve solicitar uma nova no sistema e informar ao administrador que ele deve executar o bloqueio da conta,  permitindo que o funcionário cadastre uma nova senha.
- Expiração de Sessão: Tokens de sessão expiram em 30 minutos.
4. O RESET
- Reset de Saldo: O reset do saldo das carteiras deve ser realizado por um usuário impessoal com a regra de acesso “system”.
5. Outras Funcionalidades
- Cadastro de Perfil: Os colaboradores podem cadastrar informações pessoais em seus perfis.
- Envio de Foto de Perfil: Os colaboradores podem enviar uma foto para seus perfis.ações pessoais

### Default Values

| Tabela | Campo | Valores Possíveis |
|:------:|:-----:|:-----------------:|
| feedbacks | tipoReconhecimento | "Colabore Comigo", "Priorizamos Pessoas", "Aprendizado e Melhoria Contínua", "Inovação", "Tecnologia com Propósito", "Gestão Orientada por Resultados" |
| feedbacks | pontuacao | 100 (valeu!), 200 (mandou bem!) e 300 (UAU!) |
