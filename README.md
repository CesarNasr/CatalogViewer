# Catalog Viewer

Catalog Viewer is a small Android app built with Kotlin and Jetpack Compose. It loads a bundled JSON catalog, displays items in a searchable list, supports item details, and persists favorite items locally.

## Run

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
- Empty and error states for list/detail loading

## Architecture

The app follows a layered MVI-style structure. The domain layer contains pure models, repository contracts, and use cases. The data layer reads the bundled JSON from assets, maps DTOs into domain models, and stores favorite IDs with DataStore Preferences. The presentation layer uses Compose screens, screen contracts, and ViewModels that expose state and one-shot effects.

Catalog data is treated as read-only source data, while favorites are modeled as user-owned reactive state. The repository exposes catalog reads as suspend functions and favorites as a Flow, allowing list and detail screens to update automatically when favorite IDs change.

## Trade-offs

The catalog is bundled in assets rather than fetched from a network because the exercise does not require networking or authentication. DataStore stores only favorite item IDs instead of full item records, which avoids duplicating catalog data and keeps persistence small. Hilt is used for dependency injection, while dispatchers are injected so blocking asset reads can run on IO and remain testable.

The project includes focused unit tests for search behavior and the catalog/favorites use case flow. Repository implementations are intentionally thin, so testing focuses on domain behavior rather than delegation.
