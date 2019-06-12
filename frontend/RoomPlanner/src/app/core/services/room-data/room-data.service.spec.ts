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


  it('should return the date given as parameter', () => {
    service = TestBed.get(RoomDataService);
    var date = new Date();
    var dateReturned = service.getRoomsByDate(date);
    expect(dateReturned).toBe(date);
  });
});
