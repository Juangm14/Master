import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-curriculum',
  templateUrl: './curriculum.page.html',
  styleUrls: ['./curriculum.page.scss'],
  standalone: false
})
export class CurriculumPage implements OnInit {

  listaTrabajos: { fecha: string, desc: string }[] = [
    { fecha: '2010-Actualidad', desc: 'Profesor del Máster Universitario de Software para Dispositivos Móviles.' },
    { fecha: '2010-Actualidad', desc: 'Profesor del Máster Universitario en Desarrollo de aplicaciones y Servicios Web' }
  ];

  constructor() { }

  ngOnInit() {
  }
  
  ionViewWillEnter() {
    console.log('ionViewWillEnter CurriculumPage');
  }

  ionViewDidEnter() {
    console.log('ionViewDidEnter CurriculumPage');
  }

  ionViewWillLeave() {
    console.log('ionViewWillLeave CurriculumPage');
  }

  ionViewDidLeave() {
    console.log('ionViewDidLeave CurriculumPage');
  }
}
