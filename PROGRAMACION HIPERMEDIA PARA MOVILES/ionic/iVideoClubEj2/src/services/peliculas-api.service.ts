import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class PeliculasAPIService {

  APIep = 'http://gbrain.dlsi.ua.es/videoclub/api/v1/catalog';

  constructor(private http: HttpClient) {}

  getPeliculas(): Observable<Pelicula[]> {
    return this.http.get<Pelicula[]>(this.APIep);
  }

  getPelicula(id: number) {
    return this.http.get(this.APIep + '/' + id);
  }
}

export interface Pelicula {
  id: number;
  title: string;
  director: string;
  poster: string;
  synopsis: string;
  year: number;
}
