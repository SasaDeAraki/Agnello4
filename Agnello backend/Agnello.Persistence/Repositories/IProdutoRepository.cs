using Agnello.Persistence.Domain;

namespace Agnello.Persistence.Repositories;

public interface IProdutoRepository
{
    int Inserir(Produto produto);
    bool Atualizar(Produto produto);
    bool Excluir(int id);
    Produto? ObterPorId(int id);
    IReadOnlyList<Produto> ListarTodos();
    IReadOnlyList<Produto> BuscarPorNome(string termo);
}
