# Auto Paste — ARM32

Package: `com.autopaste.app`  
Native ABI target: `armeabi-v7a` only.

## Features
- User-entered text; no clipboard access.
- Editable interval and repeat target settings.
- Accessibility-assisted insertion into a focused editable field.
- One insertion per explicit activation.
- Does not press Send or submit comments.

## Android setup
1. Install the APK.
2. Open Auto Paste.
3. Enter the text and save.
4. Open Android Accessibility settings and enable Auto Paste.
5. Return to the app and tap `PREPARE 1x PASTE`.
6. Switch to the destination app and focus an editable text field.
7. Check the inserted text yourself before manually sending.

Some apps do not expose editable fields to Android Accessibility, so insertion may not work in every app. The app never reads clipboard contents.
