import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FiltersComponent } from './filters.component';
import { DebugElement } from '@angular/core';
import { MaterialDesignModule } from 'src/app/material-design/material-design.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { By } from '@angular/platform-browser';

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
        FiltersComponent
      ],
      imports: [
        MaterialDesignModule,
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

  it('should call **onApplyFilters** when button is clicked ', () => {

    const elem: DebugElement = getButton();
    spyOn(component, 'onApplyFilters');

    elem.nativeElement.click();

    fixture.whenStable().then(() => {
      expect(component.onApplyFilters).toHaveBeenCalled();
    });
    
  });

  it('should call **onDateChanged** with date picked by user when calendar is clicked ', () => {

    var newDate=new Date(2019,6,16);

    const calendarElem:DebugElement=getCalendar();
   
    spyOn(component,'onDateChanged');

    calendarElem.nativeElement.selected=newDate;

    fixture.whenStable().then(() => {
      expect(component.onDateChanged).toHaveBeenCalledWith(newDate);
    });
  });

  
  
});
