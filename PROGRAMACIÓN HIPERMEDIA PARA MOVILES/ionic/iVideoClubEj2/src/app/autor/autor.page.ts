import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-autor',
  templateUrl: './autor.page.html',
  styleUrls: ['./autor.page.scss'],
  standalone: false
})
export class AutorPage implements OnInit {

  autor = {
    name: 'Juan García Martínez',
    email: 'juan@ua.es',
    twitter: '@juan',
    phone: '+ 34999 999 999'
  };

  constructor() { }

  ngOnInit() {
  }
  
  ionViewDidEnter() {
    console.log('ionViewDidEnter AutorPage');
  }

  ionViewWillEnter() {
    console.log('ionViewWillEnter AutorPage');
  }

  ionViewDidLeave() {
    console.log('ionViewDidLeave AutorPage');
  }

  ionViewWillLeave() {
    console.log('ionViewWillLeave AutorPage');
  }
}
