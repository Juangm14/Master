function limpiar(){
    document.getElementById('formula').value = ''
}

function setValue(valor){
    debugger;
    let formula = document.getElementById('formula');

    if (formula.value == "ERROR") {
        formula.value = ''
    }

    formula.value += valor
}

function calcular(){
    try {
        if(document.getElementById('formula').value == ''){
            document.getElementById('formula').value = "ERROR"
        }else{
            document.getElementById('formula').value = eval(document.getElementById('formula').value);
        }
    } catch (e) {
        document.getElementById('formula').value = "ERROR"
    }
}