import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { AutorPage } from './autor.page';

import { AutorPageRoutingModule } from './autor-routing.module';


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    AutorPageRoutingModule
  ],
  declarations: [AutorPage]
})
export class AutorPageModule {}
