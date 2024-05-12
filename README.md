# Sentiment Analysis / Duygu Analizi

:speech_balloon: Bilgi Notu :speech_balloon:

* Bu proje, insanlarin duygularini analiz etmeyi ve bu analize dayanarak tavsiyelerde bulunmayi amaclamaktadir.

#

:electron: Kullanilan Teknolojiler :electron:

* Android Studio (Surum: Hedgehog | 2023.1.1 Patch 2)

* OpenCV (Surum: 4.9.0-android-sdk)

* TensorFlow Lite (Surum: 0.1.0)

* Gemini API (Surum: 0.5.0)

* Google Services (Surum: 4.4.1)

* Firebase Auth (Surum: 23.0.0)

* Firebase Firestore (Surum: 25.0.0)

#

:fleur_de_lis: Projeyi Indirme :fleur_de_lis:

* GitHub'tan projenin Repository'sine gidin.

* <code>Code</code> dugmesine tiklayin ve <code>HTTPS</code> sekmesini secin.

* Proje baglantisini kopyalayin.

* Android Studio baslangic menusunden <code>Get from VCS</code> secenegine tiklayin.

* <code>Version Control</code> bolumunden <code>Git</code> secenegini secin.

* <code>URL</code> bolumune kopyaladiginiz baglantiyi yapistirin.

* <code>Directory</code> bolumune projenin bilgisayarinizda hangi konumda olmasini istiyorsaniz oranin uzantisini yazin. <code>C:\AndroidStudioProjects\sentiment_analysis</code>

* Projeyi Android Studio'da acmak icin, indirdiginiz klasordeki <code>SentimentAnalysis</code> dosyasini secin.

* Proje acildiginda bagimliliklar yuklenecektir.

* Yuklendikten sonra projeyi derleyip APK dosyasini telefonunuza atarak uygulamayi kullanmaya baslayabilirsiniz.

#

:boom: Hata Cozumleri :boom:

* Eger projeyi derlerken <code>SDK location not found. Define a valid SDK location with an ANDROID_HOME environment variable or by setting the sdk.dir path in your project's local properties file at 'C:\AndroidStudioProjects\sentiment_analysis\SentimentAnalysis\local.properties'.</code> hatasiyla karsilastiysaniz, Android Studio <code>local.properties</code> dosyasini olusturmamis demektir. Bunu kendiniz olusturmalisiniz.

* Android Studio'da <code>Settings</code> butonuna tiklayin. Acilan panelde <code>Project Structure</code> butonuna tiklayin. <code>SDK Location</code> kismina gelin. Burada SDK uzantisi olmalidir. <code>C:\Users\kullanici_adiniz\AppData\Local\Android\Sdk</code> tamam'a tikladiginizda Android Studio gerekli degisiklikleri yapacaktir.

* Eger yukaridaki yontem ise yaramadiysa, projenin konumuna gidin. <code>C:\AndroidStudioProjects\sentiment_analysis\SentimentAnalysis</code> dizinine yeni bir metin dosyasi olusturun. Metin dosyasini <code>local.properties</code> adiyla kaydedin. Dosyayi duzenleyin ve yerel yapilandirma ayarlarini icine ekleyin. Bu, Android SDK'nin konumunu belirten bir satir olacaktir. <code>sdk.dir=/Users/kullanici_adiniz/Library/Android/sdk</code>
  
#

:vibration_mode: Desteklenen Android Surumleri :vibration_mode:

* Minimum: <code>'API 24' ("Nougat"; Android 7.0)</code>

* Maximum: <code>'API 34' ("UpsideDownCake"; Android 14.0)</code>

#

:maple_leaf: API Kullanimlari :maple_leaf:

* <code>Gemini API</code> kullanabilmek icin oncelikle bir Gemini hesabi olusturarak API anahtari alin. Aldiginiz anahtari <code>C:\AndroidStudioProjects\sentiment_analysis\SentimentAnalysis\app\src\main\java\com\example\sentimentanalysis</code> dizininde olan <code>BuildConfig.java</code> sinifindaki <code>static String apikey</code> degiskenine atayin. <code>public static String apikey = "API anahtariniz";</code>

* <code>Firebase API</code> kullanabilmek icin oncelikle bir Firebase hesabi olusturarak projeye uygun bir bulut hesabi acin. Hesap acma adimlarini takip ederken <code>google-services.json</code> dosyasini indirin. Indirdiginiz dosyayi <code>C:\AndroidStudioProjects\sentiment_analysis\SentimentAnalysis\app</code> dizinine atin.