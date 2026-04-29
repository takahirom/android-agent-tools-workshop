# Event Schedule

This is a small Jetpack Compose event schedule app designed for an Edge-to-Edge workshop.

The user-facing app is deliberately ordinary: a schedule tab, a venue guide tab, detail screens, and an event notes dialog. It intentionally leaves the Edge-to-Edge layout incomplete so the workshop can focus on fixing system bar overlap with Android documentation, skills, build checks, and screenshots.

## Demo Flow

1. Build the app:

   ```bash
   ./gradlew :app:compileDebugKotlin
   ```

2. Install or select the official Android skill:

   ```bash
   android skills add --skill=edge-to-edge
   ```

3. Ask the agent to fix the Edge-to-Edge layout:

   ```text
   Fix the incomplete Edge-to-Edge handling in this app.
   Use the official edge-to-edge skill and Android documentation.
   Keep the existing screen transitions and user-facing behavior unchanged.
   ```

4. Verify the change by rerunning the build and tests:

   ```bash
   ./gradlew :app:compileDebugKotlin :app:testDebugUnitTest
   ```

## Files To Watch During The Workshop

- `app/src/main/java/com/example/eventschedule/Navigation.kt`
- `app/src/main/java/com/example/eventschedule/MainActivity.kt`
- `app/src/main/java/com/example/eventschedule/ui/main/MainScreen.kt`
