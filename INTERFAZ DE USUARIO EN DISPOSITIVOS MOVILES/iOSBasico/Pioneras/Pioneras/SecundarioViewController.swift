//
//  SecundarioViewController.swift
//  Pioneras
//
//  Created by Master Moviles on 31/10/24.
//

import UIKit

class SecundarioViewController: UIViewController {

    @IBOutlet weak var Biografia: UITextView!
    
    var nombreAsset: String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        var texto = ""
        let asset = NSDataAsset(name: nombreAsset)
        if let data = asset?.data {
            texto = String(data: data, encoding: .utf8) ?? ""
            self.Biografia.text = texto
        }
    }
}
