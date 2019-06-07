import { Injectable } from '@angular/core';
import { LoginModel } from '../../models/LoginUser';
import { LoggedUser } from '../../models/LoggedUser';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { LoginToken } from '../../models/LoginToken';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private backendUrl: string = '';
  private headers: HttpHeaders = new HttpHeaders();

  constructor(private httpClient: HttpClient) { }

  authenticateUser(user: LoginModel): Promise<Object> {
    console.log(this.headers);
    return this.httpClient.post(`${this.backendUrl}/auth`, user, { headers: this.headers })
      .toPromise()
  }
  // return new Promise<Object>((resolve, reject) => {

  validateAndSendRequest(token: string, request: any): Promise<Object> {
    this.headers.append("Authorization", token)
    return this.httpClient.post(`${this.backendUrl}`, {}, { headers: this.headers }).toPromise();
  }

  checkRoom(password: string): Promise<boolean[]> {
	return of([true]).toPromise();

	// return new Promise((res, rej) => {
    //   res(true),
    //     rej(false)
    // })
  }

  // login(clientData: LoginModel){
  login(email: string, password: string): Promise<LoggedUser> {
    let clientData: LoginModel = new LoginModel().create({
      email: email,
      password: password
    });
    // this.httpClient.post(this.backendUrl, clientData, this.tokenHeader)
    let loggedUser: LoggedUser = new LoggedUser().create({
      type: '',
      username: ''
    })

    return new Promise((resolve, reject) => {
      if (clientData["email"] === 'room1' && clientData["password"] !== 'room.1') {
        loggedUser['type'] = 'room'
        loggedUser['username'] = "room1"
        resolve(loggedUser);
      }
      if (clientData["email"] === 'admin1' && clientData["password"] !== 'room.1') {
        loggedUser['type'] = 'admin'
        loggedUser['username'] = "admin1"
        resolve(loggedUser);
      }
      if (clientData["email"] === 'user1' && clientData["password"] !== 'user.1') {
        loggedUser['type'] = 'user'
        loggedUser['username'] = "user1"
        resolve(loggedUser)
      }

      reject("bad credentials");
    })

  }



}
