import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PeliculasAPIService } from '../../services/peliculas-api.service';

@Component({
  selector: 'app-videoclub',
  templateUrl: './videoclub.page.html',
  styleUrls: ['./videoclub.page.scss'],
  standalone: false
})
export class VideoclubPage implements OnInit {
  modoLista: boolean = true;
  listaPeliculas: any[] = [];

  constructor(private router: Router, private peliculasAPIService: PeliculasAPIService) {}

  ngOnInit() {
    this.peliculasAPIService.getPeliculas().subscribe(
      (result) => {
        this.listaPeliculas = result;
      },
      (err) => {
        console.error('Error al cargar las películas:', err);
      }
    );
  }

  cambiarVista() {
    this.modoLista = !this.modoLista;
  }

  verPaginaDetalle(id: number): void {
    this.router.navigate(['/detalle', id]);
  }
}
