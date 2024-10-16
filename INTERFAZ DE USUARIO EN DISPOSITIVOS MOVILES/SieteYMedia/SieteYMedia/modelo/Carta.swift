class Carta {
    var valor: Int
    var palo: Palo
    
    init?(valor: Int, palo: Palo) {
        if(valor < 1 || valor > 12 || valor == 8 || valor == 9){
            return nil
        }
        self.valor = valor
        self.palo = palo
    }
    
    func descripcion() -> String {
        return "el \(String(self.valor)) de \(self.palo.rawValue)"
    }
}
