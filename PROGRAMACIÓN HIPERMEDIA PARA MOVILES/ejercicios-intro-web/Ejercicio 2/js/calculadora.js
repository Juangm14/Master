function obtenerOperadores(){
    let op1 = document.getElementById('operando1').value;
    let op2 = document.getElementById('operando2').value;

    if(op1 == '' || op2 == ''){
        alert('Debe de rellenar todos los operandos.')
        return
    }

    return [parseFloat(op1), parseFloat(op2)]
}

function sumar(){
    let operandos = obtenerOperadores()
    if (operandos != null) {
        document.getElementById('resultado').innerHTML = parseFloat(operandos[0]) + parseFloat(operandos[1]);
    }
}

function restar(){
    let operandos = obtenerOperadores()
    if (operandos != null) {
        document.getElementById('resultado').innerHTML =parseFloat(operandos[0]) - parseFloat(operandos[1]);
    }
}

function multiplicar(){
    let operandos = obtenerOperadores()
    if (operandos != null) {
        document.getElementById('resultado').innerHTML = parseFloat(operandos[0]) * parseFloat(operandos[1]);
    }
}

function dividir(){
    let operandos = obtenerOperadores()
    if (operandos != null) {
        if (operandos[1] == 0) {
            alert('Error: División por cero no permitida.');
            document.getElementById('resultado').innerHTML = 'Error';
        }else{
            document.getElementById('resultado').innerHTML = parseFloat(operandos[0]) / parseFloat(operandos[1]);
        }
    }
}