import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FiltersComponent } from './filters.component';
import { DebugElement } from '@angular/core';
import { MaterialDesignModule } from 'src/app/material-design/material-design.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { By } from '@angular/platform-browser';
import { HourInputComponent } from '../../../shared/hour-input/hour-input.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

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
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('default day should be the current day', () => {
    var date=new Date();
    expect(component.defaultDate.toString).toEqual(date.toString);
  });


  
  
});
