import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClockTwoComponent } from './clock-two.component';

describe('ClockTwoComponent', () => {
  let component: ClockTwoComponent;
  let fixture: ComponentFixture<ClockTwoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClockTwoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClockTwoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
