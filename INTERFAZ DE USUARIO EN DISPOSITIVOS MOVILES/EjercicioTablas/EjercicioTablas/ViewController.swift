//
//  ViewController.swift
//  EjercicioTablas
//
//  Created by Master Moviles on 18/10/24.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var miTabla: UITableView!
    @IBOutlet weak var miTexto: UITextField!
    
    let miDS = MiDatasource()
    let miDelegate = MiDelegate()

    override func viewDidLoad() {
        super.viewDidLoad()
        
        miTabla.dataSource = miDS
        miTabla.delegate = miDelegate
        
        //miTabla.setEditing(true, animated: true)
        
    }


    @IBAction func insertarTexto(_ sender: Any) {
        
    }
}

