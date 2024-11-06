//
//  DetailViewController.swift
//  FilmTime
//
//  Created by Master Moviles on 6/11/24.
//

import UIKit

class DetailViewController: UIViewController {
    
    @IBOutlet weak var titulo: UILabel!
    @IBOutlet weak var anyoPeli: UILabel!
    @IBOutlet weak var descripcionPeli: UITextView!
    @IBOutlet weak var imagenPeli: UIImageView!
    @IBOutlet weak var stackView: UIStackView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    func didChangePelicula(with: Pelicula){
        if(with.titulo != self.titulo.text){
            self.titulo.text = with.titulo
            self.anyoPeli.text = with.fecha
            self.descripcionPeli.text = with.descripcion
            
            if let image = UIImage(named: with.caratula) {
                self.imagenPeli.image = image
            }
        }
    }
    
    override func viewWillLayoutSubviews() {
        if view.bounds.size.width >= view.bounds.size.height {
            self.stackView.axis = .horizontal
        }
        else {
            self.stackView.axis = .vertical
        }
    }

    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
