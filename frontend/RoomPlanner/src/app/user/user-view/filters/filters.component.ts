import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Time } from '@angular/common';
import { Filters } from 'src/app/shared/models/Filters';
import { DateAdapter } from '@angular/material';
import { RoomDataService } from 'src/app/core/services/room-data/room-data.service';

@Component({
  selector: 'app-filters',
  templateUrl: './filters.component.html',
  styleUrls: ['./filters.component.css']
})
export class FiltersComponent implements OnInit {

  @Input() buildingLayout: any;
  @Output() filterChange: EventEmitter<any> = new EventEmitter();

  dropdownOpen: boolean = false;
  dateChanged: boolean = false;



  floors: number[] = [1, 2, 3, 4, 5, 6, 7, 8];
  startHour: Date;
  endHour: Date;
  numberOfPeople: number;
  floorSelected: number;

  filters: Filters;

  defaultDate: Date = new Date(new Date().setHours(0, 0, 0, 0));

  selectedDate: Date;
  returnDate: Date;
  finalDate: Date;


  dateInThePastIn: boolean = false;

  constructor(
    private _dateAdapter: DateAdapter<Date>,
    private roomDataService: RoomDataService
  ) { }

  ngOnInit() { }

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }

  onDateChanged(date) {
    this.dateChanged = true;
    this.dateInThePastIn = false;

    if (this.defaultDate.getTime() > date.getTime()) {
      this.dateInThePastIn = true;
    }
  }

  onApplyFilters() {

    // !IMORTANT -> ASK Dorin
    this.filterChange.emit("filter object");




    if (this.dateChanged) {
      this.finalDate = this.selectedDate;
    }
    else {
      this.finalDate = this.defaultDate;
    }


    /*let rooms: RoomModel[] = */
    //this.returnDate = this.userdataService.getRoomsByDate(new Date(this.finalDate));

    console.log(`Selected date is: ${this.finalDate}`);

    if (this.startHour != null) {
      console.log(`Start hour selected: ${this.startHour}`);
    }
    if (this.endHour != null) {
      console.log(`End hour selected: ${this.endHour}`);
    }
    if (this.numberOfPeople != null) {
      console.log(`Number of people selected: ${this.numberOfPeople}`);
    }
    if (this.floorSelected != null) {
      console.log(`The floor selected: ${this.floorSelected}`);
    }

    this.filters = new Filters().create({
      date: this.finalDate,
      startHour: this.startHour,
      endHour: this.endHour,
      floor: this.floorSelected,
      numberOfPeople: this.numberOfPeople
    });

    console.log(this.filters);

  }

  getReturnDate() {
    return this.returnDate;
  }

  onSliderChange(event) {
    this.numberOfPeople = event.value;
  }


  onFloorChange(event) {
    this.floorSelected = event.value;
  }

  onStartHourEmit(event) {
    this.startHour = event;
  }

  onEndHourEmit(event) {
    this.endHour = event;
  }

}
