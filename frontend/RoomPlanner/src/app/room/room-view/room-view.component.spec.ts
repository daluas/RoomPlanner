import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RoomViewComponent } from './room-view.component';
import { ClockComponent } from './clock/clock.component';
import { ClockThreeComponent } from './clock-three/clock-three.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { RoomRoutingModule } from '../room-routing.module';
import { CommonModule } from '@angular/common';

describe('RoomViewComponent', () => {
  let component: RoomViewComponent;
  let fixture: ComponentFixture<RoomViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RoomViewComponent, ClockComponent, ClockThreeComponent ],
      imports:[
        CommonModule,
        RoomRoutingModule,
        SharedModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoomViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
