import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HourInputComponent } from './hour-input.component';
import { MaterialDesignModule } from '../../material-design/material-design.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpClient } from 'selenium-webdriver/http';
import { AuthGuard } from '../../core/guards/auth.guard';
import { Router } from '@angular/router';

describe('HourInputComponent', () => {
  let component: HourInputComponent;
  let fixture: ComponentFixture<HourInputComponent>;

  let routerMock = { navigate: (path: string) => { } }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HourInputComponent],
      imports: [
        MaterialDesignModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
        FormsModule,
        HttpClientTestingModule
      ],
      providers: [{ provide: Router, useValue: routerMock}]
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
