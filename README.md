# Catalog Viewer

A small Android app built with **Kotlin**, **Jetpack Compose**, **Hilt**, **DataStore**, and **Coroutines/Flow**. It loads a bundled JSON catalog, displays items in a searchable list, supports item details, and persists favorite items locally.

## Run

Connect an Android device or start an emulator, then run:

```bash
./gradlew installDebug
```

## Test

```bash
./gradlew test
```

## Features

- Catalog list with title, category, price, and rating
- Case-insensitive title search
- Detail screen for each item
- Favorite toggle with persistence across app restarts
- Empty and error states for list and detail loading

## Architecture

The app follows a layered MVI-style structure:

- **Domain** — Pure models, repository contracts, and use cases.
- **Data** — Reads the bundled JSON from assets, maps DTOs into domain models, and stores favorite IDs with DataStore Preferences.
- **Presentation** — Compose screens, screen contracts, and ViewModels that expose state and one-shot effects.

Catalog data is treated as a read-only source, while favorites are modeled as user-owned reactive state. Repository interfaces expose catalog reads as suspend functions and favorites as a Flow, allowing list and detail screens to update automatically when favorite IDs change. UI state is exposed from ViewModels and rendered by Compose; user actions are sent back as intents.

## Trade-offs

- The catalog is bundled in assets rather than fetched from a network because the exercise does not require networking or authentication.
- DataStore stores only favorite item IDs instead of full item records, avoiding data duplication and keeping persistence small.
- Hilt is used for dependency injection. Dispatchers are injected so blocking asset reads run on IO and remain testable.

## Testing

The project includes focused unit tests for search behavior and the catalog/favorites use case flow. Repository implementations are intentionally thin, so testing focuses on domain behavior rather than delegation.

With more time I would add ViewModel tests for state transitions, Compose UI tests for search and favorite interactions, and repository tests if data-source coordination became more complex.

https://github.com/user-attachments/assets/e86ab784-059f-4dac-a02b-663963453ddc

