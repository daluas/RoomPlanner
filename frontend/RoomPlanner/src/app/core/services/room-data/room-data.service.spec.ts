import { TestBed } from '@angular/core/testing';

import { RoomDataService } from './room-data.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
describe('RoomDataService', () => {

  let service: RoomDataService;

  beforeEach(() => TestBed.configureTestingModule({
    providers: [RoomDataService],
    imports: [
      HttpClientTestingModule
    ]
  }));

  it('should be created', () => {
    service = TestBed.get(RoomDataService);
    expect(service).toBeTruthy();
  });
});
