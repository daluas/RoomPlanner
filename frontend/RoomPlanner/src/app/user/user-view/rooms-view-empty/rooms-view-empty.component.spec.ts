import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RoomsViewEmptyComponent } from './rooms-view-empty.component';

describe('RoomsViewEmptyComponent', () => {
  let component: RoomsViewEmptyComponent;
  let fixture: ComponentFixture<RoomsViewEmptyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RoomsViewEmptyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoomsViewEmptyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
