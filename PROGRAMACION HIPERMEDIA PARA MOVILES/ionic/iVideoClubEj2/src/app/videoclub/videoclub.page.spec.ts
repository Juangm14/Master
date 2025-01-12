import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VideoclubPage } from './videoclub.page';

describe('VideoclubPage', () => {
  let component: VideoclubPage;
  let fixture: ComponentFixture<VideoclubPage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(VideoclubPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
