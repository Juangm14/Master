class Mano {
    
    var cartas : [Carta] = []
    
    var tamaño : Int{
        return cartas.count
    }
    
    func addCarta(_ carta: Carta){
        cartas.append(carta)
    }
    
    func getCarta(at posicion: Int) -> Carta? {
        if posicion >= 0 && posicion < tamaño{
            return cartas[posicion]
        }else{
            return nil
        }
    }
}
