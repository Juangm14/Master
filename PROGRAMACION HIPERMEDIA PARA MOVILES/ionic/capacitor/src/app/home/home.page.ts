import { Component, OnInit, NgZone } from '@angular/core';
import { App } from '@capacitor/app';
import { Platform } from '@ionic/angular';
import { Capacitor } from '@capacitor/core';
import { Device } from '@capacitor/device';
import { Network } from '@capacitor/network';
import { Geolocation } from '@capacitor/geolocation';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: false
})
export class HomePage implements OnInit {

  capacitorInfo = {
    version: 0,//Capacitor.version,
    platform: Capacitor.getPlatform(),
  };

  deviceInfo: any = {};
  batteryInfo: any = {};
  networkInfo: any = {};
  eventLogs: string[] = [];
  gpsInfo: any = { latitude: '', longitude: '' };
  
  constructor(private platform: Platform, private ngZone: NgZone) {}

  ngOnInit() {
    this.platform.ready().then(() => {

      //Boton back
      this.platform.backButton.subscribeWithPriority(10, () => {
        console.log('Botón de retroceso detectado');
        if (this.platform.is('android')) {
          App.exitApp();
        }
      });

      this.getDeviceInfo();

      this.getBatteryInfo();

      this.getNetworkInfo();

      this.getGpsInfo();

      Network.addListener('networkStatusChange', (status) => {
        this.ngZone.run(() => {
          this.eventLogs.push(`Cambio tipo conexión a ${status.connectionType}`);
        });
      });

      App.addListener('resume', () => {
        this.ngZone.run(() => {
          this.eventLogs.push('onResume');
        });
      });

      App.addListener('pause', () => {
        this.ngZone.run(() => {
          this.eventLogs.push('onPause');
        });
      });

      App.addListener('appStateChange', (state) => {
        this.ngZone.run(() => {
          const event = state.isActive ? 'onStart' : 'onStop';
          this.eventLogs.push(event);
        });
      });
    });
  }

  async getDeviceInfo() {
    this.deviceInfo = await Device.getInfo();
  }

  async getBatteryInfo() {
    this.batteryInfo = await Device.getBatteryInfo();
  }

  async getNetworkInfo() {
    this.networkInfo = await Network.getStatus();
  }

  async getGpsInfo() {
    try {
      const position = await Geolocation.getCurrentPosition();
      console.log(position)
      this.gpsInfo.latitude = position.coords.latitude;
      this.gpsInfo.longitude = position.coords.longitude;
    } catch (error) {
      console.error('Error al obtener la ubicación:', error);
    }
  }
}
