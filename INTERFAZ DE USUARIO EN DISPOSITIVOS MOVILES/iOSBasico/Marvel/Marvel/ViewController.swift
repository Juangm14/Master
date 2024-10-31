//
//  ViewController.swift
//  Marvel
//
//  Created by Otto Colomina Pardo on 31/10/22.
//

import UIKit
import Marvelous

class ViewController: UIViewController {

    func mostrarPersonajes(comienzanPor cadena : String) {
            let marvelAPI = RCMarvelAPI()
            //PUEDES CAMBIAR ESTO PARA PONER TUS CLAVES
            marvelAPI.publicKey = "a6927e7e15930110aade56ef90244f6d"
            marvelAPI.privateKey = "487b621fc3c0d6f128b468ba86c99c508f24d357"
            let filtro = RCCharacterFilter()
            filtro.nameStartsWith = cadena
            filtro.limit = 50
            marvelAPI.characters(by: filtro) {
                resultados, info, error in
                if let personajes = resultados as? [RCCharacterObject] {
                    for personaje in personajes {
                        print(personaje.name ?? "")
                    }
                    print("Hay \(personajes.count) personajes")
                }
            }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        mostrarPersonajes(comienzanPor: "Spider")
    }


}

