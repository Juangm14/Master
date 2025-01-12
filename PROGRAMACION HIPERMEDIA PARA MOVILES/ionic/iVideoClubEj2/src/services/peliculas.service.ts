import { Injectable } from '@angular/core';
import peliculasJSON from '../app/posters/lista_peliculas.json';

@Injectable({
  providedIn: 'root'
})
export class PeliculasService {

  private peliculas: {
      id: number,
      title: string,
      year: string,
      director: string,
      poster: string,
      synopsis: string
  }[] = peliculasJSON;

  constructor() { }

  getPeliculas(){
    return this.peliculas;
  }
}
