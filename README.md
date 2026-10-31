# Okuma Yolculuğu

7 yaşındaki çocuklar için reklamsız, çevrimdışı çalışan Türkçe okuma-yazma öğrenme uygulamasının ilk Android sürümü.

## Özellikler

- Türk alfabesindeki 29 harfin tamamı
- Büyük/küçük harf gösterimi ve sevimli örnek kelime
- Android cihazın Türkçe Text-to-Speech motoru ile harf seslendirme
- Harfleri sırayla açan ders akışı ve tamamlanma takibi
- Mini alıştırma ekranı
- Güncel ilerleme çubuğu
- Telefon ve tabletlerde Compose tabanlı responsive arayüz
- Reklamsız ve ağ izni istemeyen çevrimdışı tasarım
- GitHub Actions ile otomatik debug APK üretimi

## Teknoloji

- Kotlin 2.0.21
- Jetpack Compose ve Material 3
- Android Gradle Plugin 8.7.3
- Minimum Android API 24 (Android 7.0)
- Yerel durum: ilk sürümde uygulama oturumu içi ilerleme; sonraki sürümde DataStore kalıcılığı eklenebilir

## Android Studio ile çalıştırma

1. Bu klasörü Android Studio ile açın.
2. Gradle senkronizasyonunun tamamlanmasını bekleyin.
3. Android 7.0 veya üzeri bir emülatör/cihaz seçin.
4. `app` çalıştırma yapılandırmasıyla uygulamayı başlatın.
5. TTS sesinin çalışması için cihazda Türkçe metin okuma dili yüklü olmalıdır.

## Komut satırından APK

```bash
./gradlew :app:assembleDebug
```

APK yolu:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## GitHub Actions

`main` dalına yapılan push veya manuel `workflow_dispatch` sonrasında `.github/workflows/android.yml` çalışır. Üretilen APK, Actions çalışmasının **Artifacts** bölümünde `okuma-yolculugu-debug-apk` adıyla bulunur.

## Sonraki geliştirmeler

- DataStore ile cihazlar arası olmayan kalıcı ilerleme kaydı
- Hece ve kelime dersleri
- Yazılış animasyonu
- Ebeveyn paneli ve günlük hedefler
- Önceden kaydedilmiş profesyonel ses dosyaları
- Ses tanıma ile çocuğun okumasını değerlendirme
- İsteğe bağlı karanlık tema ayarı
