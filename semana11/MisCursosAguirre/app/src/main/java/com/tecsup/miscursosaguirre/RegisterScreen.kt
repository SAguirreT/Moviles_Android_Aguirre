
package com.tecsup.miscursosaguirre

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

// Colores iguales a los utilizados en LoginScreen.
private val FondoRegistro = Color(0xFFF8F4FF)
private val MoradoRegistro = Color(0xFF7956B8)

// Pantalla para crear una cuenta mediante Firebase Authentication.
@Composable
fun RegisterScreen(
    onRegistroExitoso: () -> Unit,
    onIrLogin: () -> Unit
) {
    // Variables que almacenan los datos del formulario.
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    // Instancia de Firebase para registrar usuarios.
    val auth = remember { FirebaseAuth.getInstance() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoRegistro)
            .imePadding()
    ) {
        // Encabezado pequeño que se mantiene en la parte superior.
        Text(
            text = "Sheila Aguirre",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF79668D),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp)
        )

        // Formulario centrado como en la captura del profesor.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título principal de la pantalla.
            Text(
                text = "Crear cuenta",
                fontSize = 23.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF292333)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Primer campo: correo electrónico.
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
                    focusedBorderColor = MoradoRegistro
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Segundo campo: contraseña.
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
                    focusedBorderColor = MoradoRegistro
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tercer campo: confirmación de contraseña.
            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = {
                    confirmarContrasena = it
                    mensaje = ""
                },
                label = {
                    Text("Confirmar contraseña")
                },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(6.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = MoradoRegistro
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón que valida los datos y registra la cuenta.
            Button(
                onClick = {
                    when {
                        // Verifica que todos los campos estén completos.
                        correo.isBlank() ||
                                contrasena.isBlank() ||
                                confirmarContrasena.isBlank() -> {
                            mensaje = "Completa todos los campos"
                        }

                        // Comprueba que las contraseñas coincidan.
                        contrasena != confirmarContrasena -> {
                            mensaje = "Las contraseñas no coinciden"
                        }

                        // Firebase exige una contraseña de al menos 6 caracteres.
                        contrasena.length < 6 -> {
                            mensaje = "La contraseña debe tener al menos 6 caracteres"
                        }

                        else -> {
                            cargando = true
                            mensaje = ""

                            // Crea el usuario en Firebase Authentication.
                            auth.createUserWithEmailAndPassword(
                                correo.trim(),
                                contrasena
                            ).addOnCompleteListener { tarea ->
                                cargando = false

                                if (tarea.isSuccessful) {
                                    // Abre la bienvenida después del registro.
                                    onRegistroExitoso()
                                } else {
                                    // Muestra el error cuando el registro falla.
                                    mensaje =
                                        tarea.exception?.localizedMessage
                                            ?: "No se pudo crear la cuenta"
                                }
                            }
                        }
                    }
                },
                enabled = !cargando,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MoradoRegistro
                ),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = if (cargando) {
                        "Registrando..."
                    } else {
                        "Registrarse"
                    },
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Enlace para regresar a la pantalla de inicio de sesión.
            TextButton(
                onClick = onIrLogin
            ) {
                Text(
                    text = "← Volver al login",
                    color = MoradoRegistro
                )
            }

            // Mensajes de error o validación del formulario.
            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
            }
        }
    }
}
