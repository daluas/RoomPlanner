import { TestBed, async, ComponentFixture } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { FormsModule,ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { NavbarComponent } from './navbar/navbar.component';
import { AppRoutingModule } from './app-routing.module';
import { CoreModule } from './core/core.module';
import { MaterialDesignModule } from './material-design/material-design.module';
import { SharedModule } from './shared/shared.module';

describe('AppComponent', () => {

  let app:AppComponent;
  let fixture: ComponentFixture<AppComponent>;


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent,
        LoginComponent,
        NotFoundComponent,
        NavbarComponent
      ],
      imports: [
        RouterTestingModule,
        AppRoutingModule,
        CoreModule,
        MaterialDesignModule,
        SharedModule,
        ReactiveFormsModule
      ]
      
    }).compileComponents();
  }));

  it('should create the app', () => {
    fixture = TestBed.createComponent(AppComponent);
    app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });
 
  it(`should have as title 'RoomPlanner'`, () => {
    fixture = TestBed.createComponent(AppComponent);
    app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('RoomPlanner');
  });

 
});
