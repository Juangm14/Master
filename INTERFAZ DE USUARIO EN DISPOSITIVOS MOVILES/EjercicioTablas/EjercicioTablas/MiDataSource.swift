//
//  MiDataSource.swift
//  EjercicioTablas
//
//  Created by Master Moviles on 18/10/24.
//

import UIKit


class MiDatasource : NSObject, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1000
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let nuevaCelda = tableView.dequeueReusableCell(withIdentifier: "miCelda",
                                  for: indexPath)
        nuevaCelda.textLabel?.text = String(indexPath.row)
            return nuevaCelda
        
        
    }
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
            if editingStyle==UITableViewCell.EditingStyle.delete {
                //self.datos.remove(at: indexPath.row)
                tableView.deleteRows(at: [indexPath], with: UITableView.RowAnimation.fade)
            }
            else if editingStyle==UITableViewCell.EditingStyle.insert {
                //self.datos.insert("Nueva celda", at: indexPath.row)
                tableView.insertRows(at: [indexPath], with: UITableView.RowAnimation.bottom)
            }
        }
}
