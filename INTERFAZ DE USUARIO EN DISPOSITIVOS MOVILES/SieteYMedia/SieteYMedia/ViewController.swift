//
//  ViewController.swift
//  SieteYMedia
//
//  Created by Master Moviles on 16/10/24.
//

import UIKit

class ViewController: UIViewController {
    
    var juego : Juego!
    var viewsCartas : [UIImageView] = []
    
    @IBOutlet weak var nuevaPartida: UIButton!
    @IBOutlet weak var pedirCarta: UIButton!
    @IBOutlet weak var plantarse: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    
    
    @IBAction func nuevaPartida(_ sender: Any) {
        self.juego = Juego()
        self.juego.turnoMaquina()
        self.pedirCarta.isEnabled = true
        self.plantarse.isEnabled = true
        self.nuevaPartida.isEnabled = false
    }
    
    @IBAction func pedirCarta(_ sender: Any) {
        
        let estadoJuego = self.juego.jugadorPideCarta()
        
        if let carta = self.juego.manoJugador.getCarta(at: self.juego.manoJugador.tamaño-1){
            dibujarCarta(carta)
        }
        
        comprobar(el : estadoJuego)
    }
    
    
    @IBAction func plantarse(_ sender: Any) {
        let estadoJuego = self.juego.jugadorSePlanta()
        comprobar(el : estadoJuego)
    }
    
    func dibujarCarta(_ carta: Carta) {
        let nombreImagen = "\(Int(carta.valor))\(carta.palo)"
        guard let image = UIImage(named: nombreImagen) else {
            print("Imagen no encontrada: \(nombreImagen)")
            return
        }
        
        print("dijujando carta: \(carta)")
        let imageView = UIImageView(image: image)
        let limitesPantalla = UIScreen.main.bounds
        let anchoPantalla = limitesPantalla.width
        let altoPantalla = limitesPantalla.height
        imageView.frame = CGRect(x: 100, y: 100, width: anchoPantalla*0.20, height: altoPantalla*0.15) // Posición inicial fuera de la pantalla
        self.view.addSubview(imageView)
        
        // Animar la carta a su posición final
        UIView.animate(withDuration: 0.5) {
            imageView.frame.origin.x += CGFloat(self.viewsCartas.count * 25) // Espaciado entre cartas
            imageView.transform = CGAffineTransform(rotationAngle: CGFloat(0))
        }
        
        self.viewsCartas.append(imageView) // Guardar referencia. Lo hacemos fuera de la animación ya que es para saber que cartas tenemos en pantalla para poder eliminarlas.
    }
    
    
    func eliminarCartas() {

        for v in self.viewsCartas {
            v.removeFromSuperview() // Quitamos la referencia de nuestra vista.
        }
        //ya no tenemos imágenes de cartas en pantalla, ponemos el array a vacío
        self.viewsCartas=[]
    }
    
    func comprobar(el estadoJuego: EstadoJuego){
        
        if (estadoJuego != EstadoJuego.turnoJugador){
            switch estadoJuego {
            case .empate:
                print("¡Empate!")
                eliminarCartas()
                self.pedirCarta.isEnabled = false
                self.plantarse.isEnabled = false
                self.nuevaPartida.isEnabled = true
                mostrarDialogo()
            case .ganaJugador:
                print("¡Has ganado, pierde la máquina!");
                eliminarCartas()
                self.pedirCarta.isEnabled = false
                self.plantarse.isEnabled = false
                self.nuevaPartida.isEnabled = true
                mostrarDialogo()
            case .noIniciado:
                print("¡Debes iniciar partida")
            case .pierdeJugador:
                print("¡Has perdido, gana la máquina!")
                eliminarCartas()
                self.pedirCarta.isEnabled = false
                self.plantarse.isEnabled = false
                self.nuevaPartida.isEnabled = true
                mostrarDialogo()
            case .turnoJugador:
                print("")
            case .sepasaJugador :
                print("¡Has perdido, gana la máquina!")
                eliminarCartas()
                self.pedirCarta.isEnabled = false
                self.plantarse.isEnabled = false
                self.nuevaPartida.isEnabled = true
                mostrarDialogo()
            }
        }
    }
    
    func mostrarDialogo(){
        let alert = UIAlertController(
            title: "Fin del juego",
            message: self.juego.mensajeEstadoJuego(),
            preferredStyle: UIAlertController.Style.alert)
        let action = UIAlertAction(
            title: "OK",
            style: UIAlertAction.Style.default)
        alert.addAction(action)
        self.present(alert, animated: true, completion: nil)
    }
    
}
