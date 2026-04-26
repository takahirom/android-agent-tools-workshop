# Android Skills Workshop

このリポジトリは、Androidに詳しくない人でも「専門領域のSkillを使って既存アプリを安全に変更する」流れを体験するためのワークショップ用です。

題材は `event-schedule-demo` という普通のイベント予定表アプリです。アプリの見た目はセッション一覧、会場ガイド、詳細画面、メモダイアログですが、内部ではNavigation 2を使っています。ワークショップではAndroid Skillを使って、この既存アプリをNavigation 3へ移行します。

## ゴール

- Android Emulatorを起動して、実機に近い環境でアプリを動かす
- Android Skillsをインストールする
- `navigation-3` Skillを使って既存アプリを移行する
- ビルド、テスト、エミュレータ確認で「見た目の挙動が変わっていない」ことを確認する

## 1. Emulatorを起動してみよう

まず使えるAVDを確認します。

```bash
android emulator list
```

この環境では `AndroidWorldAvd` が使いやすい想定です。Play Store付きAVDでADBが `unauthorized` になる場合は、Google APIs系のAVDを使ってください。

ここで、エージェントに `GEMINI.md` を作ってもらいます。目的は「このリポジトリではEmulatorを実際に起動して確認する」「起動維持が必要な場合は `while true` を使う」という作業ルールを残すことです。

依頼文の例です。

```text
このリポジトリ用の GEMINI.md を作ってください。
Android Emulator を実際に起動して確認すること、
android emulator start のプロセスが終わると Emulator も落ちることがあるので、
必要なら while true で起動を維持すること、
Play Store付きAVDで unauthorized になる場合は AndroidWorldAvd を使うことを書いてください。
```

`GEMINI.md` に入れてほしい内容の例です。

```bash
while true; do
  android emulator start --cold AndroidWorldAvd
  sleep 2
done
```

起動後、ADBで `device` として見えることを確認します。

```bash
adb devices -l
```

`emulator-5554 device ...` のように表示されればOKです。

## 2. Emulatorでアプリを起動してみよう

アプリをビルドします。

```bash
cd event-schedule-demo
./gradlew :app:assembleDebug
```

Emulatorへインストールして起動します。

```bash
android run --apks=app/build/outputs/apk/debug/app-debug.apk --device=emulator-5554
```

起動したら、最低限以下を触ってみます。

- `Event Schedule` にセッション一覧が表示される
- セッションを開いて詳細画面へ遷移できる
- `Guide` タブへ移動できる
- `Notes` からイベントメモのダイアログを開ける
- AndroidのBackで前の画面に戻れる

## 3. Android Skillsをインストールしよう

Navigation 3用のSkillを探します。

```bash
android skills find nav
```

`navigation-3` が見つかったらインストールします。

```bash
android skills add navigation-3
```

## 4. Navigation 2からNavigation 3へ移行しよう

ここからはエージェントに作業させます。依頼文の例です。

```text
event-schedule-demo を Navigation 2 から Navigation 3 に移行してください。
公式の navigation-3 skill と migration guide に従ってください。
既存の画面や操作感は変えず、ビルド、テスト、Emulatorでの動作確認まで行ってください。
```

移行中に主に変わるファイルはこのあたりです。

- `event-schedule-demo/app/src/main/java/com/example/eventschedule/NavigationKeys.kt`
- `event-schedule-demo/app/src/main/java/com/example/eventschedule/Navigation.kt`
- `event-schedule-demo/app/build.gradle.kts`
- `event-schedule-demo/gradle/libs.versions.toml`

## 5. 移行後に確認しよう

ビルドとテストを実行します。

```bash
cd event-schedule-demo
./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug
```

もう一度Emulatorで起動します。

```bash
android run --apks=app/build/outputs/apk/debug/app-debug.apk --device=emulator-5554
```

移行前と同じように以下を確認します。

- セッション一覧が表示される
- セッション詳細へ遷移できる
- `Guide` タブへ移動できる
- メモダイアログを開閉できる
- Back操作が破綻していない

## 6. ふりかえり

このワークショップで見たいポイントは、Navigation APIそのものの暗記ではありません。

- 公式Skillが移行前提を確認できるか
- エージェントが既存挙動を壊さずに変更できるか
- ビルド、テスト、Emulator確認まで進められるか
- 知らない技術領域でも、確認可能な形で作業を任せられるか

ここまでできればワークショップとしては完了です。時間が余った場合は、移行前後の `Navigation.kt` を見比べて、`NavHost` / `NavController` から `NavDisplay` / back stack管理へどう変わったかを確認します。
