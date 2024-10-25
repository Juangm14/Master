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
    
    var personaje : RCCharacterObject?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mostrarPersonaje()
    }
    
    func mostrarPersonaje(){
        guard let personaje = personaje else { return }
        nombreText.text = personaje.name
        biografiaText.text = personaje.bio
    }
}
