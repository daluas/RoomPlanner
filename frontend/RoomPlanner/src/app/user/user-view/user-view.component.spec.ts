import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserViewComponent } from './user-view.component';
import { MaterialDesignModule } from '../../material-design/material-design.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FiltersComponent } from './filters/filters.component';

describe('UserViewComponent', () => {
  let component: UserViewComponent;
  let fixture: ComponentFixture<UserViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        UserViewComponent,
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
    fixture = TestBed.createComponent(UserViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});
