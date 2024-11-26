function validar(){
    let usuario = document.getElementById('usuario')
    let password = document.getElementById('password')
    let errorUsuario = document.getElementById('errorusuario')
    let errorPassword = document.getElementById('errorpassword')

    var error = false;

    errorUsuario.style.display = "none";
    errorPassword.style.display = "none";

    if(usuario.value == ''){
        errorUsuario.style.display = "block";
        errorusuario.innerText = 'El campo usuario es requerido.';
        errorUsuario.style.display = "block";
        error = true;
    }

    if(password.value == ''){
        errorPassword.innerText = 'El campo contraseña es requerido.';
        errorPassword.style.display = "block";
        error = true;
    }else if(password.value.length < 3){
        errorPassword.innerText = 'El password debe de tener al menos 3 caracteres';
        errorPassword.style.display = "block";
        error = true;
    }

    if(!error){
        alert("Validación correcta.")
        error = true;
    }
}