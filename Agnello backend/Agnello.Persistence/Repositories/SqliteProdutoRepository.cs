using System.Globalization;
using Agnello.Persistence.Data;
using Agnello.Persistence.Domain;
using Microsoft.Data.Sqlite;

namespace Agnello.Persistence.Repositories;

public sealed class SqliteProdutoRepository : IProdutoRepository
{
    private readonly SqliteConnectionFactory _factory;

    public SqliteProdutoRepository(SqliteConnectionFactory factory)
    {
        _factory = factory;
    }

    public int Inserir(Produto produto)
    {
        using var connection = _factory.CreateConnection();
        connection.Open();

        using var command = connection.CreateCommand();
        command.CommandText = """
            INSERT INTO produto
            (nome, tipo, regiao, uva, teor_alcoolico, intensidade, encorpado, acucar, acidez, valor, quantidade_estoque, avaliacao_critica)
            VALUES
            ($nome, $tipo, $regiao, $uva, $teor, $intensidade, $encorpado, $acucar, $acidez, $valor, $estoque, $avaliacao);
            """;
        AddParameters(command, produto);
        command.ExecuteNonQuery();
        using var idCommand = connection.CreateCommand();
        idCommand.CommandText = "SELECT last_insert_rowid();";
        var id = (long)idCommand.ExecuteScalar()!;
        produto.Id = (int)id;
        return produto.Id;
    }

    public bool Atualizar(Produto produto)
    {
        using var connection = _factory.CreateConnection();
        connection.Open();

        using var command = connection.CreateCommand();
        command.CommandText = """
            UPDATE produto SET
                nome = $nome,
                tipo = $tipo,
                regiao = $regiao,
                uva = $uva,
                teor_alcoolico = $teor,
                intensidade = $intensidade,
                encorpado = $encorpado,
                acucar = $acucar,
                acidez = $acidez,
                valor = $valor,
                quantidade_estoque = $estoque,
                avaliacao_critica = $avaliacao
            WHERE id = $id;
            """;
        AddParameters(command, produto);
        command.Parameters.AddWithValue("$id", produto.Id);
        return command.ExecuteNonQuery() == 1;
    }

    public bool Excluir(int id)
    {
        using var connection = _factory.CreateConnection();
        connection.Open();

        using var command = connection.CreateCommand();
        command.CommandText = "DELETE FROM produto WHERE id = $id";
        command.Parameters.AddWithValue("$id", id);
        return command.ExecuteNonQuery() == 1;
    }

    public Produto? ObterPorId(int id)
    {
        using var connection = _factory.CreateConnection();
        connection.Open();

        using var command = connection.CreateCommand();
        command.CommandText = "SELECT * FROM produto WHERE id = $id LIMIT 1";
        command.Parameters.AddWithValue("$id", id);
        using var reader = command.ExecuteReader();
        return reader.Read() ? Map(reader) : null;
    }

    public IReadOnlyList<Produto> ListarTodos()
        => BuscarInterno("SELECT * FROM produto ORDER BY nome");

    public IReadOnlyList<Produto> BuscarPorNome(string termo)
    {
        using var connection = _factory.CreateConnection();
        connection.Open();

        using var command = connection.CreateCommand();
        command.CommandText = "SELECT * FROM produto WHERE LOWER(nome) LIKE LOWER($termo) ORDER BY nome";
        command.Parameters.AddWithValue("$termo", "%" + (termo ?? string.Empty).Trim() + "%");
        using var reader = command.ExecuteReader();
        var produtos = new List<Produto>();
        while (reader.Read())
        {
            produtos.Add(Map(reader));
        }
        return produtos;
    }

    private IReadOnlyList<Produto> BuscarInterno(string sql)
    {
        using var connection = _factory.CreateConnection();
        connection.Open();

        using var command = connection.CreateCommand();
        command.CommandText = sql;
        using var reader = command.ExecuteReader();
        var produtos = new List<Produto>();
        while (reader.Read())
        {
            produtos.Add(Map(reader));
        }
        return produtos;
    }

    private static void AddParameters(SqliteCommand command, Produto produto)
    {
        command.Parameters.AddWithValue("$nome", produto.Nome);
        command.Parameters.AddWithValue("$tipo", (object?)produto.Tipo ?? DBNull.Value);
        command.Parameters.AddWithValue("$regiao", (object?)produto.Regiao ?? DBNull.Value);
        command.Parameters.AddWithValue("$uva", (object?)produto.Uva ?? DBNull.Value);
        var teor = ToInvariantString(produto.TeorAlcoolico);
        command.Parameters.AddWithValue("$teor", teor is null ? DBNull.Value : teor);
        command.Parameters.AddWithValue("$intensidade", (object?)produto.Intensidade ?? DBNull.Value);
        command.Parameters.AddWithValue("$encorpado", (object?)produto.Encorpado ?? DBNull.Value);
        command.Parameters.AddWithValue("$acucar", (object?)produto.Acucar ?? DBNull.Value);
        command.Parameters.AddWithValue("$acidez", (object?)produto.Acidez ?? DBNull.Value);
        command.Parameters.AddWithValue("$valor", ToInvariantString(produto.Valor));
        command.Parameters.AddWithValue("$estoque", produto.QuantidadeEstoque);
        var avaliacao = ToInvariantString(produto.AvaliacaoCritica);
        command.Parameters.AddWithValue("$avaliacao", avaliacao is null ? DBNull.Value : avaliacao);
    }

    private static Produto Map(SqliteDataReader reader)
    {
        return new Produto
        {
            Id = reader.GetInt32(reader.GetOrdinal("id")),
            Nome = reader.GetString(reader.GetOrdinal("nome")),
            Tipo = reader.IsDBNull(reader.GetOrdinal("tipo")) ? null : reader.GetString(reader.GetOrdinal("tipo")),
            Regiao = reader.IsDBNull(reader.GetOrdinal("regiao")) ? null : reader.GetString(reader.GetOrdinal("regiao")),
            Uva = reader.IsDBNull(reader.GetOrdinal("uva")) ? null : reader.GetString(reader.GetOrdinal("uva")),
            TeorAlcoolico = ReadNullableDecimal(reader, "teor_alcoolico"),
            Intensidade = ReadNullableInt(reader, "intensidade"),
            Encorpado = ReadNullableInt(reader, "encorpado"),
            Acucar = ReadNullableInt(reader, "acucar"),
            Acidez = ReadNullableInt(reader, "acidez"),
            Valor = ReadDecimal(reader, "valor"),
            QuantidadeEstoque = reader.GetInt32(reader.GetOrdinal("quantidade_estoque")),
            AvaliacaoCritica = ReadNullableDecimal(reader, "avaliacao_critica")
        };
    }

    private static decimal ReadDecimal(SqliteDataReader reader, string column)
        => decimal.Parse(reader.GetString(reader.GetOrdinal(column)), CultureInfo.InvariantCulture);

    private static decimal? ReadNullableDecimal(SqliteDataReader reader, string column)
        => reader.IsDBNull(reader.GetOrdinal(column))
            ? null
            : decimal.Parse(reader.GetString(reader.GetOrdinal(column)), CultureInfo.InvariantCulture);

    private static int? ReadNullableInt(SqliteDataReader reader, string column)
        => reader.IsDBNull(reader.GetOrdinal(column)) ? null : reader.GetInt32(reader.GetOrdinal(column));

    private static string? ToInvariantString(decimal? value)
        => value?.ToString(CultureInfo.InvariantCulture);

    private static string ToInvariantString(decimal value)
        => value.ToString(CultureInfo.InvariantCulture);
}
