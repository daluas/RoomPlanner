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

  startHour: Date = new Date(new Date().setHours(0, 0, 0, 0));
  endHour: Date = new Date(new Date().setHours(0, 0, 0, 0));
  numberOfPeople: number;
  floorSelected: number;

  filters: Filters;
  defaultDate: Date = new Date(new Date().setHours(0, 0, 0, 0));

  selectedDate: Date;
  finalDate: Date;

  invalidHours:boolean=false;

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
      let copyArray=[...this.floors];
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

    if(this.startHour.getMinutes()<30){
      this.startHour.setMinutes(0);
    }
    else if(this.startHour.getMinutes()>30){
      this.startHour.setMinutes(30);
    }

    if(this.endHour.getMinutes()<30 && this.endHour.getMinutes()>0){
      this.endHour.setMinutes(30);
    }
    else if(this.endHour.getMinutes()>30){
      this.endHour.setMinutes(0);
      this.endHour.setHours(this.endHour.getHours()+1);
    }

   
    if (this.floorSelected == null){
      this.floorSelected = this.floorByDefault;
    }
    this.filters = new Filters().create({
      date: this.finalDate,
      startHour: this.startHour,
      endHour: this.endHour,
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

  onPeopleClear(){
    this.numberOfPeople =undefined;
  }
  onFloorClear(){
    this.floorSelected=this.floorByDefault;
  }

  onStartHourEmit(event) {
    this.startHour = event;

    if(this.startHour.getTime()>=this.endHour.getTime()){
      this.invalidHours=true;
    } 
    else{
      this.invalidHours=false;
    }
  }

  onEndHourEmit(event) {
    this.endHour = event;

    if(this.startHour.getTime()>=this.endHour.getTime()){
      this.invalidHours=true;
    } 
    else{
      this.invalidHours=false;
    }
  }

}
