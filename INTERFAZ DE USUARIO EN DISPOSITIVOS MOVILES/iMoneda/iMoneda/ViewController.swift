//
//  ViewController.swift
//  iMoneda
//
//  Created by Master Moviles on 11/10/24.
//

import UIKit

class ViewController: UIViewController {
    let miMoneda = Moneda()

    @IBOutlet weak var imageMonedaView: UIImageView!
    @IBOutlet weak var respuestaLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    @IBAction func lanzarMoneda(_ sender: Any) {
        
        
        let respuesta = miMoneda.lanzarMoneda()
        var ladoMoneda = ""
        
        if respuesta == Tirada.cara{
            ladoMoneda = "cara"
        }
        else{
            ladoMoneda = "cruz"
        }
        
        respuestaLabel.text = "¡Ha salido " + ladoMoneda + "!"
        let imagenCara = UIImage(named:ladoMoneda)
        self.imageMonedaView.image = imagenCara
    }
                
}

