import { Injectable } from '@angular/core';
import { LoginModel } from '../../models/LoginUser';
import { LoggedUser } from '../../models/LoggedUser';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { LoginToken } from '../../models/LoginToken';
import { of } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
	providedIn: 'root'
})
export class AuthService {

	private backendUrl: string = '';
	private userData
	constructor(
		private httpClient: HttpClient,
		private router: Router
	) { }

	authenticateUser(user: LoginModel): Promise<Object> {
		return this.httpClient.post(`${this.backendUrl}/auth`, user)
			.toPromise()
	}
	// return new Promise<Object>((resolve, reject) => {

	checkRoomPassword(password: string): Promise<boolean> {
		return of(true).toPromise();
	}
	getCurrentUser(): Promise<LoggedUser> {
		return new Promise((res) => {
			// wait until the current user is retrieved from backend
			// if it wasn't retrieved already

			res(null);
		});
	}

	//unused?
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

	logout() {
		// complete here
		localStorage.clear();
		this.router.navigate(['/login']);
	}


}
