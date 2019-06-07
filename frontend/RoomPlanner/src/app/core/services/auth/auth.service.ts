import { Injectable } from '@angular/core';
import { LoginModel } from '../../models/LoginUser';
import { LoggedUser } from '../../models/LoggedUser';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { LoginToken } from '../../models/LoginToken';

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
    //   .then(data => {
    //     console.log(data) // user_id, token, 
    //     return "ok";
    //   })
    //   .catch(error => {
    //     console.log(error);
    //     return "error";
    //   });
    // return "nothing"
  }

  validateAndSendRequest(token: string, request: any): Promise<Object> {
    this.headers.append("Authorization", token)
    return this.httpClient.post(`${this.backendUrl}`, {}, { headers: this.headers }).toPromise();
  }


  setToken(token: LoginToken): boolean {
    localStorage.setItem(token.name, token.value);
    return true;
  }

  checkToken(token: string): boolean {
    let expirationTime = localStorage.getItem(token); // probably date of expiration ?
    console.log(expirationTime);
    return false;
  }

  //add 24h to token duration
  updateToken(token: string): boolean {
    let expirationDate = localStorage.getItem(token);

    let newExpirationDate = new Date(new Date(expirationDate).getTime() + 60 * 60 * 24 * 1000);
    let newExpirationDateStr = newExpirationDate.toString();
    console.log(newExpirationDate, newExpirationDateStr);
    localStorage.setItem(token, newExpirationDateStr);
    return true;
  }

  // login(clientData: LoginModel){
  login(email: string, password: string): Promise<LoggedUser> {
    let clientData: LoginModel = new LoginModel().create({
      email: email,
      password: password
    });
    // this.httpClient.post(this.backendUrl, clientData, this.tokenHeader)
    let loggedUser: LoggedUser = {
      type: '',
      username: ''
    }

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
