
package br.com.ofertacerta

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

private data class Produto(
    val id: Long,
    val nome: String,
    val link: String,
    val precoAlvo: Double
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { OfertaCertaApp() }
    }
}

@Composable
fun OfertaCertaApp() {
    val context = LocalContext.current

    var pesquisa by remember { mutableStateOf("") }
    var nome by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }

    val produtos = remember { mutableStateListOf<Produto>() }

    val azul = Color(0xFF142B50)
    val verde = Color(0xFF20B66B)

    LaunchedEffect(Unit) {
        val salvos = context.getSharedPreferences("oferta_certa", 0)
            .getString("produtos", "[]") ?: "[]"

        try {
            val json = JSONArray(salvos)
            for (i in 0 until json.length()) {
                val item = json.getJSONObject(i)
                produtos.add(
                    Produto(
                        item.getLong("id"),
                        item.getString("nome"),
                        item.getString("link"),
                        item.getDouble("precoAlvo")
                    )
                )
            }
        } catch (_: Exception) {
            mensagem = "Não foi possível carregar os produtos salvos."
        }
    }

    fun salvarProdutos() {
        val json = JSONArray()
        produtos.forEach { produto ->
            json.put(
                JSONObject()
                    .put("id", produto.id)
                    .put("nome", produto.nome)
                    .put("link", produto.link)
                    .put("precoAlvo", produto.precoAlvo)
            )
        }

        context.getSharedPreferences("oferta_certa", 0)
            .edit()
            .putString("produtos", json.toString())
            .apply()
    }

    fun buscarNaLoja(loja: String) {
        if (pesquisa.isBlank()) {
            mensagem = "Digite o produto que deseja pesquisar."
            return
        }

        val termo = Uri.encode(pesquisa.trim())

        val endereco = when (loja) {
            "Mercado Livre" ->
                "https://lista.mercadolivre.com.br/$termo"
            "Amazon" ->
                "https://www.amazon.com.br/s?k=$termo"
            "Shopee" ->
                "https://shopee.com.br/search?keyword=$termo"
            "Google Shopping" ->
                "https://www.google.com/search?tbm=shop&q=$termo"
            else -> return
        }

        try {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(endereco))
            )
        } catch (_: Exception) {
            mensagem = "Não foi possível abrir a loja."
        }
    }

    MaterialTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F6FA)),
            contentPadding = PaddingValues(bottom = 28.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(azul)
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = verde,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            "OFERTA CERTA",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        "Encontre as melhores oportunidades!",
                        color = Color.White,
                        fontSize = 14.sp
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "Buscar ofertas",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = pesquisa,
                        onValueChange = { pesquisa = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ex.: Fone Bluetooth JBL") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(Modifier.height(14.dp))

                    Text(
                        "Escolha onde pesquisar:",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = { buscarNaLoja("Mercado Livre") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = verde)
                    ) {
                        Text("Buscar no Mercado Livre", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { buscarNaLoja("Amazon") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9900))
                    ) {
                        Text("Buscar na Amazon", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { buscarNaLoja("Shopee") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEE4D2D))
                    ) {
                        Text("Buscar na Shopee", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { buscarNaLoja("Google Shopping") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        Text(
                            "Buscar no Google Shopping",
                            color = azul,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        "As pesquisas abrem os sites das lojas.",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            item {
                Column(
                    Modifier.padding(horizontal = 18.dp, vertical = 16.dp)
                ) {
                    Text(
                        "Cadastrar produto",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = azul
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "Cadastre um produto para acompanhar o preço desejado.",
                        color = Color.DarkGray,
                        fontSize = 14.sp
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nome do produto") },
                        singleLine = true
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = link,
                        onValueChange = { link = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Link do produto na loja") },
                        placeholder = { Text("https://...") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = preco,
                        onValueChange = { preco = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Preço desejado (R$)") },
                        placeholder = { Text("Ex.: 199,90") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val valor = preco.trim()
                                .replace(",", ".")
                                .toDoubleOrNull()

                            when {
                                nome.isBlank() ->
                                    mensagem = "Digite o nome do produto."

                                link.isBlank() ||
                                    !(link.startsWith("https://") || link.startsWith("http://")) ->
                                    mensagem = "Informe um link válido começando com http:// ou https://."

                                valor == null || valor <= 0.0 ->
                                    mensagem = "Digite um preço desejado válido."

                                else -> {
                                    produtos.add(
                                        Produto(
                                            System.currentTimeMillis(),
                                            nome.trim(),
                                            link.trim(),
                                            valor
                                        )
                                    )
                                    salvarProdutos()
                                    nome = ""
                                    link = ""
                                    preco = ""
                                    mensagem = "Produto cadastrado e salvo neste celular."
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = verde)
                    ) {
                        Text("Adicionar produto", fontWeight = FontWeight.Bold)
                    }

                    if (mensagem.isNotBlank()) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            mensagem,
                            color = if (mensagem.startsWith("Produto"))
                                verde else Color(0xFFB3261E)
                        )
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 18.dp)) {
                    Text(
                        "Meus produtos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = azul
                    )
                    Spacer(Modifier.height(8.dp))

                    if (produtos.isEmpty()) {
                        Text(
                            "Nenhum produto cadastrado ainda.",
                            color = Color.DarkGray,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            items(
                produtos.filter {
                    it.nome.contains(pesquisa, ignoreCase = true)
                },
                key = { it.id }
            ) { produto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 5.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                produto.nome,
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = azul
                            )

                            IconButton(
                                onClick = {
                                    produtos.removeAll { it.id == produto.id }
                                    salvarProdutos()
                                    mensagem = "Produto removido."
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Remover produto",
                                    tint = Color(0xFFB3261E)
                                )
                            }
                        }

                        Text(
                            "Preço desejado: " +
                                String.format(
                                    Locale("pt", "BR"),
                                    "R$ %.2f",
                                    produto.precoAlvo
                                ),
                            color = verde,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(4.dp))
                        Text(produto.link, color = Color.DarkGray, fontSize = 12.sp)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Produto cadastrado para acompanhamento.",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
