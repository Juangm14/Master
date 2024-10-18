//
//  MiDelegate.swift
//  EjercicioTablas
//
//  Created by Master Moviles on 18/10/24.
//

import UIKit

class MiDelegate : NSObject, UITableViewDelegate {
 
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
      if let celda = tableView.cellForRow(at: indexPath) {
        //Si no hay marca de verificación la ponemos. Si la hay la quitamos
        if celda.accessoryType==UITableViewCell.AccessoryType.none {
           celda.accessoryType = .checkmark
        }
        else {
           celda.accessoryType = .none
        }
        //deseleccionamos la celda, si no se quedará con el fondo gris
        tableView.deselectRow(at: indexPath, animated: true)
      }
    }
}
