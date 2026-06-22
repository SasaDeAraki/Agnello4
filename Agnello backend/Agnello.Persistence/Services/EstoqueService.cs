using Agnello.Persistence.Domain;
using Agnello.Persistence.Repositories;

namespace Agnello.Persistence.Services;

public sealed class EstoqueService
{
    private readonly IProdutoRepository _repository;

    public EstoqueService(IProdutoRepository repository)
    {
        _repository = repository;
    }

    public int Cadastrar(Produto produto)
    {
        Validar(produto);
        return _repository.Inserir(produto);
    }

    public bool Atualizar(Produto produto)
    {
        Validar(produto);
        if (produto.Id <= 0)
        {
            throw new ArgumentException("Produto id must be greater than zero.", nameof(produto));
        }

        return _repository.Atualizar(produto);
    }

    public bool Excluir(int id)
    {
        if (id <= 0)
        {
            throw new ArgumentException("Produto id must be greater than zero.", nameof(id));
        }

        return _repository.Excluir(id);
    }

    public IReadOnlyList<Produto> Listar()
        => _repository.ListarTodos();

    public IReadOnlyList<Produto> Buscar(string termo)
        => _repository.BuscarPorNome(termo);

    public bool AjustarEstoque(int id, int novoSaldo)
    {
        var produto = _repository.ObterPorId(id)
            ?? throw new InvalidOperationException("Produto not found.");

        produto.QuantidadeEstoque = novoSaldo;
        return _repository.Atualizar(produto);
    }

    private static void Validar(Produto produto)
    {
        if (produto is null)
        {
            throw new ArgumentNullException(nameof(produto));
        }

        if (string.IsNullOrWhiteSpace(produto.Nome))
        {
            throw new ArgumentException("Produto nome is required.", nameof(produto));
        }

        if (produto.Valor < 0)
        {
            throw new ArgumentException("Produto valor cannot be negative.", nameof(produto));
        }

        if (produto.QuantidadeEstoque < 0)
        {
            throw new ArgumentException("Quantidade estoque cannot be negative.", nameof(produto));
        }
    }
}
