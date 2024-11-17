//
//  TableViewController.swift
//  FilmTime
//
//  Created by Master Moviles on 6/11/24.
//

import UIKit

class TableViewController: UITableViewController, UISearchResultsUpdating {
  
    var peliculas = [Pelicula]()
    private var searchController : UISearchController?
    private var searchResults = [Pelicula]()
    
    func crearPeliculas() {
        let sentidoDeLaVida = Pelicula(titulo: "El sentido de la vida", caratula: "sentido.jpg", fecha: "1983", descripcion: "Conjunto de episodios que muestran de forma disparatada los momentos más importantes del ciclo de la vida. Desde el nacimiento a la muerte, pasando por asuntos como la filosofía, la historia o la medicina, todo tratado con el inconfundible humor de los populares cómicos ingleses. El prólogo es un cortometraje independiente rodado por Terry Gilliam: Seguros permanentes Crimson.")

        self.peliculas.append(sentidoDeLaVida)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        crearPeliculas()
        
        // Creamos una tabla alternativa para visualizar los resultados cuando se seleccione la búsqueda
        let searchResultsController = UITableViewController(style: .plain)
        searchResultsController.tableView.dataSource = self
        searchResultsController.tableView.delegate = self

        // Asignamos esta tabla a nuestro controlador de búsqueda
        self.searchController = UISearchController(searchResultsController: searchResultsController)
        self.searchController?.searchResultsUpdater = self

        // Especificamos el tamaño de la barra de búsqueda
        if let frame = self.searchController?.searchBar.frame {
            self.searchController?.searchBar.frame = CGRect(x: frame.origin.x, y: frame.origin.y, width: frame.size.width, height: 44.0)
        }

        // La añadimos a la cabecera de la tabla
        self.tableView.tableHeaderView = self.searchController?.searchBar

        // Esto es para indicar que nuestra vista de tabla de búsqueda se superpondrá a la ya existente
        self.definesPresentationContext = true

    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let src = self.searchController?.searchResultsController as! UITableViewController

        if tableView == src.tableView {
            return self.searchResults.count
        }
        else {
            return self.peliculas.count
        }
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = self.tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath)

        let src = self.searchController?.searchResultsController as! UITableViewController
        let pelicula: Pelicula

        if tableView == src.tableView {
            pelicula = self.searchResults[indexPath.row] // Aquí obtienes la película completa
        } else {
            pelicula = peliculas[indexPath.row]
        }

        cell.textLabel!.text = pelicula.titulo
        return cell
    }


    func updateSearchResults(for searchController: UISearchController) {
        // Cogemos el texto introducido en la barra de búsqueda
        let searchString = self.searchController?.searchBar.text

        // Si la cadena de búsqueda es vacía, copiamos en searchResults todos los objetos
        if searchString == nil || searchString == "" {
            self.searchResults = self.peliculas
        }
        // Si no, copiamos en searchResults sólo los que contengan el texto de búsqueda
        else {
            if let searchString = searchString {
                self.searchResults = self.peliculas
                    .filter { $0.titulo.lowercased().contains(searchString.lowercased()) }
            }
        }



        // Recargamos los datos de la tabla
        let tvc = self.searchController?.searchResultsController as! UITableViewController
        tvc.tableView.reloadData()

        // Deseleccionamos la celda de la tabla principal
        if let selected = tableView.indexPathForSelectedRow {
            tableView.deselectRow(at:selected, animated: false)
        }
    }


    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }


    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let pelicula: Pelicula

        let src = self.searchController?.searchResultsController as! UITableViewController
        if tableView == src.tableView {
            pelicula = self.searchResults[indexPath.row] // Aquí obtienes la película completa
        } else {
            pelicula = peliculas[indexPath.row]
        }

        // Conexión con el controlador detalle
        let detailViewController = splitViewController!.viewController(for: .secondary) as? DetailViewController

        // En vez de actualizar los valores aquí, los actualizamos en la vista del detalle.
        detailViewController?.didChangePelicula(with: pelicula)

        // Si no se muestra el detalle del controlador es porque el dispositivo es demasiado pequeño (un móvil)
        if !detailViewController!.isBeingPresented {
            splitViewController!.showDetailViewController(detailViewController!, sender: self)
        }
    }


    /*
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "reuseIdentifier", for: indexPath)

        // Configure the cell...

        return cell
    }
    */

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
