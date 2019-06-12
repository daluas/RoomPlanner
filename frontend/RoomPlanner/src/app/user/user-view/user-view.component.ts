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
  
  
  
  floors: number[] = [1, 2, 3, 4, 5, 6, 7, 8];
  numberOfPeople:number;
  floorSelected:number;
  filtersReturned:Filters;

  defaultDate: Date = this._dateAdapter.today();;
  selectedDate: Date;
  returnDate: Date;
  finalDate: Date;
 

  dateInThePastIn:boolean=false;

  constructor(
    private _dateAdapter: DateAdapter<Date>,
    private userdataService: UserdataService
  ) { }

  ngOnInit() { }

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }

  onDateChanged(date) {
    this.dateChanged = true;

    if(date.getDate()<this.defaultDate.getDate()){
      console.log("data din trecut!");
      this.dateInThePastIn=true;
      
    }else if(date.getDate()==this.defaultDate.getDate()){
      this.dateChanged=false;

      this.dateInThePastIn=false;
    }else{
      this.dateInThePastIn=false;
    }

    
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
