import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import peliculasJSON from './posters/lista_peliculas.json';

@Component({
  selector: 'app-videoclub',
  templateUrl: './videoclub.page.html',
  styleUrls: ['./videoclub.page.scss'],
  standalone: false
})
export class VideoclubPage implements OnInit {

  peliculas: any[] = peliculasJSON;

  constructor(private http: HttpClient) { }

  ngOnInit() {
  }

}
