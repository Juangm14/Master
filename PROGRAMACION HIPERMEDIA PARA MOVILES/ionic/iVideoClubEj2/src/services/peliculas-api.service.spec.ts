import { TestBed } from '@angular/core/testing';

import { PeliculasAPIService } from './peliculas-api.service';

describe('PeliculasAPIService', () => {
  let service: PeliculasAPIService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PeliculasAPIService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
