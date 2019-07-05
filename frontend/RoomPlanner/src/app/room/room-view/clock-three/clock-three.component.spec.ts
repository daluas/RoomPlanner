import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClockThreeComponent } from './clock-three.component';

describe('ClockThreeComponent', () => {
  let component: ClockThreeComponent;
  let fixture: ComponentFixture<ClockThreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClockThreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClockThreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
