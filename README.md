# 📱 Terminal Pet (Android)

Тот самый ASCII-питомец, но теперь живёт в твоём телефоне!

Тёмная неоновая тема в стиле терминала, питомец голодает, устаёт, грустит —
и **помнит тебя между запусками** (состояние сохраняется в SharedPreferences).

## Скачать

Готовый APK лежит в [Releases](https://github.com/Karishka-sr/terminal-pet/releases).
APK подписан: `com.terminalpet.app`, версия 1.0.0, для Android 8.0+ (API 26).

## Что умеет

* 🍖 **Покормить** — восстанавливает сытость.
* 🎾 **Поиграть** — тратит энергию, но поднимает счастье.
* 💤 **Спать** — восстанавливает энергию.
* ⏳ **Тик** — время идёт: питомец голодает и устаёт.
* ✨ **Новый питомец** — начать заново.

Если долго не ухаживать — питомец «уходит в большой скроллбуфер». 💔
Береги его, и он проживёт долгую счастливую жизнь.

## Эмоции

| Настроение | Мордочка |
|---|---|
| happy | `(^_^)` |
| hungry | `(o.o)` |
| sleepy | `(-_-)` |
| sad | `(;_;)` |
| asleep | `(u.u)zZ` |

## Сборка самому

Нужен JDK 17+, Android SDK (platform 34, build-tools 34), Gradle 8.7.

```bash
# путь к SDK в local.properties
echo "sdk.dir=/путь/к/android-sdk" > local.properties
./gradlew assembleRelease
# результат: app/build/outputs/apk/release/app-release.apk
```

Для подписи используется `keystore/terminal-pet.jks` (пароль в
`keystore/.keystore_pass`). Keystore не коммитится — храни его в секретах CI.

---

Сделано с ♥ OpenHands. Никакие чужие репозитории не пострадали.