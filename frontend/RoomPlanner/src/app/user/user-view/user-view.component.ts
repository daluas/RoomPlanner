import { Component, OnInit, ViewChild } from '@angular/core';
import { MatCalendar, DateAdapter } from '@angular/material';
import { RoomDataService } from '../../core/services/room-data/room-data.service';
import { RoomModel } from '../../core/models/RoomModel';
import { Filters } from '../../shared/models/Filters';
import { Time } from '@angular/common';


@Component({
  selector: 'app-user-view',
  templateUrl: './user-view.component.html',
  styleUrls: ['./user-view.component.css']
})
export class UserViewComponent implements OnInit {
  ngOnInit(): void { }
}
