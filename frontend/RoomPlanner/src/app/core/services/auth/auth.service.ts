import { Injectable } from '@angular/core';
import { LoginModel } from '../../models/LoginUser';
import { LoggedUser } from '../../models/LoggedUser';
import { HttpClient } from "@angular/common/http";
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private backendUrl = ''
  private tokenHeader = '';
  constructor(private httpClient: HttpClient) { }

  // login(clientData: LoginModel){
  login(email: string, password: string) {
    let clientData: LoginModel = {
      email: email,
      password: password
    };
    // this.httpClient.post(this.backendUrl, clientData, this.tokenHeader)
    let loggedUser : LoggedUser = {
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
