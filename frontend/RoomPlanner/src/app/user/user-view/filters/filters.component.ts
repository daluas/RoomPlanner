import { Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges, ViewChild, ElementRef } from '@angular/core';
import { Time } from '@angular/common';
import { Filters } from 'src/app/shared/models/Filters';
import { DateAdapter } from '@angular/material';
import { RoomDataService } from 'src/app/core/services/room-data/room-data.service';
import { FormControl } from '@angular/forms';
import { FloorModel } from '../../../core/models/FloorModel';
import { RoomModel } from '../../../core/models/RoomModel';

@Component({
  selector: 'app-filters',
  templateUrl: './filters.component.html',
  styleUrls: ['./filters.component.css']
})
export class FiltersComponent implements OnInit, OnChanges {

  @Output() filterChange: EventEmitter<any> = new EventEmitter();
  //@Output() roomChange:EventEmitter<any>=new EventEmitter();

  dropdownOpen: boolean = false;
  dateChanged: boolean = false;
  dateInThePastIn: boolean = false;
  roomIsSelected: boolean = false;

  floors: FloorModel[] = new Array<FloorModel>();
  floorByDefault: FloorModel = new FloorModel().create({ floor: 1 });


  startDate: Date = new Date(new Date().setHours(0, 0, 0, 0));
  endDate: Date = new Date(new Date().setHours(0, 0, 0, 0));
  numberOfPeople: number = null;
  floorSelected: FloorModel = null;
  roomSelected: RoomModel = null;
  filters: Filters;


  defaultDate: Date = new Date(new Date().setHours(0, 0, 0, 0));
  selectedDate: Date;
  finalDate: Date;

  startHoursToSend: string = '00';
  startMinutesToSend: string = '00';
  endHoursToSend: string = '00';
  endMinutesToSend: string = '00';

  @Input() buildingLayout: FloorModel[];

  constructor(
  ) {
  }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    let firstFloor: FloorModel;
    if (changes['buildingLayout']) {
      if (this.buildingLayout !== undefined && this.buildingLayout !== null) {
        this.buildingLayout.forEach(element => {
          this.floors.push(new FloorModel().create({
            floor: element.floor,
            rooms: element.rooms
          }));
        });

        firstFloor = this.floors[0];
        this.floorByDefault = new FloorModel().create(firstFloor);

        this.floorSelected = this.floorByDefault;

      }
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
    //console.log(`applying on :`, this.filters)

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
      if (this.endDate.getHours() != 23) {
        this.endDate.setMinutes(0);
        this.endDate.setHours(this.endDate.getHours() + 1);
      } else {
        this.endDate.setMinutes(59);
      }
    }
    if (this.dateInThePastIn == true){
      this.numberOfPeople=null;
    }

    if (this.dateInThePastIn == true || this.startDate.getTime() == this.endDate.getTime()) {
      // this.startDate.setTime(this.finalDate.getTime());
      // this.endDate.setTime(this.finalDate.getTime());

      this.startDate.setDate(this.finalDate.getDate());
      this.startDate.setMonth(this.finalDate.getMonth());
      this.startDate.setFullYear(this.finalDate.getUTCFullYear());
      this.endDate.setHours(3);
      this.endDate.setMinutes(1);

      this.endDate.setDate(this.finalDate.getDate());
      this.endDate.setMonth(this.finalDate.getMonth());
      this.endDate.setFullYear(this.finalDate.getUTCFullYear());
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
    if (this.floorSelected == undefined) {
      this.floorSelected = this.floorByDefault;
    }

    this.filters = new Filters().create({
      startDate: this.startDate,
      endDate: this.endDate,
      floor: this.floorSelected.floor,
      minPersons: this.numberOfPeople
    });

    // console.log(`Filtreleeee`)
    // console.log(this.filters.startDate)
    // console.log(this.filters.endDate)

    if (this.roomSelected != null) {
      // this.roomChange.emit(this.roomSelected);
      this.filters.roomSelected = this.roomSelected;
    }

    this.filterChange.emit(this.filters);

  }

  onSliderChange(event) {
    this.numberOfPeople = event.value;
  }

  onFloorChange(event) {
    this.floorSelected = event.value;
  }

  onRoomChange(event) {
    if (event.value == "all") {
      this.roomIsSelected = false;
      this.roomSelected = null;
    } else {
      this.roomSelected = new RoomModel().create(event.value);
      this.roomIsSelected = true;
    }
  }

  onPeopleClear() {
    this.numberOfPeople = null;
  }

  onStartHourEmit(event) {
    this.startDate = event;

    if (this.startDate.getHours() >= this.endDate.getHours()) {
      if (this.startDate.getHours() < 10) {
        this.endHoursToSend = '0' + this.startDate.getHours();
      } else {
        this.endHoursToSend = this.startDate.getHours().toString();
      }

      if (this.startDate.getMinutes() >= 30) {
        this.endMinutesToSend = '00';
        if (this.startDate.getHours() + 1 < 10) {
          this.endHoursToSend = '0' + (this.startDate.getHours() + 1);
        } else {
          this.endHoursToSend = (this.startDate.getHours() + 1).toString();
        }
      }
      else {
        this.endMinutesToSend = '30';
      }

      if (this.startDate.getHours() == 23) {
        if (this.startDate.getMinutes() == 0) {
          this.endHoursToSend = this.startDate.getHours().toString();
          this.endMinutesToSend = '30';
        } else if (this.startDate.getMinutes() == 30) {
          this.endMinutesToSend = '59';
          this.endHoursToSend = this.startDate.getHours().toString();
        }
      }
      this.endDate = new Date(2019, 0, 0, +this.endHoursToSend, +this.endMinutesToSend);
    }
  }

  onEndHourEmit(event) {
    this.endDate = event;

    if (this.endDate.getHours() <= this.startDate.getHours()) {

      if (this.endDate.getHours() < 10) {
        this.startHoursToSend = '0' + this.endDate.getHours();
      } else {
        this.startHoursToSend = this.endDate.getHours().toString();
      }

      if (this.endDate.getHours() == this.startDate.getHours()) {

        if (this.endDate.getMinutes() == 30) {
          this.startMinutesToSend = '00';
        } else if (this.endDate.getMinutes() == 0) {

          if (this.endDate.getHours() == 0) {
            this.startHoursToSend = '00';
            this.startMinutesToSend = '00';
          } else {
            this.startMinutesToSend = '30';

            if (this.endDate.getHours() < 10) {
              this.startHoursToSend = '0' + (this.endDate.getHours() - 1);
            } else {
              this.startHoursToSend = (this.endDate.getHours() - 1).toString();
            }
          }
        }
      }
      this.startDate = new Date(2019, 0, 0, +this.startHoursToSend, +this.startMinutesToSend);
    }

  }

}
