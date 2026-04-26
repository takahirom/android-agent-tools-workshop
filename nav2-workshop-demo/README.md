# Event Schedule

This is a small Jetpack Compose event schedule app designed as a Navigation 2 baseline for a Navigation 3 migration demo.

The user-facing app is deliberately ordinary: a schedule tab, a venue guide tab, detail screens, and an event notes dialog. Under the hood, it intentionally uses:

- `androidx.navigation:navigation-compose:2.9.7`
- `rememberNavController`
- `NavHost`
- type-safe serializable routes
- one-level nested top-level graphs
- a dialog destination
- bottom navigation with saved top-level state

It intentionally avoids deep links and custom destination types because those are not covered by the official Navigation 2 to Navigation 3 migration guide.

## Demo Flow

1. Build the Nav2 baseline:

   ```bash
   ./gradlew :app:compileDebugKotlin
   ```

2. Install or select the official Android skill:

   ```bash
   android skills add navigation-3
   ```

3. Ask the agent to migrate the project:

   ```text
   Migrate this app from Navigation 2 to Navigation 3 using the official migration guide.
   ```

4. Verify the migration by rerunning the build and tests:

   ```bash
   ./gradlew :app:compileDebugKotlin :app:testDebugUnitTest
   ```

## Files To Watch During Migration

- `app/src/main/java/com/example/nav2workshopdemo/NavigationKeys.kt`
- `app/src/main/java/com/example/nav2workshopdemo/Navigation.kt`
- `app/build.gradle.kts`
- `gradle/libs.versions.toml`
