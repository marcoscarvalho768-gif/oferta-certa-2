
package br.com.ofertacerta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OfertaCertaApp()
        }
    }
}

@Composable
fun OfertaCertaApp() {
    var pesquisa by remember { mutableStateOf("") }

    val lojas = listOf(
        "Amazon",
        "Mercado Livre",
        "Shopee",
        "Magazine Luiza"
    )

    val azul = Color(0xFF142B50)
    val verde = Color(0xFF20B66B)

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F6FA))
        ) {
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

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        "OFERTA CERTA",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    "Encontre os melhores preços!",
                    color = Color.White,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = pesquisa,
                    onValueChange = { pesquisa = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Qual produto você procura?")
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Pesquisar")
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Text(
                    "Lojas monitoradas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = azul
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(lojas.filter {
                        it.contains(pesquisa, ignoreCase = true) ||
                            pesquisa.isBlank()
                    }) { loja ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    loja,
                                    modifier = Modifier.weight(1f),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )

                                Text(
                                    "Em breve",
                                    color = verde,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Acompanhe os preços",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = azul
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Em breve você poderá acompanhar produtos, " +
                        "histórico de preços e receber alertas de ofertas.",
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}
