import { Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges, ViewChild, ElementRef } from '@angular/core';
import { Time } from '@angular/common';
import { Filters } from 'src/app/shared/models/Filters';
import { DateAdapter } from '@angular/material';
import { RoomDataService } from 'src/app/core/services/room-data/room-data.service';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-filters',
  templateUrl: './filters.component.html',
  styleUrls: ['./filters.component.css']
})
export class FiltersComponent implements OnInit, OnChanges {

  @Output() filterChange: EventEmitter<any> = new EventEmitter();

  dropdownOpen: boolean = false;
  dateChanged: boolean = false;
  dateInThePastIn: boolean = false;

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

  startHoursToSend: string = '00';
  startMinutesToSend: string = '00';

  endHoursToSend: string = '00';
  endMinutesToSend: string = '00';

  @Input() buildingLayout: any;

  constructor(
    private _dateAdapter: DateAdapter<Date>,
    private roomDataService: RoomDataService
  ) { }

  ngOnInit() {
  }

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


    if (this.dateInThePastIn == true || this.startDate.getTime() == this.endDate.getTime()) {
      this.startDate.setTime(this.finalDate.getTime());
      this.endDate.setTime(this.finalDate.getTime());
      this.endDate.setHours(23);
      this.endDate.setMinutes(59);
    } else {
      this.startDate.setDate(this.finalDate.getDate());
      this.startDate.setMonth(this.finalDate.getMonth());
      this.startDate.setFullYear(this.finalDate.getUTCFullYear());

      this.endDate.setDate(this.finalDate.getDate());
      this.endDate.setMonth(this.finalDate.getMonth());
      this.endDate.setFullYear(this.finalDate.getUTCFullYear());
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

  onStartHourEmit(event) {
    this.startDate = event;


    if (this.startDate.getHours() < 10) {
      this.endHoursToSend = '0' + this.startDate.getHours();
    } else {
      this.endHoursToSend = this.startDate.getHours().toString();
    }

    if (this.startDate.getMinutes() == 30) {
      this.endMinutesToSend = '00';

      if (this.startDate.getHours()+1 < 10) {
        this.endHoursToSend = '0' + (this.startDate.getHours() + 1);
      } else {
        if (this.startDate.getHours() == 23) {
          this.endHoursToSend = '00';
        }
        this.endHoursToSend = (this.startDate.getHours() + 1).toString();
      }
    }
    else {
      this.endMinutesToSend = '30';
    }

    this.endDate = new Date(2019, 0, 0, +this.endHoursToSend, +this.endMinutesToSend);
  }

  onEndHourEmit(event) {
    this.endDate = event;
  }

}
