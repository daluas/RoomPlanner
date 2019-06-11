import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserViewComponent } from './user-view.component';
import { MaterialDesignModule } from '../../material-design/material-design.module';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

fdescribe('UserViewComponent', () => {
  let component: UserViewComponent;
  let fixture: ComponentFixture<UserViewComponent>;

  function getButton(): DebugElement {
    return fixture.debugElement.query(By.css('button'));
  }

  function getCalendar(): DebugElement {
    return fixture.debugElement.query(By.css('mat-calendar'));
  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        UserViewComponent
      ],
      imports: [
        MaterialDesignModule,
        BrowserAnimationsModule
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserViewComponent);
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

  
  it('should return the date picked by user when button "Apply Filters" is clicked ', () => {

    var newDate=new Date(2019,6,16);

    const calendarElem:DebugElement=getCalendar();
    calendarElem.nativeElement.selected=newDate;

    const elem: DebugElement = getButton();
    elem.nativeElement.click();

    spyOn(component, 'getReturnDate');

    fixture.whenStable().then(() => {
      expect(component.getReturnDate().valueOf).toEqual(newDate);
    });
  });


});
