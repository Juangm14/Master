//
//  ViewController.swift
//  Vistas
//
//  Created by Master Moviles on 31/10/24.
//

import UIKit

class ViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {

    
    @IBOutlet weak var sliderValue: UILabel!
    @IBOutlet weak var slider: UISlider!
    @IBOutlet weak var textView: UITextView!
    @IBOutlet weak var textoMostrar: UITextField!
    @IBOutlet weak var pickerView: UIPickerView!
    
    let destinos = ["Saturno", "M13", "Nebulosa de Orión"]
    
    override func viewDidLoad() {
        super.viewDidLoad()

        slider.minimumValue = 0
        slider.maximumValue = 100
        slider.value = 0
        sliderValue.text = "0"
        
        pickerView.delegate = self
        pickerView.dataSource = self
    }
    
    @IBAction func textoMostrarEditingDidEnd(_ sender: UITextField) {
        if let texto = textoMostrar.text, !texto.isEmpty {
            textView.text += texto + "\n"
            self.textoMostrar.text = ""
        }
    }
    
    @IBAction func sliderValueChanged(_ sender: UISlider) {
        let value = Int(sender.value)
        sliderValue.text = "\(value)"
    }
    
    @IBAction func botonEmergencia(_ sender: UIButton) {
        let actionSheet = UIAlertController(title: "¡Emergencia!", message: "Selecciona una opción", preferredStyle: .actionSheet)

        actionSheet.addAction(UIAlertAction(title: "Nave salvavidas", style: .default) { _ in
            print("¡¡Lanzada nave salvavidas!!")
        })
        actionSheet.addAction(UIAlertAction(title: "Hiperespacio", style: .default) { _ in
            print("¡¡Activando hiperespacio!!")
        })
        actionSheet.addAction(UIAlertAction(title: "Autodestrucción", style: .destructive) { _ in
            print("¡¡Autodestrucción activada!!")
        })
        
        present(actionSheet, animated: true, completion: nil)
    }
    
    @objc(numberOfComponentsInPickerView:) func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    @objc func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return destinos.count
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return destinos[row]
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        print("Destino del viaje: \(destinos[row])")
    }
}
