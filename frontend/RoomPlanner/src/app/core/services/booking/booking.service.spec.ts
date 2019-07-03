import { TestBed } from '@angular/core/testing';

import { BookingService } from './booking.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { MatSnackBar, MatSnackBarModule } from '@angular/material';

describe('BookingService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        MatSnackBarModule
      ]
    });
  });

  it('should be created', () => {
    const service: BookingService = TestBed.get(BookingService);
    expect(service).toBeTruthy();
  });
});
