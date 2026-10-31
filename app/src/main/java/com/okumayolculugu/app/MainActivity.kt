package com.okumayolculugu.app

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.okumayolculugu.app.ui.theme.OkumaYolculuguTheme
import java.util.Locale

private data class Letter(val upper: String, val lower: String, val word: String, val emoji: String)

private val letters = listOf(
    Letter("A", "a", "Arı", "🐝"), Letter("B", "b", "Balık", "🐟"), Letter("C", "c", "Ceviz", "🌰"),
    Letter("Ç", "ç", "Çilek", "🍓"), Letter("D", "d", "Dondurma", "🍦"), Letter("E", "e", "Elma", "🍎"),
    Letter("F", "f", "Fil", "🐘"), Letter("G", "g", "Güneş", "☀️"), Letter("Ğ", "ğ", "Dağ", "⛰️"),
    Letter("H", "h", "Havuç", "🥕"), Letter("I", "ı", "Ihlamur", "🌿"), Letter("İ", "i", "İncir", "🫐"),
    Letter("J", "j", "Jelibon", "🍬"), Letter("K", "k", "Kedi", "🐱"), Letter("L", "l", "Limon", "🍋"),
    Letter("M", "m", "Muz", "🍌"), Letter("N", "n", "Nar", "🍎"), Letter("O", "o", "Orman", "🌲"),
    Letter("Ö", "ö", "Ördek", "🦆"), Letter("P", "p", "Penguen", "🐧"), Letter("R", "r", "Renk", "🎨"),
    Letter("S", "s", "Su", "💧"), Letter("Ş", "ş", "Şemsiye", "☂️"), Letter("T", "t", "Top", "⚽"),
    Letter("U", "u", "Uçak", "✈️"), Letter("Ü", "ü", "Üzüm", "🍇"), Letter("V", "v", "Vazo", "🏺"),
    Letter("Y", "y", "Yıldız", "⭐"), Letter("Z", "z", "Zürafa", "🦒")
)

private val Purple = Color(0xFF7357E8)
private val Pink = Color(0xFFFF8FA3)
private val Cream = Color(0xFFFFF9F5)
private val Mint = Color(0xFFDDF6ED)
private val Peach = Color(0xFFFFE4D6)

class MainActivity : ComponentActivity() {
    private lateinit var tts: TextToSpeech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this) { tts.language = Locale("tr", "TR") }
        setContent { OkumaYolculuguTheme { Surface(color = Cream) { App(tts) } } }
    }
    override fun onDestroy() { if (::tts.isInitialized) tts.shutdown(); super.onDestroy() }
}

@Composable
private fun App(tts: TextToSpeech) {
    var screen by remember { mutableStateOf("home") }
    var selected by remember { mutableIntStateOf(0) }
    var completed by remember { mutableStateOf(setOf<Int>()) }
    when (screen) {
        "home" -> HomeScreen(completed, onStart = { screen = "letters" }, onPractice = { screen = "practice" })
        "letters" -> LetterListScreen(completed, onBack = { screen = "home" }, onSelect = { selected = it; screen = "detail" })
        "detail" -> LetterDetailScreen(letters[selected], selected, onBack = { screen = "letters" }, onSpeak = { speak(tts, letters[selected].upper) }, onComplete = { completed = completed + selected; screen = "letters" })
        "practice" -> PracticeScreen(onBack = { screen = "home" }, onSpeak = { speak(tts, it) })
    }
}

private fun speak(tts: TextToSpeech, text: String) { tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "okuma-$text") }

@Composable
private fun HomeScreen(completed: Set<Int>, onStart: () -> Unit, onPractice: () -> Unit) {
    val progress = completed.size / letters.size.toFloat()
    Column(Modifier.fillMaxSize().background(Cream).padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(24.dp))
        Text("Okuma Yolculuğu", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Purple)
        Text("Harfleri keşfet, kelimeleri oku!", color = Color(0xFF675D70), fontSize = 16.sp)
        Spacer(Modifier.height(22.dp))
        Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Peach), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(22.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🌈", fontSize = 44.sp); Spacer(Modifier.size(14.dp));
                    Column { Text("Bugünkü hedefin", fontWeight = FontWeight.Bold, fontSize = 20.sp); Text("3 harf öğrenmek", color = Color(0xFF675D70)) }
                }
                Spacer(Modifier.height(18.dp)); LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape), color = Purple, trackColor = Color.White)
                Spacer(Modifier.height(8.dp)); Text("${completed.size} / ${letters.size} harf tamamlandı", color = Color(0xFF675D70))
            }
        }
        Spacer(Modifier.height(22.dp))
        ActionCard("Alfabeyi Öğren", "Harfleri dinle ve örnek kelimeleri keşfet", "🔤", Purple, onStart)
        Spacer(Modifier.height(14.dp)); ActionCard("Mini Alıştırma", "Öğrendiklerini eğlenceli bir oyunla pekiştir", "🎯", Pink, onPractice)
        Spacer(Modifier.weight(1f)); Text("Reklamsız • Çevrimdışı • Güvenli", color = Color(0xFF8B8190), fontSize = 13.sp)
    }
}

@Composable
private fun ActionCard(title: String, subtitle: String, icon: String, color: Color, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(58.dp).clip(CircleShape).background(color.copy(alpha = .15f)), contentAlignment = Alignment.Center) { Text(icon, fontSize = 30.sp) }
            Spacer(Modifier.size(16.dp)); Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.Bold, fontSize = 19.sp); Text(subtitle, color = Color(0xFF766C7A), fontSize = 13.sp) }; Text("›", fontSize = 30.sp, color = color)
        }
    }
}

@Composable
private fun LetterListScreen(completed: Set<Int>, onBack: () -> Unit, onSelect: (Int) -> Unit) {
    Column(Modifier.fillMaxSize().background(Cream).padding(horizontal = 18.dp)) {
        Header("Alfabe", onBack); Text("Bir harfe dokun ve keşfet!", color = Color(0xFF675D70), modifier = Modifier.padding(bottom = 12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            itemsIndexed(letters) { index, letter ->
                val unlocked = index == 0 || completed.contains(index - 1)
                Card(Modifier.fillMaxWidth().clickable(enabled = unlocked) { onSelect(index) }, shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = if (unlocked) Color.White else Color(0xFFEFECEF))) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(52.dp).clip(CircleShape).background(if (completed.contains(index)) Mint else Peach), contentAlignment = Alignment.Center) { Text(letter.upper, fontWeight = FontWeight.ExtraBold, fontSize = 25.sp, color = Purple) }
                        Spacer(Modifier.size(14.dp)); Column(Modifier.weight(1f)) { Text("${letter.upper} ${letter.lower}", fontWeight = FontWeight.Bold, fontSize = 19.sp); Text(letter.word, color = Color(0xFF766C7A)) }
                        if (completed.contains(index)) Icon(Icons.Default.CheckCircle, "Tamamlandı", tint = Color(0xFF2EAF7D)) else if (!unlocked) Icon(Icons.Default.Lock, "Kilitli", tint = Color.Gray) else Text(letter.emoji, fontSize = 26.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(title: String, onBack: () -> Unit) { Row(Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Geri") }; Text(title, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Purple) } }

@Composable
private fun LetterDetailScreen(letter: Letter, index: Int, onBack: () -> Unit, onSpeak: () -> Unit, onComplete: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Cream).padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Header("Harf ${index + 1}", onBack); Spacer(Modifier.height(12.dp))
        Card(shape = RoundedCornerShape(32.dp), colors = CardDefaults.cardColors(containerColor = Peach), modifier = Modifier.fillMaxWidth()) { Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) { Text(letter.emoji, fontSize = 68.sp); Text(letter.upper, fontSize = 88.sp, fontWeight = FontWeight.ExtraBold, color = Purple); Text(letter.lower, fontSize = 48.sp, color = Pink, fontWeight = FontWeight.Bold) } }
        Spacer(Modifier.height(18.dp)); Text("${letter.upper} harfi", fontWeight = FontWeight.Bold, fontSize = 23.sp); Text("${letter.word} kelimesinin başında bulunur.", color = Color(0xFF675D70)); Spacer(Modifier.height(20.dp))
        Button(onClick = onSpeak, colors = ButtonDefaults.buttonColors(containerColor = Purple), shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth().height(56.dp)) { Icon(Icons.Default.VolumeUp, "Dinle"); Spacer(Modifier.size(8.dp)); Text("Harf sesini dinle", fontSize = 17.sp) }
        Spacer(Modifier.height(12.dp)); Text("Örnek kelime: ${letter.word} ${letter.emoji}", fontSize = 19.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.weight(1f)); Button(onClick = onComplete, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2EAF7D)), shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth().height(58.dp)) { Icon(Icons.Default.Star, "Tamamla"); Spacer(Modifier.size(8.dp)); Text("Öğrendim! Sonraki harfe geç", fontSize = 16.sp) }
    }
}

@Composable
private fun PracticeScreen(onBack: () -> Unit, onSpeak: (String) -> Unit) {
    var answer by remember { mutableStateOf<String?>(null) }; val options = listOf("A", "M", "S"); val correct = "M"
    Column(Modifier.fillMaxSize().background(Cream).padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Header("Mini Alıştırma", onBack); Spacer(Modifier.height(28.dp)); Text("Bu kelime hangi harfle başlıyor?", fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center); Spacer(Modifier.height(24.dp)); Text("🐒", fontSize = 78.sp); Text("Muz", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Purple); Spacer(Modifier.height(28.dp))
        options.forEach { option -> Button(onClick = { answer = option; onSpeak(option) }, colors = ButtonDefaults.buttonColors(containerColor = if (answer == option) { if (option == correct) Color(0xFF2EAF7D) else Pink } else Color.White, contentColor = if (answer == option) Color.White else Purple), shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp).height(54.dp)) { Text(option, fontSize = 22.sp, fontWeight = FontWeight.Bold) } }
        AnimatedVisibility(answer != null) { Text(if (answer == correct) "Aferin! Doğru cevap ⭐" else "Tekrar deneyebilirsin 💪", color = if (answer == correct) Color(0xFF2EAF7D) else Pink, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(20.dp)) }
    }
}
