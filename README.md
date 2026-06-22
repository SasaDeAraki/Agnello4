# Agnello - instrucoes de execucao

Este arquivo deve ser lido a partir da raiz da entrega descompactada.
A estrutura esperada e:

- `Agnello backend`
- `Agnello frontend`

O pacote contem dois projetos:
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
cd ".\Agnello backend"
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
cd ".\Agnello frontend"
```

Depois execute o modulo `app`.

### Rodar via linha de comando

```powershell
cd ".\Agnello frontend"
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
.\Agnello frontend\app\src\main\java\br\com\fiap\agnellofrontend\data\AppContainer.kt
```

para o IP da sua maquina na rede local, por exemplo:

```text
http://192.168.0.10:5000/
```

## Estrutura importante

### Backend

- `Agnello backend\Agnello.Api\Program.cs`: API REST
- `Agnello backend\Agnello.Persistence`: dominio, repositorio e SQLite
### Frontend

- `Agnello frontend\app\src\main\java\...\data\local`: Room
- `Agnello frontend\app\src\main\java\...\data\remote`: Retrofit
- `Agnello frontend\app\src\main\java\...\data\repository`: sincronizacao entre API e Room
- `Agnello frontend\app\src\main\java\...\ui`: tela principal e ViewModel

## Observacoes

- O banco local do backend e o cache do Room sao separados.
- Se o app mostrar erro de conexao, confirme primeiro se a API esta rodando na porta `5000`.
- Se a porta `5000` ja estiver ocupada, feche a instancia antiga do backend antes de iniciar outra.

