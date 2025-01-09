import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: false,
})
export class HomePage {

  constructor() {}

  ionViewDidEnter() {
    console.log('ionViewDidEnter HomePage');
  }

  ionViewWillEnter() {
    console.log('ionViewWillEnter HomePage');
  }

  ionViewDidLeave() {
    console.log('ionViewDidLeave HomePage');
  }

  ionViewWillLeave() {
    console.log('ionViewWillLeave HomePage');
  }
}