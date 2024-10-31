//
//  DetalleViewController.swift
//  Marvel
//
//  Created by Master Moviles on 25/10/24.
//

import UIKit
import Marvelous

class DetalleViewController : UIViewController {
    
    @IBOutlet weak var nombreText: UITextField!
    @IBOutlet weak var biografiaText: UITextView!
    @IBOutlet weak var imagenPersonaje: UIImageView!
    
    var personaje : RCCharacterObject?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mostrarPersonaje()
    }
    
    func mostrarPersonaje(){
        guard let personaje = personaje else { return }
        nombreText.text = personaje.name
        biografiaText.text = personaje.bio
        
        let colaBackground = OperationQueue()
        colaBackground.addOperation {
            //SUPONIENDO que la variable con el personaje se llama "personaje"
            if let thumb = personaje.thumbnail {
              //portrait_uncanny es 300x450px. Puedes cambiarlo por otro tamaño si prefieres
              let url = "\(thumb.basePath!)/portrait_uncanny.\(thumb.extension!)"
              //cambiamos la URL por https://. Necesario en iOS
              let urlHttps = url.replacingOccurrences(of: "http", with: "https")
                if let urlFinal = URL(string:urlHttps) {
                    do {
                       let datos = try Data(contentsOf:urlFinal)
                        if let img = UIImage(data: datos) {
                            OperationQueue.main.addOperation {
                                //suponiendo que el outlet de la imagen se llama "miImagen"
                                self.imagenPersonaje.image = img
                            }
                        }
                    }
                    catch {
                    }
                }
            }
        }
    }
}
