import { TestBed } from '@angular/core/testing';

import { UserdataService } from './userdata.service';

describe('UserdataService', () => {

  let service:UserdataService;

  beforeEach(() => TestBed.configureTestingModule({
    providers:[UserdataService]
  }));

  it('should be created', () => {
   service= TestBed.get(UserdataService);
    expect(service).toBeTruthy();
  });


  it('should return the date given as parameter', () => {
    service= TestBed.get(UserdataService);
    var date=new Date();
    var dateReturned=service.getRoomsByDate(date);
    expect(dateReturned).toBe(date);
  });
});
