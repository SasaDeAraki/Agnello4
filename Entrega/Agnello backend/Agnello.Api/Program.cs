using Agnello.Persistence.Data;
using Agnello.Persistence.Domain;
using Agnello.Persistence.Repositories;
using Agnello.Persistence.Services;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddSingleton<SqliteConnectionFactory>();
builder.Services.AddSingleton<IProdutoRepository, SqliteProdutoRepository>();
builder.Services.AddSingleton<EstoqueService>();
builder.Services.AddCors(options =>
{
    options.AddDefaultPolicy(policy =>
    {
        policy.AllowAnyOrigin()
            .AllowAnyHeader()
            .AllowAnyMethod();
    });
});

var app = builder.Build();

app.UseCors();

using (var scope = app.Services.CreateScope())
{
    var factory = scope.ServiceProvider.GetRequiredService<SqliteConnectionFactory>();
    EstoqueDbInitializer.EnsureCreated(factory);
    SeedIfEmpty(scope.ServiceProvider.GetRequiredService<EstoqueService>());
}

app.MapGet("/", () => Results.Ok(new
{
    service = "Agnello API",
    status = "ok"
}));

app.MapGet("/api/produtos", (EstoqueService service) =>
{
    var produtos = service.Listar().Select(ToResponse);
    return Results.Ok(produtos);
});

app.MapGet("/api/produtos/{id:int}", (int id, EstoqueService service) =>
{
    var produto = service.Listar().FirstOrDefault(p => p.Id == id);
    return produto is null ? Results.NotFound() : Results.Ok(ToResponse(produto));
});

app.MapPost("/api/produtos", (ProdutoRequest request, EstoqueService service) =>
{
    try
    {
        var produto = ToDomain(request);
        var id = service.Cadastrar(produto);
        var created = service.Listar().First(p => p.Id == id);
        return Results.Created($"/api/produtos/{id}", ToResponse(created));
    }
    catch (ArgumentException ex)
    {
        return Results.BadRequest(new { error = ex.Message });
    }
});

app.MapPut("/api/produtos/{id:int}", (int id, ProdutoRequest request, EstoqueService service) =>
{
    try
    {
        var produto = ToDomain(request, id);
        return service.Atualizar(produto)
            ? Results.Ok(ToResponse(produto))
            : Results.NotFound();
    }
    catch (ArgumentException ex)
    {
        return Results.BadRequest(new { error = ex.Message });
    }
});

app.MapDelete("/api/produtos/{id:int}", (int id, EstoqueService service) =>
{
    return service.Excluir(id)
        ? Results.NoContent()
        : Results.NotFound();
});

app.Run("http://0.0.0.0:5000");

static void SeedIfEmpty(EstoqueService service)
{
    if (service.Listar().Any())
    {
        return;
    }

    service.Cadastrar(new Produto
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

    service.Cadastrar(new Produto
    {
        Nome = "Chardonnay Selecao",
        Tipo = "Branco",
        Regiao = "Vale dos Vinhedos",
        Uva = "Chardonnay",
        TeorAlcoolico = 12.8m,
        Intensidade = 2,
        Encorpado = 2,
        Acucar = 2,
        Acidez = 4,
        Valor = 74.50m,
        QuantidadeEstoque = 18,
        AvaliacaoCritica = 4.5m
    });
}

static ProdutoResponse ToResponse(Produto produto) => new(
    produto.Id,
    produto.Nome,
    produto.Tipo,
    produto.Regiao,
    produto.Uva,
    produto.TeorAlcoolico,
    produto.Intensidade,
    produto.Encorpado,
    produto.Acucar,
    produto.Acidez,
    produto.Valor,
    produto.QuantidadeEstoque,
    produto.AvaliacaoCritica);

static Produto ToDomain(ProdutoRequest request, int id = 0) => new()
{
    Id = id,
    Nome = request.Nome,
    Tipo = request.Tipo,
    Regiao = request.Regiao,
    Uva = request.Uva,
    TeorAlcoolico = request.TeorAlcoolico,
    Intensidade = request.Intensidade,
    Encorpado = request.Encorpado,
    Acucar = request.Acucar,
    Acidez = request.Acidez,
    Valor = request.Valor,
    QuantidadeEstoque = request.QuantidadeEstoque,
    AvaliacaoCritica = request.AvaliacaoCritica
};

public sealed record ProdutoRequest(
    string Nome,
    string? Tipo,
    string? Regiao,
    string? Uva,
    decimal? TeorAlcoolico,
    int? Intensidade,
    int? Encorpado,
    int? Acucar,
    int? Acidez,
    decimal Valor,
    int QuantidadeEstoque,
    decimal? AvaliacaoCritica);

public sealed record ProdutoResponse(
    int Id,
    string Nome,
    string? Tipo,
    string? Regiao,
    string? Uva,
    decimal? TeorAlcoolico,
    int? Intensidade,
    int? Encorpado,
    int? Acucar,
    int? Acidez,
    decimal Valor,
    int QuantidadeEstoque,
    decimal? AvaliacaoCritica);
