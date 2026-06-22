# Agnello - instrucoes de execucao

Este workspace tem dois projetos:

- `Agnello backend`: API C# com persistencia SQLite.
- `Agnello frontend`: app Android em Compose com Room e consumo da API.

## Requisitos

- .NET 8 SDK
- Android Studio ou Android SDK instalado
- Emulador Android ou dispositivo fisico

## Backend C#

O backend expoe a API em `http://localhost:5000` e usa SQLite local.

### Rodar

```powershell
cd "D:\Arquivos\Curso\FIAP\Ano 3\Fase 4\Agnello backend"
dotnet run --project ".\Agnello.Api\Agnello.Api.csproj"
```

### Testar a API

Abra no navegador ou via PowerShell:

```powershell
Invoke-WebRequest http://127.0.0.1:5000/
Invoke-WebRequest http://127.0.0.1:5000/api/produtos
```

### Endpoints principais

- `GET /api/produtos`
- `GET /api/produtos/{id}`
- `POST /api/produtos`
- `PUT /api/produtos/{id}`
- `DELETE /api/produtos/{id}`

## Frontend Android

O app usa:

- Room para cache local
- Retrofit para consumir a API
- Compose para a interface

### Rodar no Android Studio

Abra a pasta:

```text
D:\Arquivos\Curso\FIAP\Ano 3\Fase 4\Agnello frontend
```

Depois execute o modulo `app`.

### Rodar via linha de comando

```powershell
cd "D:\Arquivos\Curso\FIAP\Ano 3\Fase 4\Agnello frontend"
.\gradlew.bat :app:assembleDebug
```

### URL da API no app

O app aponta para:

```text
http://10.0.2.2:5000/
```

Use este endereco quando estiver rodando no emulador Android.

Se estiver usando celular fisico, troque o `BASE_URL` em:

```text
Agnello frontend\app\src\main\java\br\com\fiap\agnellofrontend\data\AppContainer.kt
```

para o IP da sua maquina na rede local, por exemplo:

```text
http://192.168.0.10:5000/
```

## Estrutura importante

### Backend

- `Agnello.Api/Program.cs`: API REST
- `Agnello.Persistence`: dominio, repositorio e SQLite

### Frontend

- `app/src/main/java/.../data/local`: Room
- `app/src/main/java/.../data/remote`: Retrofit
- `app/src/main/java/.../data/repository`: sincronizacao entre API e Room
- `app/src/main/java/.../ui`: tela principal e ViewModel

## Observacoes

- O banco local do backend e o cache do Room sao separados.
- Se o app mostrar erro de conexao, confirme primeiro se a API esta rodando na porta `5000`.
- Se a porta `5000` ja estiver ocupada, feche a instancia antiga do backend antes de iniciar outra.

