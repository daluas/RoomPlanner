import { Component, OnInit, ViewChild } from '@angular/core';
import { MatCalendar,DateAdapter } from '@angular/material';

@Component({
  selector: 'app-user-view',
  templateUrl: './user-view.component.html',
  styleUrls: ['./user-view.component.css']
})
export class UserViewComponent implements OnInit {

  dropdownOpen: boolean = false;
  dateChanged:boolean=false;

  constructor(private _dateAdapter: DateAdapter<Date>) { }

  ngOnInit() {
    
  }

  toggleDropdown(){
    this.dropdownOpen = !this.dropdownOpen;
  }

  defaultDate: Date = this._dateAdapter.today();;
  selectedDate: Date;

  onDateChanged(date) {
    this.dateChanged=true;
  }

  onApplyFilters(){
    if(this.dateChanged){
      console.log(`The date is: ${this.selectedDate}`);
    }
    else{
      console.log(`The date is: ${this.defaultDate}`);
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
