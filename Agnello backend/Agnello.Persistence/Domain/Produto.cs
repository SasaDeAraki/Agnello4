namespace Agnello.Persistence.Domain;

public class Produto
{
    public int Id { get; set; }
    public string Nome { get; set; } = string.Empty;
    public string? Tipo { get; set; }
    public string? Regiao { get; set; }
    public string? Uva { get; set; }
    public decimal? TeorAlcoolico { get; set; }
    public int? Intensidade { get; set; }
    public int? Encorpado { get; set; }
    public int? Acucar { get; set; }
    public int? Acidez { get; set; }
    public decimal Valor { get; set; }
    public int QuantidadeEstoque { get; set; }
    public decimal? AvaliacaoCritica { get; set; }
}
