import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router'
import { PeliculasAPIService } from '../../services/peliculas-api.service';

@Component({
  selector: 'app-detalle',
  templateUrl: './detalle.page.html',
  styleUrls: ['./detalle.page.scss'],
  standalone: false
})
export class DetallePage implements OnInit {

  //Declaramos la película para el deatalle.
  pelicula: any = {};

  constructor(private activatedRoute: ActivatedRoute, private peliculasAPIService: PeliculasAPIService) {}

  ngOnInit() {
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    if (id) {
      this.peliculasAPIService.getPelicula(+id).subscribe(
        (result) => {
          this.pelicula = result;
        },
        (err) => {
          console.log(err);
        }
      );
    }
  }

  //Incluímos el ciclo de vida también en esta nueva pagina.
  ionViewWillEnter() {
    console.log('ionViewWillEnter DetallePage');
  }

  ionViewDidEnter() {
    console.log('ionViewDidEnter DetallePage');
  }

  ionViewWillLeave() {
    console.log('ionViewWillLeave DetallePage');
  }

  ionViewDidLeave() {
    console.log('ionViewDidLeave DetallePage');
  }

}
