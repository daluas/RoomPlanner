import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { MaterialDesignModule } from '../material-design/material-design.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { AuthService } from '../core/core.module';
import { Router } from '@angular/router';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';


fdescribe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let de: DebugElement;
  let elem: HTMLElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        LoginComponent
      ],
      imports: [
        MaterialDesignModule,
        ReactiveFormsModule,
        FormsModule
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(LoginComponent);
      component = fixture.componentInstance;
      de = fixture.debugElement.query(By.css('form'));
      elem = de.nativeElement;
      fixture.detectChanges();
    });
  }));


  it('should create', () => {
    const comp = fixture.debugElement.componentInstance;
    expect(comp).toBeTruthy();
  });

  it(`should have as text 'Login'`, async(() => {
    fixture = TestBed.createComponent(LoginComponent);
    
    const h1Tag = fixture.nativeElement.querySelector("h1");
    expect(h1Tag.text).toContain('Login');
  }))

  it(`form should be invalid`, async(() => {
    component.loginForm.controls['email'].setValue('');
    component.loginForm.controls['password'].setValue('');
    expect(component.loginForm.valid).toBeFalsy();
  }))

  it(`form should be valid`, async(() => {
    component.loginForm.controls['email'].setValue('user1@cegeka.ro');
    component.loginForm.controls['password'].setValue('user.1');
    expect(component.loginForm.valid).toBeTruthy();
  }))

});
