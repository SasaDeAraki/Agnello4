using Microsoft.Data.Sqlite;

namespace Agnello.Persistence.Data;

public static class EstoqueDbInitializer
{
    public static void EnsureCreated(SqliteConnectionFactory factory)
    {
        using var connection = factory.CreateConnection();
        connection.Open();

        using var command = connection.CreateCommand();
        command.CommandText = """
            CREATE TABLE IF NOT EXISTS produto (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                tipo TEXT NULL,
                regiao TEXT NULL,
                uva TEXT NULL,
                teor_alcoolico TEXT NULL,
                intensidade INTEGER NULL,
                encorpado INTEGER NULL,
                acucar INTEGER NULL,
                acidez INTEGER NULL,
                valor TEXT NOT NULL,
                quantidade_estoque INTEGER NOT NULL,
                avaliacao_critica TEXT NULL
            );
            """;
        command.ExecuteNonQuery();
    }
}
