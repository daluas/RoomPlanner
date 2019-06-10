import { Injectable } from '@angular/core';
import { LoginModel } from '../../models/LoginUser';
import { LoggedUser } from '../../models/LoggedUser';
import { HttpClient, HttpHeaders, HttpResponse, HttpErrorResponse } from "@angular/common/http";
import { LoginToken } from '../../models/LoginToken';
import { Observable, of } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
	providedIn: 'root'
})
export class AuthService {


	private backendUrl: string = '';
	private userData;
	private currentUser: LoggedUser;


	constructor(
		private httpClient: HttpClient,
		private router: Router
	) {

	}
	setCurrentUser(loggedUserModel: LoggedUser): any {
		this.currentUser = loggedUserModel;
	}

	authenticateUser(credentials: LoginModel) {
		return this.httpClient.post(`${this.backendUrl}/auth/signin`, credentials)
			.toPromise()
			// .then((data) => {
			// 	console.log(data);

			// 	let user: LoggedUser = new LoggedUser().create(data);
			// 	this.setCurrentUser(user);

			// 	return of(new HttpResponse({
			// 		status: 200,
			// 		body: user
			// 	}));

			// }).catch(error => {
			// 	console.log(error)
			// 	return error
			// })
	}
	// return new Promise<Object>((resolve, reject) => {

	checkRoomPassword(password: string): Promise<boolean> {


		return of(true).toPromise();
	}
	getCurrentUser(): LoggedUser {
		if (this.currentUser === undefined) return null;
		return this.currentUser;
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
