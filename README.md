# Android Agent ToolsをAntigravityで学ぼう

このリポジトリは、Androidに詳しくない人でも「専門領域のCLI、Skills、Knowledge Baseを使って既存アプリを安全に変更する」流れをAntigravityで体験するためのワークショップ用です。

題材は `event-schedule-demo` という普通のイベント予定表アプリです。アプリの見た目はセッション一覧、会場ガイド、詳細画面、メモダイアログですが、内部ではNavigation 2を使っています。ワークショップではAndroid Skillを使って、この既存アプリをNavigation 3へ移行します。

## ゴール

- Android Emulatorを起動して、実機に近い環境でアプリを動かす
- Android CLIでビルド、起動、UI確認を行う
- Android Skillsをインストールする
- Android Knowledge Baseで公式ドキュメントを検索する
- `navigation-3` Skillを使って既存アプリを移行する
- ビルド、テスト、エミュレータ確認で「見た目の挙動が変わっていない」ことを確認する

## 0. Antigravityをセットアップしよう

最初にAntigravityを起動できる状態にします。インストール済みの場合は、この章は読み飛ばして構いません。

以下の画像はmacOSでの例です。Windowsではインストーラーの案内に従ってAntigravityを起動してください。

macOSでは、ダウンロードしたAntigravityを `Applications` にドラッグ&ドロップしてから起動します。

![Antigravity installer](images/Screenshot%202026-04-29%20at%202.46.40%E2%80%AFPM.png)

初回起動したら、Welcome画面で `Next` を押します。

![Antigravity welcome](images/Screenshot%202026-04-29%20at%203.37.37%E2%80%AFPM.png)

テーマ選択では `Dark Modern` のまま `Next` を押します。別のテーマを選んでも、このワークショップの手順には影響しません。

![Antigravity theme](images/Screenshot%202026-04-29%20at%203.37.42%E2%80%AFPM.png)

利用方法の選択では `Review-driven development` のまま `Next` を押します。

![Antigravity use case](images/Screenshot%202026-04-29%20at%203.37.46%E2%80%AFPM.png)

エディタ設定では `Normal`、`Recommended`、`Install` のまま `Next` を押します。

![Antigravity editor settings](images/Screenshot%202026-04-29%20at%203.37.51%E2%80%AFPM.png)

`Sign in with Google` を押して、Googleアカウントでサインインします。

![Antigravity sign in](images/Screenshot%202026-04-29%20at%203.37.56%E2%80%AFPM.png)

利用規約を確認します。1つ目のチェックが入っていることを確認して、`Next` を押します。

![Antigravity terms](images/Screenshot%202026-04-29%20at%203.38.55%E2%80%AFPM.png)

セットアップが終わったら、まだリポジトリをcloneしていない場合は `Clone Repository` を押します。

![Antigravity clone repository](images/Screenshot%202026-04-29%20at%206.33.53%E2%80%AFPM.png)

表示された入力欄に、次のURLを入力してcloneします。

```text
git@github.com:takahirom/android-agent-tools-workshop.git
```

すでにclone済みの場合は、`Open Folder` でこのリポジトリのフォルダを開きます。手元に `workshop` フォルダがある場合は、そのフォルダを選びます。

![Antigravity open folder](images/Screenshot%202026-04-29%20at%203.39.28%E2%80%AFPM.png)

## 1. AntigravityでEmulatorを起動してみよう

まず、AntigravityでエージェントにAndroid CLIを使ってEmulatorを起動してもらいます。手元に使えるEmulatorがない場合は、作成から依頼します。

依頼文の例です。

```text
android cliを使って、Phone系のEmulatorを起動してください。
必要ならEmulatorを作成してください。
起動後、adb devices -l で device 状態になっていることを確認してください。
```

エージェントは必要に応じて、次のようなコマンドを使います。

```bash
android emulator list
android emulator create
android emulator start --cold <AVD_NAME>
adb devices -l
```

Antigravity経由では、Emulator起動を `while true` で維持する必要はない想定です。

続いて、エージェントに `GEMINI.md` を作ってもらいます。目的は「このリポジトリではEmulatorを実際に起動して確認する」「画面確認ではAndroid CLIの出力を証跡として残す」という作業ルールを残すことです。

依頼文の例です。

```text
このリポジトリ用の GEMINI.md を作ってください。
Android Emulator を実際に起動して確認すること、
Antigravity経由では while true で起動維持しなくてよいこと、
使うAVD名は android cli で確認または作成すること、
Play Store付きAVDで unauthorized になる場合は Google APIs 系のAVDを試すこと、
画面確認では android screen capture -a と android layout を使うことを書いてください。
```

`emulator-5554 device ...` のように表示されればOKです。以降の例では `emulator-5554` を使いますが、自分の環境で別のserialが表示された場合は読み替えてください。

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

起動したら、まずAndroid CLIが画面をどう見ているかを確認します。ここは参加者が手で実行して、注釈付きPNGとlayout JSONが生成されることを確かめる時間にします。

macOS / Linux の例です。

```bash
mkdir -p artifacts/android
android screen capture -a -o artifacts/android/event-schedule-annotated.png
android layout --pretty -o artifacts/android/event-schedule-layout.json
```

Windows PowerShell の例です。

```powershell
New-Item -ItemType Directory -Force artifacts/android
android screen capture -a -o artifacts/android/event-schedule-annotated.png
android layout --pretty -o artifacts/android/event-schedule-layout.json
```

`screen capture -a` は、UI要素に番号付きの枠を付けたPNGを出力します。`layout` は、画面上のテキスト、content description、座標、クリック可能かどうかなどをJSONで出力します。

必要なら、注釈付き画像の番号を座標に変換できます。

```bash
android screen resolve --screenshot=artifacts/android/event-schedule-annotated.png --string="tap #1"
```

ホーム画面で実行すると注釈が多くなり、何を見ているのか分かりにくくなります。このワークショップでは、アプリを起動してから `screen capture -a` と `layout` を実行します。

そのうえで、最低限以下を触ってみます。

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

## 4. Knowledge Baseで公式情報を見てみよう

Android CLIには、Android公式ドキュメントを検索するKnowledge Baseコマンドがあります。移行作業に入る前に、Navigation 3の移行ガイドを検索してみます。

```bash
android docs search "Migrate from Navigation 2 to Navigation 3"
```

検索結果に `kb://android/guide/navigation/navigation-3/migration-guide` のようなURLが出たら、内容を取得します。

```bash
android docs fetch kb://android/guide/navigation/navigation-3/migration-guide
```

ここで見たいのは、細かいAPIを覚えることではありません。エージェントが作業前に公式情報へアクセスし、前提条件や未対応機能を確認できることです。

## 5. Navigation 2からNavigation 3へ移行しよう

ここからはエージェントに作業させます。依頼文の例です。

```text
event-schedule-demo を Navigation 2 から Navigation 3 に移行してください。
公式の navigation-3 skill と Android Knowledge Base の migration guide に従ってください。
既存の画面や操作感は変えず、ビルド、テスト、Emulatorでの動作確認まで行ってください。
```

移行中に主に変わるファイルはこのあたりです。

- `event-schedule-demo/app/src/main/java/com/example/eventschedule/NavigationKeys.kt`
- `event-schedule-demo/app/src/main/java/com/example/eventschedule/Navigation.kt`
- `event-schedule-demo/app/build.gradle.kts`
- `event-schedule-demo/gradle/libs.versions.toml`

## 6. 移行後に確認しよう

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

## 7. 発展: QA担当として確認してもらおう

普通は、移行後にアプリが本当に動くかを自分で確認します。ただし、毎回手でEmulatorを起動して、画面を触って、ログを見るのは面倒です。

ここでは発展項目として、AntigravityにQA担当として振る舞ってもらうための作業ルールを作ります。QA担当のルール自体は、このアプリ専用ではなく「Androidアプリの変更後確認を担当する汎用ルール」として作ります。具体的な確認シナリオは、依頼するときに渡します。

依頼文の例です。

```text
このリポジトリ用の GEMINI.md に、QA確認の作業ルールを追加してください。

目的:
- Androidアプリの変更後に、ビルド、テスト、Emulatorでの起動確認を行う
- ユーザーから渡された確認シナリオに沿ってUIを操作する
- 失敗した場合は原因を報告する
- 可能なら android screen capture -a のPNGや android layout のJSONを証跡として残す

振る舞い:
- 最初に、対象アプリ、ビルドコマンド、APKパス、デバイスserial、確認シナリオを確認する
- 情報が足りない場合は、リポジトリを読んで合理的に推測する
- 推測できない場合だけ質問する
- 実行したコマンド、確認した画面、失敗内容、未確認項目を最後に短く報告する
```

作成後は、次のようにQA担当として依頼します。

```text
QA担当として event-schedule-demo を確認してください。
Navigation 3 移行後も主要フローが壊れていないか、
ビルド、テスト、Emulator起動、UI操作まで確認してください。

確認シナリオ:
- Event Schedule が表示される
- セッション詳細へ遷移できる
- Guide タブへ移動できる
- Notes ダイアログを開閉できる
- Android Backで戻れる
```

この発展項目で見たいのは、「確認作業もエージェントに任せられるか」です。移行そのものだけでなく、移行後のQAまで分担できると、実際の開発作業に近づきます。

## 8. ふりかえり

このワークショップで見たいポイントは、Navigation APIそのものの暗記ではありません。

- 公式Skillが移行前提を確認できるか
- Knowledge Baseで公式ドキュメントを根拠にできるか
- エージェントが既存挙動を壊さずに変更できるか
- QA担当に確認作業を委任できるか
- ビルド、テスト、Emulator確認まで進められるか
- 知らない技術領域でも、確認可能な形で作業を任せられるか

ここまでできればワークショップとしては完了です。時間が余った場合は、移行前後の `Navigation.kt` を見比べて、`NavHost` / `NavController` から `NavDisplay` / back stack管理へどう変わったかを確認します。
