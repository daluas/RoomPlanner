import { TestBed } from '@angular/core/testing';

import { RoomDataService } from './room-data.service';
describe('RoomDataService', () => {

  let service: RoomDataService;

  beforeEach(() => TestBed.configureTestingModule({
    providers: [RoomDataService]
  }));

  it('should be created', () => {
    service = TestBed.get(RoomDataService);
    expect(service).toBeTruthy();
  });
});
