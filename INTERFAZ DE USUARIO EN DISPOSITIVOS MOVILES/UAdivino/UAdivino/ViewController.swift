//
//  ViewController.swift
//  UAdivino
//
//  Created by Master Moviles on 11/10/24.
//

import UIKit

class ViewController: UIViewController {
    let miAdivino = Adivino()
    
    @IBOutlet weak var respuestaLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    @IBAction func botonPulsado(_ sender: Any) {
        
        let respuesta = miAdivino.obtenerRespuesta()
        respuestaLabel.text = respuesta
    }
    
}

