import { TestBed } from '@angular/core/testing';
import { HttpClientModule, HttpClient, HttpHandler } from '@angular/common/http';
import { AuthService } from './auth.service';

describe('AuthService', () => {

  let service: AuthService;

  beforeEach(() => TestBed.configureTestingModule({

  }));

  it('should be created', () => {
    service = TestBed.get(AuthService);
    expect(service).toBeTruthy();
  });
});
