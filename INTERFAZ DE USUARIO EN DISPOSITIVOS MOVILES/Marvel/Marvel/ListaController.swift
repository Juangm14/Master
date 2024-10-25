//
//  ListaController.swift
//  Marvel
//
//  Created by Master Moviles on 25/10/24.
//

import UIKit

class ListaController : UIViewController, UISearchResultsUpdating {
    func updateSearchResults(for searchController: UISearchController) {
        let textoBuscado = searchController.searchBar.text!
        //recortamos caracteres en blanco
        let textoBuscadoTrim = textoBuscado.trimmingCharacters(in: .whitespacesAndNewlines)
        print(textoBuscadoTrim)
    }
    
    //esto debería ser una propiedad de ListaController
    var searchController : UISearchController!
    
    @IBOutlet weak var listaPersonajesTabla: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //..aquí podría haber más código
        self.searchController = UISearchController(searchResultsController: nil)
        //el delegate somos nosotros (ListaController)
        self.searchController.searchResultsUpdater = self
        //Configuramos el search controller
        //esto sería true si quisiéramos mostrar los resultados de búsqueda en un sitio distinto a la tabla
        self.searchController.obscuresBackgroundDuringPresentation = false
        //lo que aparece en la barra de búsqueda antes de teclear nada
        self.searchController.searchBar.placeholder = "Buscar texto"
        //Añadimos la barra de búsqueda a la tabla
        self.searchController.searchBar.sizeToFit()
        self.listaPersonajesTabla.tableHeaderView = searchController.searchBar
    }
}
