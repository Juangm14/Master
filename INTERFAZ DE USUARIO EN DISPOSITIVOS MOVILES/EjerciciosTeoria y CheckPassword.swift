import Foundation;
 // INTRODUCCION IOS BÁSICO

//Ejercicio 1: Al igual que Int() convierte al tipo entero, Bool() puede convertir a booleano, por ejemplo a partir de las cadenas "true" y "false". Supongamos que tienes una constante let valor="true". Declara una variable b como de tipo booleano y asígnale valor pero convertida a booleano.

let valor = "true"
var b = Bool(valor)
print(b)

//Ejercicio 2: Declara una variable "mensaje" como un String opcional. Usando el if let escribe código que haga que si es distinta a nil la imprima, pero si es nil imprima "está vacía". Ten en cuenta que en Swift las llaves son obligatorias siempre en los condicionales aunque solo haya una instrucción.
var mensaje : String?

if let mensaje = mensaje {
  print(mensaje)
}else{
  print("está vacía")
}

//Ejercicio 3: Cambia este ejemplo por un bucle for en el que se itere usando una variable i con la posición del elemento en la lista, lista[i]. El número de elementos de la lista lo puedes obtener en su propiedad count
var bizcocho = ["huevos", "leche", "harina"]
for (i, bizcochito) in bizcocho.enumerated(){
  print("En la posición \(i) está \(bizcochito)")
}

// Ejercicio 4: Implementa una función filtrar a la que le pases una lista de valores Int y un valor máximo y devuelva una nueva lista con todos los valores que no superan este máximo. El primer parámetro no debe tener etiqueta y el segundo max. Por ejemplo esto devolvería la lista [4 5] (cuidado, swift tiene una función filter que hace esto, pero evidentemente no puedes usarla para este ejercicio)

func filtrar(_ lista: [Int], max: Int) -> [Int]{
  var listaFiltrada = [Int]()

  for i in lista{
    if i <= max{
      listaFiltrada.append(i)
    }
  }
  return listaFiltrada
}

var lista = [10, 4, 5, 7]
var f = filtrar(lista, max:5) 
print(f) //[4,5]


//Ejercicio 5: Crea una clase Persona con un nombre, una edad y una propiedad computable booleana adulto que indique si tiene 18 años o más. Comprueba que el siguiente código funciona con tu clase:

class Persona{
  var nombre : String
  var edad : Int
  var adulto : Bool
  
  init(nombre: String, edad: Int){
      self.nombre = nombre
      self.edad = edad

    if self.edad >= 18 {
      adulto = true
    }else{
      adulto = false
    }
  }
}

var persona = Persona(nombre:"Pepe", edad:20)
//debería imprimir ADULTO! ya que la edad de la persona es >= 18
if persona.adulto {
  print("ADULTO!")
} 


//Ejercicio 6: Crea un tipo enumerado DiaSemana para los días de la semana (lunes, martes,...). Su rawValue será Int. Añádele un método cuantoFalta que devuelva el número de días que faltan para el fin de semana o bien 0 si es sábado o domingo. Al probarlo debería ser algo de este estilo:

enum DiaSemana : Int {
  case lunes = 0, martes = 1, miercoles = 2, jueves = 3, viernes = 4
  func cuantoFalta() -> Int {
    return 5 - self.rawValue
  }
}

var dia = DiaSemana.lunes
print(dia.rawValue) //0
dia = .viernes
print(dia.cuantoFalta()) //1

//EJERCICIOS ADICIONALES SWIFT (PALO,CARTA, MANO)
//ESTÁN INCLUIDOS EN EL JUEGO DE LAS CARTAS.

//CHECKEARPASSWORD
enum ErrorPassword: Error {
  case longitudInsuficiente
  case faltaMayuscula
  case faltaMinuscula
}

func chequearPassword(_ password: String) throws -> Bool {
  guard password.count >= 8 else {
      throw ErrorPassword.longitudInsuficiente
  }

  var tieneMayuscula = false
  var tieneMinuscula = false

  for caracter in password {
      if caracter.isUppercase {
          tieneMayuscula = true
      } else if caracter.isLowercase {
          tieneMinuscula = true
      }

      if tieneMayuscula && tieneMinuscula {
          break
      }
  }

  if !tieneMayuscula {
      throw ErrorPassword.faltaMayuscula
  }

  if !tieneMinuscula {
      throw ErrorPassword.faltaMinuscula
  }

  return true
}

// Ejemplo de uso
let contrasena = "Contraseña123"

do {
  try chequearPassword(contrasena)
  print("La contraseña es válida.")
} catch ErrorPassword.longitudInsuficiente {
  print("Error: La contraseña debe tener al menos 8 caracteres.")
} catch ErrorPassword.faltaMayuscula {
  print("Error: La contraseña debe contener al menos una letra mayúscula.")
} catch ErrorPassword.faltaMinuscula {
  print("Error: La contraseña debe contener al menos una letra minúscula.")
}



func ascendente(a: String, b: String) -> Bool {
  return a < b;    
}

let nombres = ["John", "Ringo", "Paul", "George"]
//let ord = nombres.sorted(by: ascendente)
let ord = nombres.sorted(by: {
  return $0 < $1;    
})

print(ord)

protocol Amigable {
  func saludar()-> String
}

class MiClase : Amigable {
  func saludar() -> String {
    return "Hola soy yo"
  }
}

enum ErrorImpresora: Error {
  case sinPapel
  case sinTinta(color: String)
  case enLlamas
}

func usarImpresora() throws {
  throw ErrorImpresora.sinTinta(color: "rojo")
}

func otraFuncion() {
  do{ 
    try usarImpresora()
  }catch ErrorImpresora.enLlamas{
    print("Fuego!!!")
  }catch ErrorImpresora.sinTinta(let color){
    print("No queda \(color)")
  }catch{
    print(error)
   }
}

otraFuncion()


//struct Punto2D {
class Punto2D {
  var x,y: Double
  func distancia() -> Double{
    return sqrt(x*x + y*y)
  }
  init(x:Double, y:Double){
    self.x = x
    self.y = y
  }
}

var p = Punto2D(x: 1.0, y:1.0)
var p_2 = p
p.x = 2.0
print(p.distancia())
print(p_2.distancia())


