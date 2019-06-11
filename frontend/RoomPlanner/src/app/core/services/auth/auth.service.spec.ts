import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import { inject } from '@angular/core';

fdescribe('AuthService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    declarations: [],
    imports: [],
    providers: [AuthService],
  }));

  it('should be created', inject(
    [AuthService],
    (service: AuthService) => {
      expect(service).toBeTruthy();
      // const service: AuthService = TestBed.get(AuthService);
    }
  ) 
});
