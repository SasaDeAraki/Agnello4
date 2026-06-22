# Vinheria Agnello - C# Persistence Module

Essa pasta contem um módulo de persistência C# independente para o domínio de estoque usado pela Vinheria Agnello.

## Objetivo
-Persiste itens de estoque com operações CRUD.
-Mantenha o modelo alinhado com o domínio do projeto.
-Forneça uma divisão limpa entre repositório e serviço para futura integração.

## Estrutura
- `Domain/Produto.cs`: domain model.
- `Data/SqliteConnectionFactory.cs`: database connection helper.
- `Data/EstoqueDbInitializer.cs`: database bootstrapper.
- `Repositories/IProdutoRepository.cs`: contrato de persistência.
- `Repositories/SqliteProdutoRepository.cs`: implementação SQLite.
- `Services/EstoqueService.cs`: camada de validação e orquestramento.
- `Program.cs`: pequeno entrypoint no console para verificação manual.

## Notas
- O modulo usa SQLite para manter o exemplo auto-contido.
- O schema espelha os mesmos campos de estoque usados pela camada Android Room.

## Testes
- Use a IDE do Visual Studio para rodar `Agnello.Persistence.Tests` e verificar a cobertura de testes unitários.

ou caso prefira verificar pelo console, abra a pasta root do projeto e rode o comando:

```bash
dotnet build .\Agnello.Persistence\Agnello.Persistence.csproj
dotnet run --project .\Agnello.Persistence.Tests\Agnello.Persistence.Tests.csproj
dotnet run --project .\Agnello.Persistence\Agnello.Persistence.csproj
```