//
//  ViewController.swift
//  Pioneras
//
//  Created by Master Moviles on 31/10/24.
//

import UIKit

class ViewController: UIViewController {
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    @IBAction func retornoDeSecundaria(segue: UIStoryboardSegue) {
        
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let secundarioVC = segue.destination as? SecundarioViewController {
            secundarioVC.nombreAsset = "\(segue.identifier ?? "")_bio"
        }
    }

}
