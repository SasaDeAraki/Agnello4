using Agnello.Persistence.Data;
using Agnello.Persistence.Domain;
using Agnello.Persistence.Repositories;
using Agnello.Persistence.Services;

var connectionFactory = new SqliteConnectionFactory();
EstoqueDbInitializer.EnsureCreated(connectionFactory);

IProdutoRepository repository = new SqliteProdutoRepository(connectionFactory);
var service = new EstoqueService(repository);

Console.WriteLine("Vinheria Agnello - modulo C# de persistencia");
Console.WriteLine("Banco SQLite pronto.");

var produtosAntes = service.Listar();
if (!produtosAntes.Any())
{
    Console.WriteLine("Nenhum produto encontrado. Inserindo registro de demonstracao...");
    var idNovo = service.Cadastrar(new Produto
    {
        Nome = "Cabernet Reserva",
        Tipo = "Tinto",
        Regiao = "Serra Gaucha",
        Uva = "Cabernet Sauvignon",
        TeorAlcoolico = 13.5m,
        Intensidade = 4,
        Encorpado = 4,
        Acucar = 1,
        Acidez = 3,
        Valor = 89.90m,
        QuantidadeEstoque = 12,
        AvaliacaoCritica = 4.7m
    });
    Console.WriteLine($"Produto inserido com id {idNovo}.");
}
else
{
    Console.WriteLine($"Foram encontrados {produtosAntes.Count} produto(s) no banco.");
}

var produtos = service.Listar();
Console.WriteLine("Listagem atual:");
foreach (var produto in produtos)
{
    Console.WriteLine($"- #{produto.Id} {produto.Nome} | estoque: {produto.QuantidadeEstoque} | valor: {produto.Valor}");
}

Console.WriteLine($"Total de produtos: {produtos.Count}");
