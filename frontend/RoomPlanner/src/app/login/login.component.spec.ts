import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { MaterialDesignModule } from '../material-design/material-design.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { AuthService } from '../core/core.module';
import { Router } from '@angular/router';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AuthGuard } from '../core/guards/auth.guard';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


fdescribe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let de: DebugElement;
  let elem: HTMLElement;
  let routerMock = { navigate: (path: string) => { } }
  let submitButton: HTMLButtonElement;
  let statusMessage: HTMLDivElement;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        LoginComponent
      ],
      imports: [
        MaterialDesignModule,
        ReactiveFormsModule,
        HttpClientTestingModule,
        FormsModule,
        BrowserAnimationsModule
      ],
      providers: [AuthGuard, { provide: Router, useValue: routerMock },],
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(LoginComponent);
      component = fixture.componentInstance;
      de = fixture.debugElement.query(By.css('form'));
      elem = de.nativeElement;
      fixture.detectChanges();
      submitButton = fixture.debugElement.query(By.css('.login-button')).nativeElement;
      statusMessage = fixture.debugElement.query(By.css('.statusMessage')).nativeElement;
    });
  }));


  it('should create', () => {
    const comp = fixture.debugElement.componentInstance;
    expect(comp).toBeTruthy();
  });

  it(`form should be invalid`, async(() => {
    component.loginForm.controls['email'].setValue('');
    component.loginForm.controls['password'].setValue('');
    fixture.detectChanges();
    expect(component.loginForm.valid).toBeFalsy();
  }))

  it(`form should be valid`, async(() => {
    component.loginForm.controls['email'].setValue('user1@cegeka.ro');
    component.loginForm.controls['password'].setValue('user.1');
    fixture.detectChanges();
    expect(component.loginForm.valid).toBeTruthy();
  }))

  it(`form should be invalid when email is invalid`, async(() => {
    component.loginForm.controls['email'].setValue('user1');
    component.loginForm.controls['password'].setValue('pass');
    fixture.detectChanges();
    expect(component.loginForm.valid).toBeFalsy();
  }))

  it(`submit button should be disabled when form is invalid`, async(() => {
    component.loginForm.controls['email'].setValue('');
    component.loginForm.controls['password'].setValue('');
    fixture.detectChanges();
    expect(submitButton.disabled).toBeTruthy();

  }))

  it(`submit button should be enabled when form is valid`, async(() => {
    component.loginForm.controls['email'].setValue('user1@cegeka.ro');
    component.loginForm.controls['password'].setValue('pass');
    fixture.detectChanges();
    expect(submitButton.disabled).toBeFalsy();
  }))

  // it(`status message should be 'Login successfully' when credentials are valid`, async(() => {
  //   component.loginForm.controls['email'].setValue('user1@cegeka.ro');
  //   component.loginForm.controls['password'].setValue('user.1');
  //   fixture.detectChanges();
  //   submitButton.click();
  //   component.onSubmit();
  //   fixture.detectChanges();
  
  //   expect(statusMessage.textContent).toBe('Login successfully');
  // }))

});
