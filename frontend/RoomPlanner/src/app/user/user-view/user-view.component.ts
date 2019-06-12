import { Component, OnInit, ViewChild } from '@angular/core';
import { MatCalendar, DateAdapter } from '@angular/material';
import { UserdataService } from '../../shared/services/userdata.service';
import { RoomModel } from '../../core/models/RoomModel';
import { Filters } from '../../shared/models/Filters';


@Component({
  selector: 'app-user-view',
  templateUrl: './user-view.component.html',
  styleUrls: ['./user-view.component.css']
})
export class UserViewComponent implements OnInit {

  dropdownOpen: boolean = false;
  dateChanged: boolean = false;
  
  filtersReturned:Filters;

  numberOfPeople:number;
  floorSelected:number;


  floors: number[] = [1, 2, 3, 4, 5, 6, 7, 8];

  constructor(
    private _dateAdapter: DateAdapter<Date>,
    private userdataService: UserdataService
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
    this.returnDate = this.userdataService.getRoomsByDate(new Date(this.finalDate));

  }

  getReturnDate() {
    return this.returnDate;
  }

  onSliderChange(event){
    this.numberOfPeople=event.value;
    console.log(`Number of people selected: ${this.numberOfPeople}`);
  }

  onStartHourChange(event){
    console.log(event.target.value);
    console.log(event.target.type);
  }

  onFloorChange(event){
    this.floorSelected=event.value;
    console.log(`The floor selected: ${this.floorSelected}`);
  }


}
