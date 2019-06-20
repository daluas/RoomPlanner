import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserViewComponent } from './user-view.component';
import { MaterialDesignModule } from '../../material-design/material-design.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FiltersComponent } from './filters/filters.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { RoomsViewComponent } from './rooms-view/rooms-view.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AuthGuard } from 'src/app/core/guards/auth.guard';
import { Router } from '@angular/router';

describe('UserViewComponent', () => {
  let component: UserViewComponent;
  let fixture: ComponentFixture<UserViewComponent>;
  let routerMock = {navigate: (path: string) => {}}

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        UserViewComponent,
        FiltersComponent,
        RoomsViewComponent
      ],
      imports: [
        MaterialDesignModule,
        BrowserAnimationsModule,
        SharedModule,
        HttpClientTestingModule
      ],
      providers: [AuthGuard, { provide: Router, useValue: routerMock},]
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
