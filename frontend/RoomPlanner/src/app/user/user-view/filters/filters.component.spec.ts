import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FiltersComponent } from './filters.component';
import { DebugElement, SimpleChange } from '@angular/core';
import { MaterialDesignModule } from 'src/app/material-design/material-design.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { By } from '@angular/platform-browser';
import { HourInputComponent } from '../../../shared/hour-input/hour-input.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RoomDataService } from '../../../core/services/room-data/room-data.service';
import { RoomModel } from '../../../core/models/RoomModel';
import { FloorModel } from '../../../core/models/FloorModel';

describe('FiltersComponent', () => {
  
  let component: FiltersComponent;
  let fixture: ComponentFixture<FiltersComponent>;

  function getButton(): DebugElement {
    return fixture.debugElement.query(By.css('button'));
  }

  function getCalendar(): DebugElement {
    return fixture.debugElement.query(By.css('mat-calendar'));
  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        FiltersComponent,
        HourInputComponent
      ],
      imports: [
        MaterialDesignModule,
        ReactiveFormsModule,
        HttpClientTestingModule,
        FormsModule,
        BrowserAnimationsModule
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FiltersComponent);
    component = fixture.componentInstance;
    component.buildingLayout=new Array<FloorModel>();
    component.buildingLayout.push(new FloorModel().create({
      id: 1,
      floor: 5,
      rooms: [
          new RoomModel().create(
              {
                  id: 2,
                  email: "wonderland@yahoo.com",
                  type: "ROOM",
                  reservations: [],
                  name: "Wonderland",
                  floor: 5,
                  maxPersons: 14
              })
      ]
  }), new FloorModel().create(
      {

          id: 2,
          floor: 8,
          rooms: [new RoomModel().create(
              {
                  id: 3,
                  email: "westeros@yahoo.com",
                  type: "ROOM",
                  reservations: [],
                  name: "Westeros",
                  floor: 8,
                  maxPersons: 20
              })
          ]
      }), new FloorModel().create(
          {
              id: 3,
              floor: 4,
              rooms: [
                  {
                      id: 4,
                      email: "neverland@yahoo.com",
                      type: "ROOM",
                      reservations: [],
                      name: "Neverland",
                      floor: 4,
                      maxPersons: 5
                  }
              ]
          })
      )

    component.ngOnChanges({ buildingLayout: new SimpleChange(null, component.buildingLayout,true)})
  
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('default day should be the current day', () => {
    var date = new Date();
    expect(component.defaultDate.getDate().toString).toEqual(date.getDate().toString);
  });

});
