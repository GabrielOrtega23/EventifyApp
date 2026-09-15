# Eventify — App Android (Jetpack Compose + Room/SQLite)

Projeto Android nativo do Eventify, construído a partir do protótipo web
(`index.html` / `style.css` / `script.js`) e do mockup de alta fidelidade,
seguindo o roteiro do quadro branco:

1. Prototipagem de interface (alta fidelidade) — referência de design
2. Interface em Jetpack Compose (Splash + Login + App) — sem limites de tela
3. Persistência local com SQLite via Room
4. Repositório no GitHub

## Como abrir

1. Abra o Android Studio (recomendado: Koala/Ladybug ou mais recente).
2. `File > Open` e selecione a pasta `Eventify` (esta pasta, a raiz que contém `settings.gradle.kts`).
3. Aguarde o Gradle sincronizar (baixa Compose, Navigation, Room, Coil).
4. Rode no emulador ou dispositivo físico (`minSdk 24`).

## Estrutura

```
app/src/main/java/com/gabriel/eventify/
├── data/
│   ├── local/
│   │   ├── entity/        -> UserEntity, EventEntity, ReservationEntity
│   │   ├── dao/            -> UserDao, EventDao, ReservationDao
│   │   └── AppDatabase.kt  -> Room database + pré-carga de eventos (seed)
│   └── repository/         -> AuthRepository, EventRepository
├── ui/
│   ├── theme/               -> Color.kt, Type.kt, Theme.kt (paleta do mockup)
│   ├── components/          -> GradientButton, EventCard
│   ├── navigation/          -> EventifyNavGraph.kt (NavHost)
│   ├── screens/
│   │   ├── splash/          -> SplashScreen
│   │   ├── auth/            -> LoginScreen, SignUpScreen, AuthViewModel
│   │   ├── home/             -> HomeScreen (busca + filtro por categoria), HomeViewModel
│   │   └── eventdetail/      -> EventDetailScreen (reserva de assentos), EventDetailViewModel
│   └── ViewModelFactory.kt
├── EventifyApp.kt            -> Application (instancia Room + repositórios)
└── MainActivity.kt
```

## Banco de dados (Room/SQLite)

- **users**: cadastro/login local (nome, e-mail, senha).
- **events**: eventos exibidos na Home (pré-populado no primeiro run com
  eventos de exemplo, incluindo os do mockup: *Rock in Rio 2026*, *Indie Music
  Festival*, *Romeo and Juliet*).
- **reservations**: registra as reservas de assento feitas por cada usuário.

Todo o acesso é feito via DAOs com `Flow`/`suspend`, então a lista de eventos
na Home é reativa (atualiza sozinha se o banco mudar).

## Fluxo de telas (bate com o mockup)

`Splash` → `Login` (ou `Cadastro`) → `Home` (busca + categorias + lista de
eventos) → `Detalhe do evento` (descrição, seletor de assentos, reserva).

## Próximos passos sugeridos

- Adicionar upload de imagem de perfil e tela de "Meus ingressos" (lendo
  `reservations` pelo `userId`).
- Trocar o hash de senha em texto puro por um hash (ex.: `BCrypt`) antes de
  entregar em produção.
- Conectar o ícone do launcher (`mipmap`) — este projeto não inclui ícones
  customizados para manter o pacote enxuto.
