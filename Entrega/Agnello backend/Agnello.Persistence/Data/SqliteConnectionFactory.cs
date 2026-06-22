using Microsoft.Data.Sqlite;

namespace Agnello.Persistence.Data;

public sealed class SqliteConnectionFactory
{
    private readonly string _connectionString;

    public SqliteConnectionFactory(string? databasePath = null)
    {
        var path = databasePath ?? Path.Combine(AppContext.BaseDirectory, "agnello-estoque.db");
        _connectionString = new SqliteConnectionStringBuilder
        {
            DataSource = path
        }.ToString();
    }

    public SqliteConnection CreateConnection()
        => new SqliteConnection(_connectionString);
}
