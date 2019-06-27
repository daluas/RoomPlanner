import { TestBed } from '@angular/core/testing';

import { BookingService } from './booking.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('BookingService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [],
      imports: [HttpClientTestingModule],
    });
  });

  it('should be created', () => {
    const service: BookingService = TestBed.get(BookingService);
    expect(service).toBeTruthy();
  });
});
