//
//  Adivino.swift
//  UAdivino
//
//  Created by Master Moviles on 11/10/24.
//

class Adivino {
 
    let respuestas = ["Si","No","Ni de casualidad","Pues claro!","Lo dudabas?"]
    
    func obtenerRespuesta() -> String {
        return self.respuestas[Int.random(in:0..<respuestas.count)]
    }
}
