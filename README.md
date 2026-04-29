# Android Agent ToolsをAntigravityで学ぼう

このリポジトリは、Androidに詳しくない人でも「専門領域のCLI、Skills、Knowledge Baseを使って既存アプリを安全に変更する」流れをAntigravityで体験するためのワークショップ用です。

題材は `event-schedule-demo` という普通のイベント予定表アプリです。アプリの見た目はセッション一覧、会場ガイド、詳細画面、メモダイアログです。Edge-to-Edge対応がまだ不完全な状態にしてあります。ワークショップではAndroid Skillを使って、Android 15以降で起きやすいシステムバー周りのレイアウト崩れを直します。

## なぜAntigravityで行うのか

Android関連のAIツールは、Android StudioのAgent Modeに一番多く揃っています。Googleが特に力を入れているのも、Android Studio上の開発体験です。

一方で、実際の開発ではClaude Codeなど、Android Studio以外のツールを使うことも多くあります。Googleは、そうしたツールを使っている場合でも、Android開発に必要な情報や操作へアクセスしやすくしようとしています。

このワークショップではAntigravityからAndroid CLI、Skills、Knowledge Baseを利用することで、Android Studio以外の環境でもAndroid向けの開発体験をどう作れるかを体験します。

## ゴール

- Android Emulatorを起動して、実機に近い環境でアプリを動かす
- Android CLIでビルド、起動、UI確認を行う
- Android Skillsをインストールする
- Android Knowledge Baseで公式ドキュメントを検索する
- `edge-to-edge` Skillを使って既存アプリのレイアウトを修正する
- ビルド、テスト、エミュレータ確認で「システムバーにUIが被っていない」ことを確認する

## 事前準備

開始前に、手元のPCで以下を確認してください。

- GoogleアカウントでAntigravityにサインインできる
- GitHubからこのリポジトリをcloneできる
- Android SDK、Android Emulator、JDKを使える、またはAndroid CLIに作成を依頼できる
- エラーが出たときに、エラー文をコピーしてAntigravityに渡せる

このワークショップでは、AndroidやWindowInsets APIを暗記することよりも、公式SkillとKnowledge Baseを使って確認可能な形で作業を進めることを重視します。

## 0. Antigravityをセットアップしよう

最初にAntigravityを起動できる状態にします。インストール済みの場合は、この章は読み飛ばして構いません。

以下の画像はmacOSでの例です。Windowsではインストーラーの案内に従ってAntigravityを起動してください。

macOSでは、ダウンロードしたAntigravityを `Applications` にドラッグ&ドロップしてから起動します。

![Antigravity installer](images/annotated-install-drag-to-applications.png)

初回起動したら、Welcome画面で `Next` を押します。

![Antigravity welcome](images/annotated-welcome-next.png)

テーマ選択では `Dark Modern` のまま `Next` を押します。別のテーマを選んでも、このワークショップの手順には影響しません。

![Antigravity theme](images/annotated-theme-next.png)

利用方法の選択では `Review-driven development` のまま `Next` を押します。

![Antigravity use case](images/annotated-use-case-next.png)

エディタ設定では `Normal`、`Recommended`、`Install` のまま `Next` を押します。

![Antigravity editor settings](images/annotated-editor-next.png)

`Sign in with Google` を押して、Googleアカウントでサインインします。

![Antigravity sign in](images/annotated-sign-in-google.png)

利用規約を確認します。1つ目のチェックが入っていることを確認して、`Next` を押します。

![Antigravity terms](images/annotated-terms-next.png)

セットアップが終わったら、まだリポジトリをcloneしていない場合は `Clone Repository` を押します。

![Antigravity clone repository](images/annotated-clone-repository.png)

表示された入力欄に、次のURLを入力してcloneします。

```text
git@github.com:takahirom/android-agent-tools-workshop.git
```

フォルダを開いたあと、信頼確認のダイアログが表示された場合は、対象フォルダがこのワークショップ用リポジトリであることを確認して `Yes, I trust the authors` を押します。

![Antigravity trust authors](images/annotated-trust-authors.png)

## Antigravityを使うときのコツ

### Planを使う場面

`Plan` は、すぐにファイル変更を始めず、先に作業方針や変更対象を整理して確認するためのモードです。Edge-to-Edge対応のように、どの画面のどのinsetを扱うかを先に見たい作業では `Plan` を有効にします。

計画を確認して問題なければ、その後に実装へ進めます。`Plan` は安全に進めるための確認ステップであり、クォータ節約そのものを目的にした機能ではありません。

### モデルと依頼の粒度を調整する

エージェントに調査、実装、テスト、画面確認をまとめて任せるほどクォータを消費します。ワークショップ中は、作業を小さく分けて、必要な情報をこちらから渡すようにします。複雑な修正方針の検討は `Gemini 3.1 Pro` 系、試行や軽い確認は `Gemini 3 Flash` 系のように、作業の重さに合わせてモデルを切り替えます。

- 1回の依頼では目的を1つに絞る
- 実装、QA、README修正は別の依頼に分ける
- 画面の見た目は自分で確認し、気づいたことを言葉で伝える
- エラーログはコピーしてチャットに貼る
- うまく進まない場合は、会話を分けるかモデルを切り替える

### 成果物を確認する

エージェントの作業後は、説明だけで判断せず、変更差分、実行したコマンド、テスト結果、Emulator上の画面を確認します。Antigravityが作るWalkthroughやTask Listは、何を変更したかを追う入口として使います。

`Failed to send` のようにチャット送信自体が失敗する場合は、一度Antigravityからログアウトし、再度Googleアカウントでサインインしてから試してください。

## 1. AntigravityでEmulatorを起動してみよう

まず、AntigravityでエージェントにAndroid CLIを使ってEmulatorを起動してもらいます。手元に使えるEmulatorがない場合は、作成から依頼します。

依頼文の例です。

```text
android cliを使って、Phone系のEmulatorを起動してください。
必要ならEmulatorを作成してください。
起動後、adb devices -l で device 状態になっていることを確認してください。
```

この依頼を実行したあとに `Agent terminated due to error` が表示された場合、モデルの空き容量が不足している可能性があります。`Retry` しても同じ表示になる場合は、入力欄のモデル名を押して `Gemini 3 Flash` など別のモデルに切り替えてから、もう一度実行してください。

![Antigravity no capacity available](images/antigravity-no-capacity-available.png)

エージェントは必要に応じて、次のようなコマンドを使います。

```bash
android emulator list
android emulator create
android emulator start --cold <AVD_NAME>
adb devices -l
```

`emulator-5554 device ...` のように `device` 状態のEmulatorが表示されればOKです。表示されたserialは、このあとの `--device=<DEVICE_SERIAL>` に指定します。以降の例では `<DEVICE_SERIAL>` と書きます。

## 2. Emulatorでアプリを起動してみよう

ここでは、エージェントにデバッグアプリのビルド、インストール、起動を依頼します。

依頼文の例です。

```text
event-schedule-demo のデバッグアプリをビルドして、Emulatorにインストールし、アプリを起動してください。
デバイスserialは、前の手順で確認したEmulatorのserialを使ってください。
```

エージェントは必要に応じて、次のようなコマンドを使います。

```bash
cd event-schedule-demo
./gradlew :app:assembleDebug
android run --apks=app/build/outputs/apk/debug/app-debug.apk --device=<DEVICE_SERIAL>
```

アプリが起動すればOKです。

## 3. AIがどうやってアプリを見ているか理解しよう

ここでは、AntigravityのTerminalを開き、Android CLIを自分で実行してみます。エージェントも同じようにCLIの出力、スクリーンショット、layout JSONを見ながらAndroidアプリを確認します。

画面右上のTerminalアイコンを押して、Terminalを開きます。

![Antigravity open terminal](images/antigravity-open-terminal.png)

起動済みのアプリに対して、Android CLIが画面をどう見ているかを確認します。ここは参加者が手で実行して、注釈付きPNGとlayout JSONが生成されることを確かめる時間にします。

macOS / Linux の例です。

```bash
android screen capture -a -o artifacts/android/event-schedule-annotated.png
android layout --pretty -o artifacts/android/event-schedule-layout.json
```

Windows PowerShell の例です。

```powershell
android screen capture -a -o artifacts/android/event-schedule-annotated.png
android layout --pretty -o artifacts/android/event-schedule-layout.json
```

`screen capture -a` は、UI要素に番号付きの枠を付けたPNGを出力します。`layout` は、画面上のテキスト、content description、座標、クリック可能かどうかなどをJSONで出力します。

必要なら、注釈付き画像の番号を座標に変換できます。

```bash
android screen resolve --screenshot=artifacts/android/event-schedule-annotated.png --string="tap #1"
```

ホーム画面で実行すると注釈が多くなり、何を見ているのか分かりにくくなります。このワークショップでは、アプリを起動してから `screen capture -a` と `layout` を実行します。

通常のスクリーンショットでは、次のように画面上部のタイトルがstatus barに近すぎたり、画面下部のnavigation bar周りに余白が足りなかったりすることが確認できます。

<img src="images/event-schedule-before-edge-to-edge.png" alt="Edge-to-Edge before" width="320">

これはEdge-to-Edgeと呼ばれる、画面の端までアプリを描画する表示に対して、レイアウト側の対応が不十分な状態です。このワークショップでは、この問題に対応していきます。

## 4. Android Skillsをインストールしよう

Edge-to-Edge用のSkillを探します。

```bash
android skills find edge
```

`edge-to-edge` が見つかったらインストールします。

```bash
android skills add --skill=edge-to-edge
```

## 5. Knowledge Baseで公式情報を見てみよう

Android CLIには、Android公式ドキュメントを検索するKnowledge Baseコマンドがあります。修正作業に入る前に、Edge-to-EdgeとWindowInsetsの情報を検索してみます。

```bash
android docs search "Edge-to-edge Compose WindowInsets Android 15"
```

検索結果にEdge-to-EdgeやWindowInsetsに関する `kb://android/...` のURLが出たら、内容を取得します。

```bash
android docs fetch <検索結果のkb:// URL>
```

ここで見たいのは、細かいAPIを覚えることではありません。エージェントが作業前に公式情報へアクセスし、前提条件や未対応機能を確認できることです。

## 6. Edge-to-Edgeの未対応箇所を修正しよう

ここからはエージェントに作業させます。依頼文の例です。

```text
event-schedule-demo の Edge-to-Edge 対応を完成させてください。
公式の edge-to-edge skill と Android Knowledge Base の情報に従ってください。
既存の画面遷移や操作感は変えず、システムバーにUIが被らないようにしてください。
最後にビルド、テスト、Emulatorでのスクショ確認まで行ってください。
```

修正中に主に見るファイルはこのあたりです。

- `event-schedule-demo/app/src/main/java/com/example/eventschedule/MainActivity.kt`
- `event-schedule-demo/app/src/main/java/com/example/eventschedule/Navigation.kt`
- `event-schedule-demo/app/src/main/java/com/example/eventschedule/ui/main/MainScreen.kt`

## 7. 修正後に確認しよう

ビルドとテストを実行します。

```bash
cd event-schedule-demo
./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug
```

もう一度Emulatorで起動します。

```bash
android run --apks=app/build/outputs/apk/debug/app-debug.apk --device=<DEVICE_SERIAL>
```

以下を確認します。

- セッション一覧が表示される
- セッション詳細へ遷移できる
- `Guide` タブへ移動できる
- メモダイアログを開閉できる
- Back操作が破綻していない
- タイトル、戻るボタン、リスト下部、ダイアログがstatus bar / navigation barに被っていない

## 8. 発展: QA担当として確認してもらおう

普通は、変更後にアプリが本当に動くかを自分で確認します。ただし、毎回手でEmulatorを起動して、画面を触って、ログを見るのは面倒です。

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
Edge-to-Edge 対応後も主要フローが壊れていないか、
ビルド、テスト、Emulator起動、UI操作、確認用スクショ保存まで行ってください。

確認シナリオ:
- Event Schedule が表示される
- セッション詳細へ遷移できる
- Guide タブへ移動できる
- Notes ダイアログを開閉できる
- Android Backで戻れる
- status bar / navigation bar と主要UIの重なりが疑われる箇所があれば報告する
```

この発展項目で見たいのは、「確認作業もエージェントに任せられるか」です。Edge-to-Edgeの最終判断はスクショを見て人間も確認しますが、ビルド、起動、操作、証跡保存を分担できると、実際の開発作業に近づきます。

## 9. ふりかえり

このワークショップで見たいポイントは、WindowInsets APIそのものの暗記ではありません。

- 公式Skillが修正方針を確認できるか
- Knowledge Baseで公式ドキュメントを根拠にできるか
- エージェントが既存挙動を壊さずに変更できるか
- QA担当に確認作業を委任できるか
- ビルド、テスト、Emulator確認まで進められるか
- 知らない技術領域でも、確認可能な形で作業を任せられるか

ここまでできればワークショップとしては完了です。時間が余った場合は、どの画面でどのinsetが必要だったかを見直します。

## 参考資料

- [Antigravity公式サイト](https://antigravity.google/)
- [Antigravity公式ドキュメント](https://antigravity.google/docs)
- [Antigravity plans](https://antigravity.google/docs/plans)
- [Getting Started with Google Antigravity Codelab](https://codelabs.developers.google.com/getting-started-google-antigravity?hl=ja)
- [antigravity-hands-on-gdg-sapporo](https://github.com/yanzm/antigravity-hands-on-gdg-sapporo)
