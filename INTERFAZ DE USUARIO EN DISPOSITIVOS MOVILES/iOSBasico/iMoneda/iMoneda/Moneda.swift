//
//  Moneda.swift
//  iMoneda
//
//  Created by Master Moviles on 11/10/24.
//

enum Tirada {
    case cara, cruz
}

class Moneda {
    
    func lanzarMoneda() -> Tirada {
        let tirada = Bool.random()
        
        if tirada {
            return Tirada.cara
        }else{
            return Tirada.cruz
        }
    }
}
