import { Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { Time } from '@angular/common';
import { Filters } from 'src/app/shared/models/Filters';
import { DateAdapter } from '@angular/material';
import { RoomDataService } from 'src/app/core/services/room-data/room-data.service';

@Component({
  selector: 'app-filters',
  templateUrl: './filters.component.html',
  styleUrls: ['./filters.component.css']
})
export class FiltersComponent implements OnInit, OnChanges {

  @Output() filterChange: EventEmitter<any> = new EventEmitter();

  dropdownOpen: boolean = false;
  dateChanged: boolean = false;

  floors: number[] = [];
  floorByDefault: number;

  startDate: Date = new Date(new Date().setHours(0, 0, 0, 0));
  endDate: Date = new Date(new Date().setHours(0, 0, 0, 0));

  numberOfPeople: number = null;
  floorSelected: number = null;

  filters: Filters;

  defaultDate: Date = new Date(new Date().setHours(0, 0, 0, 0));

  selectedDate: Date;
  finalDate: Date;

  invalidHours: boolean = false;

  dateInThePastIn: boolean = false;

  constructor(
    private _dateAdapter: DateAdapter<Date>,
    private roomDataService: RoomDataService
  ) {
  }

  ngOnInit() {
  }

  @Input() buildingLayout: any;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['buildingLayout']) {
      if (this.buildingLayout !== undefined) {
        this.buildingLayout.forEach(element => {
          this.floors.push(element.floor);
        });
      }
      let copyArray = [...this.floors];
      copyArray.reverse();
      this.floorByDefault = copyArray.pop();
    }
  }

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

    if (this.dateChanged) {
      this.finalDate = this.selectedDate;
    }
    else {
      this.finalDate = this.defaultDate;
    }
    //if is not in the past

    if (this.dateInThePastIn == true || this.startDate.getTime() == this.endDate.getTime()) {
      this.startDate.setDate(this.finalDate.getDate());
      this.startDate.setMonth(this.finalDate.getMonth());
      this.startDate.setFullYear(this.finalDate.getUTCFullYear());

      this.endDate = null;
    } else {
      this.startDate.setDate(this.finalDate.getDate());
      this.startDate.setMonth(this.finalDate.getMonth());
      this.startDate.setFullYear(this.finalDate.getUTCFullYear());

      this.endDate.setDate(this.finalDate.getDate());
      this.endDate.setMonth(this.finalDate.getMonth());
      this.endDate.setFullYear(this.finalDate.getUTCFullYear());

    }

    if (this.startDate.getMinutes() < 30) {
      this.startDate.setMinutes(0);
    }
    else if (this.startDate.getMinutes() > 30) {
      this.startDate.setMinutes(30);
    }

    if (this.endDate.getMinutes() < 30 && this.endDate.getMinutes() > 0) {
      this.endDate.setMinutes(30);
    }
    else if (this.endDate.getMinutes() > 30) {
      this.endDate.setMinutes(0);
      this.endDate.setHours(this.endDate.getHours() + 1);
    }


    if (this.floorSelected == null) {
      this.floorSelected = this.floorByDefault;
    }
    this.filters = new Filters().create({

      startDate: this.startDate,
      endDate: this.endDate,
      floor: this.floorSelected,
      numberOfPeople: this.numberOfPeople
    });


    this.filterChange.emit(this.filters);
  }

  onSliderChange(event) {
    this.numberOfPeople = event.value;
  }

  onFloorChange(event) {
    this.floorSelected = event.value;
  }

  onPeopleClear() {
    this.numberOfPeople = null;
  }
  onFloorClear() {
    this.floorSelected = this.floorByDefault;
  }

  onStartHourEmit(event) {
    this.startDate = event;

    // if (this.startDate.getTime() == this.endDate.getTime()) {
    //   if(this.endDate.getMinutes()==0){
    //     this.endDate.setMinutes(this.endDate.getMinutes() + 30);
    //   }else{
    //     this.endDate.setHours(this.endDate.getHours() + 1);
    //     this.endDate.setMinutes(this.endDate.getMinutes() + 30);
        
    //   }
      
    // }

    if (this.startDate.getTime() >= this.endDate.getTime()) {
      this.invalidHours = true;
    }
    else {
      this.invalidHours = false;
    }
  }

  onEndHourEmit(event) {
    this.endDate = event;

    if (this.startDate.getTime() >= this.endDate.getTime()) {
      this.invalidHours = true;
    }
    else {
      this.invalidHours = false;
    }
  }

}
