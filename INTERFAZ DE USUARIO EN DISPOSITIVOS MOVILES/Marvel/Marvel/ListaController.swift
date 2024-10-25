//
//  ListaController.swift
//  Marvel
//
//  Created by Master Moviles on 25/10/24.
//

import UIKit
import Marvelous

class ListaController : UIViewController, UISearchResultsUpdating, UITableViewDataSource {
    
    var searchController : UISearchController!
    
    @IBOutlet weak var listaPersonajesTabla: UITableView!
    
    var arrayPersonajes : [RCCharacterObject] = []
    
    var miSpinner = UIActivityIndicatorView()
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        self.arrayPersonajes.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let nuevaCelda = listaPersonajesTabla.dequeueReusableCell(withIdentifier: "CeldaNombrePersonaje",
                                  for: indexPath)
        let personaje = self.arrayPersonajes[indexPath.row]
        nuevaCelda.textLabel?.text = personaje.name
            return nuevaCelda
    }
    
    func updateSearchResults(for searchController: UISearchController) {
        let textoBuscado = searchController.searchBar.text!
        //recortamos caracteres en blanco
        let textoBuscadoTrim = textoBuscado.trimmingCharacters(in: .whitespacesAndNewlines)
        if textoBuscado.count > 2 {
            self.miSpinner.startAnimating()
            let marvelAPI = RCMarvelAPI()
            //PUEDES CAMBIAR ESTO PARA PONER TUS CLAVES
            marvelAPI.publicKey = "a6927e7e15930110aade56ef90244f6d"
            marvelAPI.privateKey = "487b621fc3c0d6f128b468ba86c99c508f24d357"
            let filtro = RCCharacterFilter()
            filtro.nameStartsWith = textoBuscadoTrim
            filtro.limit = 50
            
            marvelAPI.characters(by: filtro) {
                resultados, info, error in
                if let personajes = resultados as? [RCCharacterObject] {
                    self.arrayPersonajes = personajes
                }else{
                    self.arrayPersonajes = []
                }
            }
            
            OperationQueue.main.addOperation() {
                self.listaPersonajesTabla.reloadData();
            }
            self.miSpinner.stopAnimating()
        }
    }
    
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "DetallePersonaje" {
            if let detailVC = segue.destination as? DetalleViewController {
                
                if let selectedRow = listaPersonajesTabla.indexPathForSelectedRow {
                    detailVC.personaje = self.arrayPersonajes[selectedRow.row]
                }
                
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        listaPersonajesTabla.dataSource = self

        self.searchController = UISearchController(searchResultsController: nil)
        //el delegate somos nosotros (ListaController)
        self.searchController.searchResultsUpdater = self
        //Configuramos el search controller
        //esto sería true si quisiéramos mostrar los resultados de búsqueda en un sitio distinto a la tabla
        self.searchController.obscuresBackgroundDuringPresentation = false
        //lo que aparece en la barra de búsqueda antes de teclear nada
        self.searchController.searchBar.placeholder = "Buscar texto"
        //Añadimos la barra de búsqueda a la tabla
        self.navigationItem.searchController = searchController
        
        //que se oculte automáticamente al pararse
        miSpinner.hidesWhenStopped = true
        //lo añadimos a la vista principal del controller actual
        self.view.addSubview(miSpinner)
        //lo centramos en la pantalla
        miSpinner.center.x = self.view.center.x
        miSpinner.center.y = self.view.center.y
        //nos aseguramos que está al frente y no tapado por la tabla
        self.view.bringSubviewToFront(self.miSpinner)
    }
}
