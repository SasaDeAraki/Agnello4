using Agnello.Persistence.Data;
using Agnello.Persistence.Domain;
using Agnello.Persistence.Repositories;
using Agnello.Persistence.Services;

var factory = new SqliteConnectionFactory(Path.Combine(Path.GetTempPath(), $"agnello-tests-{Guid.NewGuid():N}.db"));
EstoqueDbInitializer.EnsureCreated(factory);

var repository = new SqliteProdutoRepository(factory);
var service = new EstoqueService(repository);

Console.WriteLine("Executando verificacoes do modulo C#...");

var produto = new Produto
{
    Nome = "Teste Reserva",
    Tipo = "Tinto",
    Regiao = "Campanha",
    Uva = "Merlot",
    Valor = 59.90m,
    QuantidadeEstoque = 10,
    AvaliacaoCritica = 4.5m
};

Console.WriteLine("1) Cadastrando produto...");
var id = service.Cadastrar(produto);
Require(id > 0, "Cadastro deve retornar um id positivo.");
Console.WriteLine($"   OK - id gerado: {id}");

Console.WriteLine("2) Conferindo listagem...");
var listagem = service.Listar();
Require(listagem.Count == 1, "Listagem deve conter um item.");
Require(listagem[0].Nome == "Teste Reserva", "Nome persistido incorretamente.");
Console.WriteLine($"   OK - {listagem.Count} item(s) encontrado(s)");

Console.WriteLine("3) Atualizando estoque...");
produto.Id = id;
produto.QuantidadeEstoque = 7;
Require(service.Atualizar(produto), "Atualizacao deve retornar true.");

var atualizado = service.Listar().Single();
Require(atualizado.QuantidadeEstoque == 7, "Quantidade em estoque nao foi atualizada.");
Console.WriteLine($"   OK - estoque atualizado para {atualizado.QuantidadeEstoque}");

Console.WriteLine("4) Excluindo produto...");
Require(service.Excluir(id), "Exclusao deve retornar true.");
Require(service.Listar().Count == 0, "Banco deve ficar vazio apos exclusao.");
Console.WriteLine("   OK - banco vazio");

Console.WriteLine("5) Validando regra de negocio...");
RequireThrows<ArgumentException>(() => service.Cadastrar(new Produto
{
    Nome = "",
    Valor = 10m,
    QuantidadeEstoque = 1
}), "Cadastro com nome vazio deve falhar.");
Console.WriteLine("   OK - validacao de nome obrigatorio");

Console.WriteLine("Todas as verificacoes passaram.");
return;

static void Require(bool condition, string message)
{
    if (!condition)
    {
        throw new InvalidOperationException(message);
    }
}

static void RequireThrows<TException>(Action action, string message)
    where TException : Exception
{
    try
    {
        action();
    }
    catch (TException)
    {
        return;
    }

    throw new InvalidOperationException(message);
}
