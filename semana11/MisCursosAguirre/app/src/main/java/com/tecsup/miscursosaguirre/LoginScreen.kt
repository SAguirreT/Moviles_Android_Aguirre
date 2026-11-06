
package com.tecsup.miscursosaguirre

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

// Colores personalizados de la pantalla.
private val FondoLila = Color(0xFFF8F4FF)
private val MoradoBoton = Color(0xFF7956B8)

// Pantalla para iniciar sesión con una cuenta registrada en Firebase.
@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    onIrRegistro: () -> Unit
) {
    // Variables que almacenan los datos ingresados por el usuario.
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    // Instancia de Firebase Authentication.
    val auth = remember { FirebaseAuth.getInstance() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoLila)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Encabezado pequeño con mi nombre
            Text(
                text = "Sheila Aguirre",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF79668D)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Encabezado del laboratorio.
            Text(
                text = "Programación Móvil - Tecsup",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Logo oficial de Tecsup guardado en drawable.
            Image(
                painter = painterResource(
                    id = R.drawable.logo_tecsup
                ),
                contentDescription = "Logo de Tecsup",
                modifier = Modifier
                    .width(150.dp)
                    .height(65.dp),
                contentScale = ContentScale.Fit
            )

            // Espacio para mantener el formulario debajo del logo.
            Spacer(modifier = Modifier.height(95.dp))

            // Título principal de la pantalla.
            Text(
                text = "Iniciar Sesión",
                fontSize = 23.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF292333)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Campo para ingresar el correo electrónico.
            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    mensaje = ""
                },
                label = {
                    Text("Correo electrónico")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true,
                shape = RoundedCornerShape(6.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = MoradoBoton
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo para ingresar la contraseña de forma protegida.
            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    mensaje = ""
                },
                label = {
                    Text("Contraseña")
                },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(6.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = MoradoBoton
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón que inicia sesión utilizando Firebase Authentication.
            Button(
                enabled = !cargando,
                onClick = {
                    // Verifica que los campos no estén vacíos.
                    if (correo.isBlank() || contrasena.isBlank()) {
                        mensaje = "Ingresa tu correo y contraseña"
                    } else {
                        cargando = true
                        mensaje = ""

                        // Firebase comprueba las credenciales ingresadas.
                        auth.signInWithEmailAndPassword(
                            correo.trim(),
                            contrasena
                        ).addOnCompleteListener { tarea ->
                            cargando = false

                            if (tarea.isSuccessful) {
                                // Abre la pantalla de bienvenida.
                                onLoginExitoso()
                            } else {
                                // Informa cuando los datos no son válidos.
                                mensaje =
                                    "No se pudo iniciar sesión. Revisa tus datos."
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MoradoBoton
                ),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = if (cargando) {
                        "Ingresando..."
                    } else {
                        "Ingresar"
                    },
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Enlace que permite abrir el formulario de registro.
            TextButton(
                onClick = onIrRegistro
            ) {
                Text(
                    text = "¿No tienes cuenta? Regístrate aquí",
                    color = MoradoBoton
                )
            }

            // Muestra los mensajes de validación o error.
            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
            }

            // Espacio inferior para separar el formulario del pie.
            Spacer(modifier = Modifier.height(80.dp))

            // Pie de página con mi nombre
            Text(
                text = "Sheila Aguirre - Tecsup",
                fontSize = 11.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
