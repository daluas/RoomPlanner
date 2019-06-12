import { Component, OnInit, ViewChild } from '@angular/core';
import { MatCalendar, DateAdapter } from '@angular/material';
import { RoomDataService } from '../../core/services/room-data/room-data.service';
import { RoomModel } from '../../core/models/RoomModel';


@Component({
  selector: 'app-user-view',
  templateUrl: './user-view.component.html',
  styleUrls: ['./user-view.component.css']
})
export class UserViewComponent implements OnInit {

  dropdownOpen: boolean = false;
  dateChanged: boolean = false;

  floors: number[] = [1, 2, 3, 4, 5, 6, 7, 8];

  constructor(
    private _dateAdapter: DateAdapter<Date>,
    private roomDataService: RoomDataService
  ) { }

  ngOnInit() { }

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }

  defaultDate: Date = this._dateAdapter.today();;
  selectedDate: Date;

  finalDate: Date;

  returnDate: Date;

  onDateChanged(date) {
    this.dateChanged = true;
  }

  onApplyFilters() {

    if (this.dateChanged) {
      this.finalDate = this.selectedDate;
    }
    else {
      this.finalDate = this.defaultDate;
    }


    /*let rooms: RoomModel[] = */
    this.returnDate = this.roomDataService.getRoomsByDate(new Date(this.finalDate));

  }

  getReturnDate() {
    return this.returnDate;
  }


}
