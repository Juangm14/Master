class Baraja {
    var cartas: [Carta] = []
    
    
    init(){
        for palo in [Palo.bastos, Palo.espadas, Palo.copas, Palo.oros] {
            for valor in 1...12 {
                if valor != 8 && valor != 9 {
                    if let carta = Carta(valor: valor, palo: palo){
                        cartas.append(carta)
                    }
                }
            }
        }
    }
    
    func repartirCarta() -> Carta? {
        return cartas.popLast()
    }
    
    func barajar(){
        cartas.shuffle()
    }
}
