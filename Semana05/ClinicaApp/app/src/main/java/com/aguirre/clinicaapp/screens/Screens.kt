package com.aguirre.clinicaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ChevronRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.aguirre.clinicaapp.model.Doctor
import com.aguirre.clinicaapp.model.DoctorRepository
import com.aguirre.clinicaapp.navigation.Screen

val PurpleDark = Color(0xFF1D0036)
val PurpleMid = Color(0xFF7B2CBF)
val PurpleLight = Color(0xFFE0AAFF)
val BackgroundLilac = Color(0xFFF8F5FE)

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize().background(BackgroundLilac), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.fillMaxWidth(0.9f).border(1.dp, PurpleLight, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.LocalHospital, null, tint = PurpleMid, modifier = Modifier.size(48.dp))
                Text("Portal Clínica App", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = PurpleDark)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo / CMP") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Login.route) { inclusive = true } } },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleMid)
                ) { Text("INICIAR SESIÓN", color = Color.White) }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(PurpleDark, PurpleMid))).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bienvenida,", color = Color.White, fontSize = 18.sp)
        Text("Dra. Sheila Aguirre", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 26.sp)
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { navController.navigate(Screen.List.route) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) { Text("Ver Directorio Médico", color = PurpleDark, fontWeight = FontWeight.Bold) }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { navController.navigate(Screen.Profile.route) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurpleLight)
        ) { Text("Mi Perfil Clínico", color = PurpleDark, fontWeight = FontWeight.Bold) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Directorio Médico", color = Color.White) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PurpleMid)) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(DoctorRepository.doctors) { doctor ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { navController.navigate(Screen.Detail.createRoute(doctor.id)) },
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(model = doctor.avatarUrl, contentDescription = null, modifier = Modifier.size(48.dp).clip(CircleShape))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(doctor.name, fontWeight = FontWeight.Bold, color = PurpleDark)
                            Text(doctor.specialty, color = Color.Gray, fontSize = 12.sp)
                        }
                        Icon(Icons.AutoMirrored.Filled.ChevronRight, null, tint = PurpleMid)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, doctor: Doctor) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(doctor.name, color = Color.White) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PurpleMid)) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(model = doctor.avatarUrl, contentDescription = null, modifier = Modifier.size(100.dp).clip(CircleShape))
            Spacer(modifier = Modifier.height(16.dp))
            Text(doctor.name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = PurpleDark)
            Text(doctor.specialty, color = PurpleMid, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("CMP: ${doctor.cmpCode}", fontWeight = FontWeight.Bold)
                    Text("Email: ${doctor.email}")
                    Text("Ubicación: ${doctor.location}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(doctor.bio, color = Color.Gray)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi Perfil", color = Color.White) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PurpleMid)) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(model = "https://randomuser.me/api/portraits/women/44.jpg", contentDescription = null, modifier = Modifier.size(100.dp).clip(CircleShape))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Dra. Sheila Aguirre", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = PurpleDark)
            Text("sheila.aguirre@clinica.pe", color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate(Screen.Login.route) { popUpTo(0) } },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) { Text("Cerrar Sesión") }
        }
    }
}