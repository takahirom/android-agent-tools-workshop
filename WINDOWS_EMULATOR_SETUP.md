# Windows Emulator Setup

このファイルは、Windowsでワークショップに参加する人向けの補助手順です。

ここで行うことは、次の2つだけです。

- Android StudioでEmulatorを起動する
- 起動したEmulatorのdevice serialを確認する

ここまでできたら、本編READMEに戻ってAntigravityから作業を続けます。

> Status:
> この手順は公式ドキュメントに基づくWindows向け補助手順です。
> 作者の手元ではWindows実機で未検証です。
> Windows環境で確認できた方は、IssueまたはPRでフィードバックしてください。

## なぜ別手順が必要か

現時点では、Android CLI の `android emulator` コマンドは Windows で無効になっています。
そのため、Windowsでは次のAndroid CLIコマンドを使ってEmulatorを作成・起動する手順は使いません。

```powershell
android emulator list
android emulator create --profile=medium_phone
android emulator start --cold <AVD_NAME>
```

Windowsでは、Android Studio の Device Manager でEmulatorを作成・起動します。

## 1. Android Studioを用意する

Android Studioをまだインストールしていない場合は、公式ページからインストールしてください。

- [Download Android Studio](https://developer.android.com/studio)

インストール後、Android Studioを起動します。

初回起動時にセットアップ画面が表示された場合は、表示される案内に従って進めます。Android SDKやEmulator関連のコンポーネントをインストールする画面が出た場合は、選択されたまま進めてください。

## 2. Device Managerを開く

Android StudioのWelcome画面が表示されている場合は、次の順に開きます。

```text
More Actions > Virtual Device Manager
```

すでにAndroid Studioでプロジェクトを開いている場合は、次の順に開きます。

```text
View > Tool Windows > Device Manager
```

Device Managerは、Android StudioでEmulatorを作成・起動するための画面です。

## 3. Phone系Emulatorを作成する

Device Managerに使えるEmulatorがまだない場合は、新しいVirtual Deviceを作成します。

Device Managerで `+` または `Create Virtual Device` を押します。

選択する内容の目安です。

- Form factor: Phone
- Device: Pixel系、または標準的なPhoneデバイス
- System Image: Android 15 / API 35 以上
- Image type: Google APIs または Google Play

このワークショップではEdge-to-Edgeの挙動を確認したいため、Android 15 / API 35 以上のEmulatorを推奨します。

System Imageに `Download` と表示されている場合は、Android Studioの案内に従ってダウンロードしてください。ダウンロードが終わったら、`Next` や `Finish` を押して作成を完了します。

すでにAndroid 15 / API 35 以上のPhone系Emulatorがある場合は、新しく作らずにそれを使って構いません。

## 4. Emulatorを起動する

Device Managerで、使うEmulatorの再生ボタンを押して起動します。

Emulatorのホーム画面が表示されるまで待ちます。起動には数分かかることがあります。

## 5. Device Serialを確認する

次に、起動したEmulatorをAndroid CLIから指定するためのdevice serialを確認します。

AntigravityのTerminal、またはWindows Terminal / PowerShellを開き、次のコマンドをそのまま貼り付けて実行します。

```powershell
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" devices -l
```

`$env:LOCALAPPDATA` は、Windowsの自分のユーザーフォルダ配下にある `AppData\Local` を指します。Android Studioが標準の場所にAndroid SDKを入れている場合、`adb.exe` は次の場所にあります。

```text
C:\Users\<Windowsのユーザー名>\AppData\Local\Android\Sdk\platform-tools\adb.exe
```

次のように `device` と表示されればOKです。

```text
List of devices attached
emulator-5554 device product:... model:... device:...
```

この例では、device serial は `emulator-5554` です。

もし `指定されたパスが見つかりません` のようなエラーが出た場合は、Android StudioでSDKの場所を確認します。

```text
File > Settings > Languages & Frameworks > Android SDK
```

画面上部の `Android SDK Location` に表示されているフォルダを確認し、その中の `platform-tools\adb.exe` を使ってください。

## ここまでできたらREADMEに戻る

このファイルで行うWindows固有の作業はここまでです。

本編READMEの「3. Emulatorでアプリを起動してみよう」に戻り、`<DEVICE_SERIAL>` と書かれている部分を、ここで確認したdevice serialに置き換えて進めてください。

以降のビルド、アプリ起動、スクリーンショット取得、layout JSON取得は、本編READMEの手順でAntigravityから実行します。

## 参考資料

- [Create and manage virtual devices](https://developer.android.com/studio/run/managing-avds)
- [Android Debug Bridge](https://developer.android.com/studio/command-line/adb)
- [Run apps on the Android Emulator](https://developer.android.com/studio/run/emulator)
