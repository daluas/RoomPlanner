import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HourInputComponent } from './hour-input.component';
import { MaterialDesignModule } from '../../material-design/material-design.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

describe('HourInputComponent', () => {
  let component: HourInputComponent;
  let fixture: ComponentFixture<HourInputComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HourInputComponent ]
   ,
    imports: [
      MaterialDesignModule,
      BrowserAnimationsModule,
      ReactiveFormsModule,
      FormsModule,
    ]
  })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HourInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
