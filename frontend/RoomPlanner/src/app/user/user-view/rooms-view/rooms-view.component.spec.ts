import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RoomsViewComponent } from './rooms-view.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { MaterialDesignModule } from 'src/app/material-design/material-design.module';

describe('RoomsViewComponent', () => {
  let component: RoomsViewComponent;
  let fixture: ComponentFixture<RoomsViewComponent>;
  let routerMock = {navigate: (path: string) => {}}

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RoomsViewComponent ],
      imports: [HttpClientTestingModule, MaterialDesignModule],
      providers: [{ provide: Router, useValue: routerMock},]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoomsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
