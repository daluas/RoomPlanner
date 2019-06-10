import { Component, OnInit, ViewChild } from '@angular/core';
import { MatCalendar,DateAdapter } from '@angular/material';
import { UserdataService } from '../../shared/services/userdata.service';
import { RoomModel } from '../../core/models/RoomModel';

@Component({
  selector: 'app-user-view',
  templateUrl: './user-view.component.html',
  styleUrls: ['./user-view.component.css']
})
export class UserViewComponent implements OnInit {

  dropdownOpen: boolean = false;
  dateChanged:boolean=false;

  constructor(
    private _dateAdapter: DateAdapter<Date>,
    private userdataService: UserdataService
  ) { }

  ngOnInit() {
    
  }

  toggleDropdown(){
    this.dropdownOpen = !this.dropdownOpen;
  }

  defaultDate: Date = this._dateAdapter.today();;
  selectedDate: Date;

  finalDate:Date;

  onDateChanged(date) {
    this.dateChanged=true;
  }

  onApplyFilters(){

    if(this.dateChanged){
      console.log(`The date is: ${this.selectedDate}`);
      this.finalDate=this.selectedDate;
      let rooms: RoomModel[] = this.userdataService.getRoomsByDate(new Date(this.selectedDate));
    }
    else{
      console.log(`The date is: ${this.defaultDate}`);
      this.finalDate=this.defaultDate;
    }
    // var datee=[];
    // var datee2=[];
    // for(let i=0;i<30;i++){
    //   datee[i]=document.getElementsByClassName("mat-calendar-body-cell-content")[i];
    //   datee2[i]=document.getElementsByClassName("mat-calendar-body-cell-content")[i].firstChild;
      
    //   if((parseInt(datee2[i], 10))<7){
    //     datee[i].classList.add('example-custom-date-class');
    //   }
    //   console.log(datee[i]);
    

    
  }

 
}
